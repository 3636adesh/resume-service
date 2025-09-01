package com.example.resume_service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private final SMTPService smtpService;
    private final ResumeLogRepository resumeLogRepository;
    private final ResumeTrackingRepository resumeTrackingRepository;

    @Value("${audit.ignore-emails}")
    private List<String> ignoreEmails;

    @PostConstruct
    public void init() {
        logger.info("Ignoring emails for audit: {}", ignoreEmails);
    }

    public EmailService(SMTPService smtpService,
                        ResumeLogRepository resumeLogRepository,
                        ResumeTrackingRepository resumeTrackingRepository) {
        this.smtpService = smtpService;
        this.resumeLogRepository = resumeLogRepository;
        this.resumeTrackingRepository = resumeTrackingRepository;
    }

    public void sendResume(List<String> recipients) {
        sendResumeInternal(recipients, false);
    }

    public void sendResumeForFreeLancing(List<String> recipients) {
        sendResumeInternal(recipients, true);
    }

    private void sendResumeInternal(List<String> recipients, boolean isFreelancing) {
        FileSystemResource file = new FileSystemResource(new File(ATTACHMENT_PATH));
        if (!file.exists()) {
            logger.error("Attachment file not found: {}", ATTACHMENT_PATH);
            return;
        }

        recipients = recipients.stream()
                .map(Trim::trim)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        int unsentCount = 0;
        for (String email : recipients) {

            if (!ignoreEmails.contains(email) && checkAlreadySent(email, isFreelancing)) {
                logger.warn("⚠️ Email already sent to: {}", email);
                unsentCount++;
                continue;
            }

            ResumeTracking resumeTracking = auditEmailBefore(email, isFreelancing);
            CompletableFuture<Void> future;
            try {
                future = isFreelancing
                        ? smtpService.sendEmailForFreelancing(email, file)
                        : smtpService.sendEmail(email, file);

            } catch (Exception e) {
                logger.error("❌ Error sending email to: {}", email, e);
                auditEmailError(resumeTracking);
                continue;
            }


            CompletableFuture<Void> trackedFuture = future.handle((res, ex) -> {
                if (ex != null) {
                    logger.error("❌ Async error sending email to: {}", email,ex);
                    auditEmailError(resumeTracking);
                } else {
                    logger.info("✅ Email sent successfully to: {}", email);
                    auditEmailSent(resumeTracking);
                }
                return null;
            });

            futures.add(trackedFuture);
        }

        // Wait for all to complete
        futures.forEach(CompletableFuture::join);

        int totalSent = smtpService.getEmailSentCount();
        logger.info("✅ Total emails sent: {}", totalSent);
        if (unsentCount > 0) {
            logger.info("ℹ️ Total emails skipped (already sent): {}", unsentCount);
        }

        resumeLogRepository.save(new ResumeLog(totalSent));
        smtpService.resetEmailSentCount();
    }

    private boolean checkAlreadySent(String email, boolean isFreelancing) {
        if (isFreelancing) {
            return resumeTrackingRepository.existsByRecruiterEmailAndTypeAndStatus(email, ResumeTracking.TYPE_FREE_LANCING, ResumeTracking.STATUS_SENT);
        } else {
            return resumeTrackingRepository.existsByRecruiterEmailAndTypeAndStatus(email, ResumeTracking.TYPE_FULL_TIME, ResumeTracking.STATUS_SENT);
        }

    }

    private ResumeTracking auditEmailBefore(String email, boolean isFreelancing) {
        ResumeTracking tracking = new ResumeTracking();
        tracking.setRecruiterEmail(email);
        tracking.setMessage("📧 Sending resume to " + email);
        if (isFreelancing) {
            tracking.setType(ResumeTracking.TYPE_FREE_LANCING);
        }
        return resumeTrackingRepository.save(tracking);
    }

    private void auditEmailSent(ResumeTracking tracking) {
        tracking.setStatus(ResumeTracking.STATUS_SENT);
        tracking.setMessage("✅ Resume sent successfully.");
        resumeTrackingRepository.save(tracking);
    }

    private void auditEmailError(ResumeTracking tracking) {
        tracking.setStatus(ResumeTracking.STATUS_ERROR);
        tracking.setMessage("❌ Error sending resume.");
        resumeTrackingRepository.save(tracking);
    }

    private static class Trim {
        public static String trim(String str) {
            return (str == null) ? null : str.trim();
        }
    }
}
