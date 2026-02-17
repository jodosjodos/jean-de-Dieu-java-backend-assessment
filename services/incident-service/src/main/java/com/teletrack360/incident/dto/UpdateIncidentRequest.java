package com.teletrack360.incident.dto;

import com.teletrack360.common.enums.IncidentPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIncidentRequest {
    private String title;
    private String description;
    private IncidentPriority priority;
}
