package com.he190620.mvc.task1_website.service;

import com.he190620.mvc.task1_website.dto.FeedbackForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class FeedbackEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public FeedbackEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendFeedback(FeedbackForm feedback) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(feedback.getEmail());
        mailMessage.setSubject(feedback.getSubject());

        String content = """
                Hello %s,
                
                Thank you for your feedback.
                
                ------------------------------
                Name: %s
                Email: %s
                Subject: %s
                
                Message:
                %s
                ------------------------------
                
                Course Registration System
                """.formatted(
                feedback.getName(),
                feedback.getName(),
                feedback.getEmail(),
                feedback.getSubject(),
                feedback.getMessage()
        );

        mailMessage.setText(content);

        mailSender.send(mailMessage);
    }
}
