package com.teletrack360.incident.service;

import com.teletrack360.common.enums.IncidentStatus;
import com.teletrack360.common.exception.ResourceNotFoundException;
import com.teletrack360.common.util.CorrelationIdGenerator;
import com.teletrack360.incident.dto.CreateIncidentRequest;
import com.teletrack360.incident.dto.IncidentResponse;
import com.teletrack360.incident.dto.UpdateIncidentRequest;
import com.teletrack360.incident.entity.Incident;
import com.teletrack360.incident.event.IncidentEvent;
import com.teletrack360.incident.kafka.IncidentEventProducer;
import com.teletrack360.incident.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService {
    
    private final IncidentRepository incidentRepository;
    private final IncidentEventProducer eventProducer;
    
    @Transactional
    public IncidentResponse createIncident(CreateIncidentRequest request, String username) {
        Incident incident = Incident.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(IncidentStatus.OPEN)
                .priority(request.getPriority())
                .createdBy(username)
                .build();
        
        Incident saved = incidentRepository.save(incident);
        log.info("Incident created with ID: {}", saved.getId());
        
        // Publish event
        IncidentEvent event = buildEvent("INCIDENT_CREATED", saved);
        eventProducer.sendEvent("incident-created", event);
        
        return mapToResponse(saved);
    }
    
    @Transactional(readOnly = true)
    public IncidentResponse getIncidentById(Long id) {
        Incident incident = incidentRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident", id));
        return mapToResponse(incident);
    }
    
    @Transactional(readOnly = true)
    public Page<IncidentResponse> getAllIncidents(Pageable pageable) {
        return incidentRepository.findAllActive(pageable)
                .map(this::mapToResponse);
    }
    
    @Transactional
    public IncidentResponse updateIncident(Long id, UpdateIncidentRequest request, String username) {
        Incident incident = incidentRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident", id));
        
        if (request.getTitle() != null) {
            incident.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            incident.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            incident.setPriority(request.getPriority());
        }
        
        incident.setUpdatedBy(username);
        Incident updated = incidentRepository.save(incident);
        
        // Publish event
        IncidentEvent event = buildEvent("INCIDENT_UPDATED", updated);
        eventProducer.sendEvent("incident-updated", event);
        
        return mapToResponse(updated);
    }
    
    @Transactional
    public IncidentResponse updateStatus(Long id, IncidentStatus newStatus, String username) {
        Incident incident = incidentRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident", id));
        
        incident.setStatus(newStatus);
        incident.setUpdatedBy(username);
        
        if (newStatus == IncidentStatus.RESOLVED) {
            incident.setResolvedAt(LocalDateTime.now());
            IncidentEvent event = buildEvent("INCIDENT_RESOLVED", incident);
            eventProducer.sendEvent("incident-resolved", event);
        } else if (newStatus == IncidentStatus.CLOSED) {
            incident.setClosedAt(LocalDateTime.now());
        }
        
        Incident updated = incidentRepository.save(incident);
        return mapToResponse(updated);
    }
    
    @Transactional
    public IncidentResponse assignIncident(Long id, String assignedTo, String username) {
        Incident incident = incidentRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident", id));
        
        incident.setAssignedTo(assignedTo);
        incident.setUpdatedBy(username);
        if (incident.getStatus() == IncidentStatus.OPEN) {
            incident.setStatus(IncidentStatus.IN_PROGRESS);
        }
        
        Incident updated = incidentRepository.save(incident);
        
        // Publish event
        IncidentEvent event = buildEvent("INCIDENT_ASSIGNED", updated);
        eventProducer.sendEvent("incident-assigned", event);
        
        return mapToResponse(updated);
    }
    
    @Transactional
    public void deleteIncident(Long id) {
        Incident incident = incidentRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident", id));
        incident.setDeleted(true);
        incidentRepository.save(incident);
    }
    
    private IncidentEvent buildEvent(String eventType, Incident incident) {
        return IncidentEvent.builder()
                .eventId(CorrelationIdGenerator.generate())
                .eventType(eventType)
                .timestamp(LocalDateTime.now())
                .payload(IncidentEvent.IncidentEventPayload.builder()
                        .incidentId(incident.getId())
                        .title(incident.getTitle())
                        .status(incident.getStatus())
                        .priority(incident.getPriority())
                        .assignedTo(incident.getAssignedTo())
                        .createdBy(incident.getCreatedBy())
                        .build())
                .build();
    }
    
    private IncidentResponse mapToResponse(Incident incident) {
        return IncidentResponse.builder()
                .id(incident.getId())
                .title(incident.getTitle())
                .description(incident.getDescription())
                .status(incident.getStatus())
                .priority(incident.getPriority())
                .assignedTo(incident.getAssignedTo())
                .createdBy(incident.getCreatedBy())
                .createdAt(incident.getCreatedAt())
                .updatedAt(incident.getUpdatedAt())
                .resolvedAt(incident.getResolvedAt())
                .closedAt(incident.getClosedAt())
                .build();
    }
}
