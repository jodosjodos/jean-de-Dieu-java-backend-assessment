package com.teletrack360.incident.event;

import com.teletrack360.common.enums.IncidentPriority;
import com.teletrack360.common.enums.IncidentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentEvent {
    
    private String eventId;
    private String eventType; // INCIDENT_CREATED, INCIDENT_UPDATED, INCIDENT_ASSIGNED, INCIDENT_RESOLVED
    private LocalDateTime timestamp;
    private IncidentEventPayload payload;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IncidentEventPayload {
        private Long incidentId;
        private String title;
        private IncidentStatus status;
        private IncidentPriority priority;
        private String assignedTo;
        private String createdBy;
    }
}
