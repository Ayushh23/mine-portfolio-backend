package com.shiva.portfolio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.to}")
    private String recipientEmail;

    /**
     * Sends a contact form email to your Gmail inbox.
     *
     * @param senderName    Name entered by the visitor
     * @param senderEmail   Email entered by the visitor
     * @param message       Message body entered by the visitor
     */
    @Async
    public void sendContactEmail(String senderName, String senderEmail, String message) {
        try {
            log.info("Starting async email sending process for sender: {}", senderEmail);
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setFrom(recipientEmail);
            // Send TO yourself
            mail.setTo(recipientEmail);

            // Reply-To so you can click "Reply" in Gmail and it goes to the visitor
            mail.setReplyTo(senderEmail);

            mail.setSubject("📬 Portfolio Contact: Message from " + senderName);
            mail.setText(
                "You received a new message from your portfolio contact form.\n\n" +
                "-------------------------------------------\n" +
                "Name   : " + senderName + "\n" +
                "Email  : " + senderEmail + "\n" +
                "-------------------------------------------\n\n" +
                message + "\n\n" +
                "-------------------------------------------\n" +
                "Reply directly to this email to respond to " + senderName + "."
            );

            mailSender.send(mail);
            log.info("Successfully sent notification email to self.");

            // Send auto-reply to the visitor
            sendAutoReply(senderName, senderEmail);
        } catch (Exception e) {
            log.error("Failed to send async email! Error: {}", e.getMessage(), e);
        }
    }

    private void sendAutoReply(String senderName, String senderEmail) {
        SimpleMailMessage autoReply = new SimpleMailMessage();
        
        autoReply.setFrom(recipientEmail);
        autoReply.setTo(senderEmail);
        autoReply.setSubject("Thank you for reaching out!");
        
        String autoReplyText = "Hi " + senderName + ",\n\n" +
            "Thank you for reaching out via my portfolio website!\n\n" +
            "This is an automated confirmation to let you know that I have received your message. " +
            "I will review it and get back to you within 4-6 hours.\n\n" +
            "Best regards,\n" +
            "Ayush Raj\n\n" +
            "(Note: This is an automated response, but you can reply directly to this email to reach me.)";
            
        autoReply.setText(autoReplyText);
        
        mailSender.send(autoReply);
        log.info("Successfully sent auto-reply email to visitor: {}", senderEmail);
    }
}
