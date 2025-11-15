package com.example.resume_service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public String send(@RequestBody List<String> to) {
        emailService.sendResume(to);
        return "Resume email sent to " + to;
    }

    @PostMapping("/free-lancing")
    public String sendForFreeLancing(@RequestBody List<String> to) {
        emailService.sendResumeForFreeLancing(to);
        return "Resume email sent to " + to;
    }
}
