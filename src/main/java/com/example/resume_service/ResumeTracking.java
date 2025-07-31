package com.example.resume_service;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.ZonedDateTime;

@Entity
public class ResumeTracking {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_SENT = "SENT";
    public static final String STATUS_ERROR = "ERROR";

    public static final String TYPE_FREE_LANCING = "FREE_LANCING";
    public static final String TYPE_FULL_TIME = "FULL_TIME";

    @Id
    @GeneratedValue
    private Long id;
    private String recruiterEmail;
    private String status = STATUS_PENDING;
    private ZonedDateTime sentAt = ZonedDateTime.now();
    private String type= TYPE_FULL_TIME;
    private String message;

    public ResumeTracking() {
    }

    public ResumeTracking(Long id, String recruiterEmail, String status, ZonedDateTime sentAt, String type, String message) {
        this.id = id;
        this.recruiterEmail = recruiterEmail;
        this.status = status;
        this.sentAt = sentAt;
        this.type = type;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecruiterEmail() {
        return recruiterEmail;
    }

    public void setRecruiterEmail(String recruiterEmail) {
        this.recruiterEmail = recruiterEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
