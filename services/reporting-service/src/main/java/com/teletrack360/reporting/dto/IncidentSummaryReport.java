package com.teletrack360.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentSummaryReport {
    private Long totalIncidents;
    private Long openIncidents;
    private Long inProgressIncidents;
    private Long resolvedIncidents;
    private Long closedIncidents;
    private Double averageResolutionTimeHours;
    private String reportGeneratedAt;
}
