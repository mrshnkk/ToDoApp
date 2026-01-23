package de.thws.Adapters.web_in.dto;

import java.time.LocalDateTime;

public class TeamResponse {
    private final Long teamId;
    private final String teamName;
    private final String description;
    private final LocalDateTime createdAt;
    private final Long ownerId;

    public TeamResponse(
            Long teamId,
            String teamName,
            String description,
            LocalDateTime createdAt,
            Long ownerId) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.description = description;
        this.createdAt = createdAt;
        this.ownerId = ownerId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getOwnerId() {
        return ownerId;
    }
}
