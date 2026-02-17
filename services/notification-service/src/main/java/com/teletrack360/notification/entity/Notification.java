package com.teletrack360.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String type; // EMAIL, SMS, SLACK
    
    @Column(nullable = false, length = 100)
    private String recipient;
    
    @Column(length = 200)
    private String subject;
    
    @Column(columnDefinition = "TEXT")
    private String body;
    
    @Column(nullable = false, length = 20)
    private String status; // PENDING, SENT, FAILED
    
    @Column
    private LocalDateTime sentAt;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private String errorMessage;
    
    private Integer retryCount = 0;
}
