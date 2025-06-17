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


@Service
public class SMTPService {

    private static final Logger logger = LoggerFactory.getLogger(SMTPService.class);

    private final JavaMailSender mailSender;

    public SMTPService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async("emailExecutor")
    void sendEmail(String email, FileSystemResource file) throws MessagingException {
        logger.info("Sending resume to {}", email);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject(SUBJECT);
        helper.setText(BODY, false);

        String fileName = "Adesh_Malunjkar_Java_Developer_Resume.pdf";
        helper.addAttachment(fileName, file);


        mailSender.send(message);
        logger.info("Resume sent to {}", email);
    }

    private static final String BODY = """
            Hi,

            I am writing to express my interest in the Java Backend Developer position. I am a Java backend developer with 3 years of experience building scalable microservices and cloud-native solutions.

            Below are my details for your consideration:

            - Total Experience: 3 years (Java, Spring Boot, Microservices)
            - Relevant Experience: 3 years in building scalable cloud-native apps
            - Current CTC: ₹11.44 LPA
            - Expected CTC: ₹16–20 LPA
            - Notice Period: 30 days (Negotiable)
            - Current Location: Noida, UP
            - Preferred Location: Pune / Remote

            Thank you for your time and consideration. I look forward to the opportunity to discuss how I can contribute to your team.

            Best regards,
            Adesh Malunjkar
            +91-88568 35971reboot
            3636adesh@gmail.com
            """;


    private static final String SUBJECT = "Resume - Adesh Malunjkar | Java / Software Developer | 3 Yrs Exp";


}
