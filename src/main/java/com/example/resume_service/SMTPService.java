package com.example.resume_service;

import com.example.resume_service.config.ResumeTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SMTPService {

    private static final Logger logger = LoggerFactory.getLogger(SMTPService.class);

    private final JavaMailSender mailSender;
    private final ResumeTemplate resumeTemplate;
    private final ResumeTemplate resumeTemplateForFreelance;
    private final AtomicInteger emailSentCount = new AtomicInteger(0);

    public SMTPService(JavaMailSender mailSender,
                       @Qualifier("resumeTemplateForFullTime") ResumeTemplate resumeTemplate,
                       @Qualifier("resumeTemplateForFreeLance") ResumeTemplate resumeTemplateForFreelance) {
        this.mailSender = mailSender;
        this.resumeTemplate = resumeTemplate;
        this.resumeTemplateForFreelance = resumeTemplateForFreelance;
    }

    @Async("emailExecutor")
    public CompletableFuture<Void> sendEmail(String email, FileSystemResource file) {
        return send(email, file, resumeTemplate);
    }

    @Async("emailExecutor")
    public CompletableFuture<Void> sendEmailForFreelancing(String email, FileSystemResource file) {
        return send(email, file, resumeTemplateForFreelance);
    }

    private CompletableFuture<Void> send(String email, FileSystemResource file, ResumeTemplate template) {
        if (email == null || email.isBlank()) {
            logger.warn("Email address is empty. Skipping email send.");
            return CompletableFuture.completedFuture(null);
        }

        try {
            logger.info("Sending resume to {}", email);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(template.getSubject());
            helper.setText(template.getBody(), true);
            helper.addAttachment(template.getAttachmentName(), file);

            mailSender.send(message);

            emailSentCount.incrementAndGet();
            logger.info("Resume sent successfully to {}", email);
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", email, e.getMessage(), e);
        }

        return CompletableFuture.completedFuture(null);
    }

    public int getEmailSentCount() {
        return emailSentCount.get();
    }

    public void resetEmailSentCount() {
        emailSentCount.set(0);
    }
}
