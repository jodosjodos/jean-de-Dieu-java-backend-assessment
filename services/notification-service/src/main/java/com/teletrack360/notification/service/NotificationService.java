package com.teletrack360.notification.service;

import com.teletrack360.notification.entity.Notification;
import com.teletrack360.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    @Transactional
    public void sendEmailNotification(String recipient, String subject, String body) {
        Notification notification = Notification.builder()
                .type("EMAIL")
                .recipient(recipient)
                .subject(subject)
                .body(body)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
        
        try {
            // Simulate email sending
            log.info("=== EMAIL NOTIFICATION ===");
            log.info("To: {}", recipient);
            log.info("Subject: {}", subject);
            log.info("Body: {}", body);
            log.info("========================");
            
            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now());
            
        } catch (Exception e) {
            log.error("Failed to send email notification", e);
            notification.setStatus("FAILED");
            notification.setErrorMessage(e.getMessage());
            notification.setRetryCount(notification.getRetryCount() + 1);
        }
        
        notificationRepository.save(notification);
    }
    
    @Transactional
    public void sendSmsNotification(String recipient, String body) {
        Notification notification = Notification.builder()
                .type("SMS")
                .recipient(recipient)
                .body(body)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
        
        try {
            // Simulate SMS sending
            log.info("=== SMS NOTIFICATION ===");
            log.info("To: {}", recipient);
            log.info("Message: {}", body);
            log.info("=======================");
            
            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now());
            
        } catch (Exception e) {
            log.error("Failed to send SMS notification", e);
            notification.setStatus("FAILED");
            notification.setErrorMessage(e.getMessage());
        }
        
        notificationRepository.save(notification);
    }
}
