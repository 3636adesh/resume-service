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

            Please find my resume attached for the Java Backend Developer position.

            Below are my details for your consideration:

            - Total Experience: 3 years (Java, Spring Boot, Microservices)
            - Relevant Experience: 3 years in building scalable cloud-native apps
            - Current CTC: ₹11.44 LPA
            - Expected CTC: ₹16 – ₹20 LPA
            - Notice Period: 30 days (Negotiable)
            - Current Location: Noida, UP
            - Preferred Location: Pune / Remote

            Highlights:
            - Reduced API latency by 60% for an EdTech platform handling 180k+ users/month
            - Experienced with AKS, Kafka, PostgreSQL, Redis, and CI/CD pipelines
            - Built secure microservices with Spring Security & OAuth 2.0
            - Finalist at SAP BTP Hackathon 2024 (Invoice matching microservice)

            Thank you for your time and consideration. I look forward to your response.

            Best regards,
            Adesh Malunjkar
            +91-88568 35971
            3636adesh@gmail.com
            """;


    private static final String SUBJECT = "Resume - Adesh Malunjkar | Java / Software Developer | 3 Yrs Exp";


}
