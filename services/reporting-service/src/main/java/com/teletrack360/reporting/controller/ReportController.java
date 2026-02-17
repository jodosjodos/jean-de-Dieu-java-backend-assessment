package com.teletrack360.reporting.controller;

import com.teletrack360.common.dto.ApiResponse;
import com.teletrack360.reporting.dto.IncidentSummaryReport;
import com.teletrack360.reporting.service.ReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reporting", description = "Incident reporting and analytics endpoints")
public class ReportController {
    
    private final ReportingService reportingService;
    
    @GetMapping("/incidents/summary")
    @Operation(summary = "Get overall incident statistics")
    public ResponseEntity<ApiResponse<IncidentSummaryReport>> getIncidentSummary() {
        log.info("Fetching incident summary report");
        IncidentSummaryReport report = reportingService.getIncidentSummary();
        return ResponseEntity.ok(ApiResponse.success(report));
    }
}
