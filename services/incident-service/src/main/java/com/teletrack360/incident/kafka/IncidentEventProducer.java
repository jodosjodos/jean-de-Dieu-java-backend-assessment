package com.teletrack360.incident.kafka;

import com.teletrack360.incident.event.IncidentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentEventProducer {
    
    private final KafkaTemplate<String, IncidentEvent> kafkaTemplate;
    
    public void sendEvent(String topic, IncidentEvent event) {
        log.info("Sending event to topic {}: {}", topic, event.getEventType());
        
        CompletableFuture<SendResult<String, IncidentEvent>> future = 
                kafkaTemplate.send(topic, event.getEventId(), event);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Event sent successfully: {} to partition: {}", 
                        event.getEventType(), result.getRecordMetadata().partition());
            } else {
                log.error("Failed to send event: {}", event.getEventType(), ex);
            }
        });
    }
}
