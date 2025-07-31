package com.example.resume_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource(value = "classpath:resume-template.properties")
@ConfigurationProperties(prefix = "email")
public class ResumeTemplateForFullTime extends ResumeTemplate {

}
