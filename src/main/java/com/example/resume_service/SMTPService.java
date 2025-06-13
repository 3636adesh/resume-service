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
        helper.setText(BODY, false); // false = plain text, set true for HTML

        helper.addAttachment("Adesh_Malunjkar_Resume.pdf", file);

        mailSender.send(message);
        logger.info("Resume sent to {}", email);
    }

    private static final String BODY = """
            Hi,
            Please find attached my resume for your consideration. I am a Java backend developer with 3 years of experience in building scalable microservices and cloud-native solutions.

            Looking forward to the opportunity to connect.
    
             🔹 Total experience: 3 yrs
             🔹 Notice Period : 30 days
             🔹 Current CTC: 11,44,000 LPA
             🔹 Expected CTC: 16 to 20 LPA
             🔹 Current Location: Noida,UP
             🔹 Preferred Location: Pune/Remote

            Best regards,
            Adesh Malunjkar
            📞 +91-88568 35971
            📧 3636adesh@gmail.com
            """;


    private static final String SUBJECT = "Resume – Adesh Malunjkar | Java Backend Developer";



}
