package com.example.resume_service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    public SMTPService(JavaMailSender mailSender, ResumeTemplate resumeTemplate) {
        this.mailSender = mailSender;
        this.resumeTemplate = resumeTemplate;
    }

    private final AtomicInteger emailSentCount = new AtomicInteger(0);

    @Async("emailExecutor")
    CompletableFuture<Void>  sendEmail(String email, FileSystemResource file) throws MessagingException {
        logger.info("Sending resume to {}", email);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");

        helper.setTo(email);
        helper.setSubject(resumeTemplate.getSubject());
        helper.setText(resumeTemplate.getBody(), true);
        helper.addAttachment(resumeTemplate.getAttachmentName(), file);

        mailSender.send(message);
        logger.info("Resume sent to {}", email);
        emailSentCount.incrementAndGet();
        return CompletableFuture.completedFuture(null);
    }

    public int getEmailSentCount() {
        return emailSentCount.get();
    }


}
