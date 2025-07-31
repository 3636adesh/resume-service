package com.example.resume_service;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.sql.Date;

@Entity
public class ResumeLog {

    @Id
    @GeneratedValue
    private Long id;
    private int sentCount;
    private Date sentDate;

    public ResumeLog() {
    }
    public ResumeLog(Long id, int sentCount, Date sentDate) {
        this.id = id;
        this.sentCount = sentCount;
        this.sentDate = sentDate;
    }

    public ResumeLog( int sentCount) {
        this.sentCount = sentCount;
        this.sentDate = java.sql.Date.valueOf(java.time.LocalDate.now());
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getSentCount() {
        return sentCount;
    }
    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }
    public Date getSentDate() {
        return sentDate;
    }
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}
