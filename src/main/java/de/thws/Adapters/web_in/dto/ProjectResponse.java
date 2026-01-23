package de.thws.Adapters.web_in.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProjectResponse {
    private final Long projectId;
    private final String name;
    private final String description;
    private final LocalDateTime startDate;
    private final LocalDate endDate;
    private final Long ownerId;
    private final Long teamId;

    public ProjectResponse(
            Long projectId,
            String name,
            String description,
            LocalDateTime startDate,
            LocalDate endDate,
            Long ownerId,
            Long teamId) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ownerId = ownerId;
        this.teamId = teamId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getTeamId() {
        return teamId;
    }
}
