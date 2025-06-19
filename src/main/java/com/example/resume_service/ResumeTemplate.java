package com.example.resume_service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource(value = "classpath:resume-template.properties")
@ConfigurationProperties(prefix = "email")
public class ResumeTemplate {

    private String subject;
    private String body;
    private String attachmentName;


    public String getAttachmentName() {
        return attachmentName;
    }
    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public ResumeTemplate() {
        // Default constructor
    }
    public ResumeTemplate(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }
}
