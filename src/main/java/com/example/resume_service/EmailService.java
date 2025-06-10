package com.example.resume_service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static final String SUBJECT = "Resume – Adesh Malunjkar | Java Backend Developer";

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

    private static final String ATTACHMENT_PATH = "/home/adeshmalunjkar/me/temp/final-resume/AdeshResume_1.2.pdf";

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResume(List<String> recipients) {
        FileSystemResource file = new FileSystemResource(new File(ATTACHMENT_PATH));
        if (!file.exists()) {
            logger.error("Attachment file not found: {}", ATTACHMENT_PATH);
            return;
        }

        for (String email : recipients) {
            try {
                trim(email); // Ensure email is trimmed
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(email);
                helper.setSubject(SUBJECT);
                helper.setText(BODY, false); // false = plain text, set true for HTML

                helper.addAttachment("Adesh_Malunjkar_Resume.pdf", file);

                mailSender.send(message);
                logger.info("Resume sent to {}", email);

            } catch (MessagingException e) {
                logger.error("Failed to send resume to {}: {}", email, e.getMessage(), e);
            }
        }
    }

    private static void  trim(String str){
        if (str == null) {
            return ;
        }
        str= str.trim();
    }
}
