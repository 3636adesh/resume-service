package com.example.resume_service.config;

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
    }
    public ResumeTemplate(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }
}
