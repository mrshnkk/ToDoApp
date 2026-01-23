package de.thws.Adapters.web_in.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class TaskResponse {
    private final Long taskId;
    private final String title;
    private final String description;
    private final LocalDate deadline;
    private final String priority;
    private final String status;
    private final Set<String> tags;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Long projectId;
    private final Long assignedUserId;

    public TaskResponse(
            Long taskId,
            String title,
            String description,
            LocalDate deadline,
            String priority,
            String status,
            Set<String> tags,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long projectId,
            Long assignedUserId) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.projectId = projectId;
        this.assignedUserId = assignedUserId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public Set<String> getTags() {
        return tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }
}
