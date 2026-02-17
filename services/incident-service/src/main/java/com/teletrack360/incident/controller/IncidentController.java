package com.teletrack360.incident.controller;

import com.teletrack360.common.dto.ApiResponse;
import com.teletrack360.common.dto.PageResponse;
import com.teletrack360.common.enums.IncidentStatus;
import com.teletrack360.incident.dto.CreateIncidentRequest;
import com.teletrack360.incident.dto.IncidentResponse;
import com.teletrack360.incident.dto.UpdateIncidentRequest;
import com.teletrack360.incident.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Incident Management", description = "Incident management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class IncidentController {
    
    private final IncidentService incidentService;
    
    @PostMapping
    @Operation(summary = "Create a new incident")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ApiResponse<IncidentResponse>> createIncident(
            @Valid @RequestBody CreateIncidentRequest request,
            Authentication authentication
    ) {
        log.info("Creating new incident: {}", request.getTitle());
        IncidentResponse response = incidentService.createIncident(request, authentication.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Incident created successfully", response));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get incident by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'SUPPORT')")
    public ResponseEntity<ApiResponse<IncidentResponse>> getIncidentById(@PathVariable Long id) {
        log.info("Getting incident by ID: {}", id);
        IncidentResponse response = incidentService.getIncidentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping
    @Operation(summary = "Get all incidents with pagination")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'SUPPORT')")
    public ResponseEntity<ApiResponse<PageResponse<IncidentResponse>>> getAllIncidents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        log.info("Getting all incidents - page: {}, size: {}", page, size);
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<IncidentResponse> incidents = incidentService.getAllIncidents(pageable);
        PageResponse<IncidentResponse> response = PageResponse.of(
                incidents.getContent(),
                incidents.getNumber(),
                incidents.getSize(),
                incidents.getTotalElements()
        );
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update incident")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ApiResponse<IncidentResponse>> updateIncident(
            @PathVariable Long id,
            @Valid @RequestBody UpdateIncidentRequest request,
            Authentication authentication
    ) {
        log.info("Updating incident: {}", id);
        IncidentResponse response = incidentService.updateIncident(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Incident updated successfully", response));
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update incident status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ApiResponse<IncidentResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam IncidentStatus status,
            Authentication authentication
    ) {
        log.info("Updating incident status: {} to {}", id, status);
        IncidentResponse response = incidentService.updateStatus(id, status, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Status updated successfully", response));
    }
    
    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign incident to user")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ApiResponse<IncidentResponse>> assignIncident(
            @PathVariable Long id,
            @RequestParam String assignedTo,
            Authentication authentication
    ) {
        log.info("Assigning incident {} to {}", id, assignedTo);
        IncidentResponse response = incidentService.assignIncident(id, assignedTo, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Incident assigned successfully", response));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete incident (soft delete)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteIncident(@PathVariable Long id) {
        log.info("Deleting incident: {}", id);
        incidentService.deleteIncident(id);
        return ResponseEntity.ok(ApiResponse.success("Incident deleted successfully", null));
    }
}
