package com.example.resume_service;


import jakarta.validation.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static final String ATTACHMENT_PATH = "/home/adeshmalunjkar/me/temp/final-resume/AdeshResume.pdf";

    private final SMTPService sMTPService;

    public EmailService(SMTPService sMTPService) {
        this.sMTPService = sMTPService;
    }

    public void sendResume(List<String> recipients) {
        FileSystemResource file = new FileSystemResource(new File(ATTACHMENT_PATH));
        if (!file.exists()) {
            logger.error("Attachment file not found: {}", ATTACHMENT_PATH);
            return;
        }
        recipients = recipients.stream().map(Trim::trim).filter(Objects::nonNull).distinct().toList();

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (String email : recipients) {
            try {
                CompletableFuture<Void> voidCompletableFuture = sMTPService.sendEmail(email, file);
                futures.add(voidCompletableFuture);
            } catch (Exception e) {
                logger.error("Error while sending email via SMTP, email: {}", email, e);
            }
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        int totalSent = sMTPService.getEmailSentCount();
        logger.info("✅  Total emails successfully sent: {}", totalSent);
    }

    static class Trim {
        public static String trim(String str) {
            try {
                return trimming(str);
            } catch (Exception e) {
                logger.error("Error trimming email: {}", str, e);
                return str;
            }
        }

        private static String trimming(@Email String str) {
            return (str == null) ? null : str.trim();

        }
    }


}
