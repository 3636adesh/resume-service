package com.example.resume_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource(value = "classpath:resume-template-free-lancing.properties")
@ConfigurationProperties(prefix = "freelance.email")
public class ResumeTemplateForFreeLance  extends ResumeTemplate {


}
