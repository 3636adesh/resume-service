package com.example.resume_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeTrackingRepository extends JpaRepository<ResumeTracking, Long> {

    boolean existsByRecruiterEmailAndTypeAndStatusIsNot(String recruiterEmail,String type,String status);

}
