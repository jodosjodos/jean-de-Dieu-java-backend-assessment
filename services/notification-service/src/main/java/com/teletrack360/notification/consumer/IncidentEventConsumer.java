package com.teletrack360.notification.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.teletrack360.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IncidentEventConsumer {
    
    private final NotificationService notificationService;
    
    @KafkaListener(topics = "incident-created", groupId = "notification-service")
    public void handleIncidentCreated(JsonNode event) {
        log.info("Received incident-created event: {}", event);
        
        try {
            JsonNode payload = event.get("payload");
            String title = payload.get("title").asText();
            String createdBy = payload.get("createdBy").asText();
            
            String subject = "New Incident Created: " + title;
            String body = String.format(
                    "A new incident has been created.\n\nTitle: %s\nCreated by: %s\nPlease review and take appropriate action.",
                    title, createdBy
            );
            
            notificationService.sendEmailNotification(createdBy, subject, body);
            
        } catch (Exception e) {
            log.error("Error processing incident-created event", e);
        }
    }
    
    @KafkaListener(topics = "incident-assigned", groupId = "notification-service")
    public void handleIncidentAssigned(JsonNode event) {
        log.info("Received incident-assigned event: {}", event);
        
        try {
            JsonNode payload = event.get("payload");
            String title = payload.get("title").asText();
            String assignedTo = payload.get("assignedTo").asText();
            Long incidentId = payload.get("incidentId").asLong();
            
            String subject = "Incident Assigned to You: " + title;
            String body = String.format(
                    "Incident #%d has been assigned to you.\n\nTitle: %s\nPlease review and begin work.",
                    incidentId, title
            );
            
            notificationService.sendEmailNotification(assignedTo, subject, body);
            
        } catch (Exception e) {
            log.error("Error processing incident-assigned event", e);
        }
    }
    
    @KafkaListener(topics = "incident-resolved", groupId = "notification-service")
    public void handleIncidentResolved(JsonNode event) {
        log.info("Received incident-resolved event: {}", event);
        
        try {
            JsonNode payload = event.get("payload");
            String title = payload.get("title").asText();
            String createdBy = payload.get("createdBy").asText();
            Long incidentId = payload.get("incidentId").asLong();
            
            String subject = "Incident Resolved: " + title;
            String body = String.format(
                    "Incident #%d has been resolved.\n\nTitle: %s\nThank you for your patience.",
                    incidentId, title
            );
            
            notificationService.sendEmailNotification(createdBy, subject, body);
            
        } catch (Exception e) {
            log.error("Error processing incident-resolved event", e);
        }
    }
}
