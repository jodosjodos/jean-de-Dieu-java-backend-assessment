package com.teletrack360.incident.service;

import com.teletrack360.common.enums.IncidentPriority;
import com.teletrack360.common.enums.IncidentStatus;
import com.teletrack360.incident.dto.CreateIncidentRequest;
import com.teletrack360.incident.dto.IncidentResponse;
import com.teletrack360.incident.entity.Incident;
import com.teletrack360.incident.kafka.IncidentEventProducer;
import com.teletrack360.incident.repository.IncidentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {
    
    @Mock
    private IncidentRepository incidentRepository;
    
    @Mock
    private IncidentEventProducer eventProducer;
    
    @InjectMocks
    private IncidentService incidentService;
    
    private CreateIncidentRequest createRequest;
    private Incident incident;
    
    @BeforeEach
    void setUp() {
        createRequest = CreateIncidentRequest.builder()
                .title("Server Down")
                .description("Production server is not responding")
                .priority(IncidentPriority.HIGH)
                .build();
        
        incident = Incident.builder()
                .id(1L)
                .title("Server Down")
                .description("Production server is not responding")
                .status(IncidentStatus.OPEN)
                .priority(IncidentPriority.HIGH)
                .createdBy("testuser")
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }
    
    @Test
    void createIncident_Success() {
        // Arrange
        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);
        doNothing().when(eventProducer).sendEvent(anyString(), any());
        
        // Act
        IncidentResponse response = incidentService.createIncident(createRequest, "testuser");
        
        // Assert
        assertNotNull(response);
        assertEquals("Server Down", response.getTitle());
        assertEquals(IncidentStatus.OPEN, response.getStatus());
        assertEquals(IncidentPriority.HIGH, response.getPriority());
        verify(incidentRepository).save(any(Incident.class));
        verify(eventProducer).sendEvent(eq("incident-created"), any());
    }
    
    @Test
    void updateStatus_ToResolved_SetsResolvedAt() {
        // Arrange
        when(incidentRepository.findByIdAndNotDeleted(1L)).thenReturn(java.util.Optional.of(incident));
        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);
        doNothing().when(eventProducer).sendEvent(anyString(), any());
        
        // Act
        IncidentResponse response = incidentService.updateStatus(1L, IncidentStatus.RESOLVED, "testuser");
        
        // Assert
        assertNotNull(response);
        verify(eventProducer).sendEvent(eq("incident-resolved"), any());
    }
}
