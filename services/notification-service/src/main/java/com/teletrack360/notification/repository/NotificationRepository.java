package com.teletrack360.notification.repository;

import com.teletrack360.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByStatusAndRetryCountLessThan(String status, Integer maxRetries);
    
    List<Notification> findByRecipient(String recipient);
    
    List<Notification> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
