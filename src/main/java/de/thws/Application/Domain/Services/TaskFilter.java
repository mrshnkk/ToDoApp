package de.thws.Application.Domain.Services;

import de.thws.Application.Domain.DomainModels.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TaskFilter {
    private final String status;
    private final String priority;
    private final Project project;
    private final Set<String> tags;
    private final LocalDate dueDate;
    private final Long teamId;

    public TaskFilter(String status, String priority, Project project, Set<String> tags) {
        this.status = status;
        this.priority = priority;
        this.project = project;
        if (tags == null) {
            this.tags = Collections.emptySet();
        } else {
            this.tags = new HashSet<>(tags);
        }
        this.dueDate = null;
        this.teamId = null;
    }

    public TaskFilter(
            String status,
            String priority,
            Project project,
            Set<String> tags,
            LocalDate dueDate,
            Long teamId) {
        this.status = status;
        this.priority = priority;
        this.project = project;
        if (tags == null) {
            this.tags = Collections.emptySet();
        } else {
            this.tags = new HashSet<>(tags);
        }
        this.dueDate = dueDate;
        this.teamId = teamId;
    }

    public TaskStatus parseStatus() {
        if (status == null) {
            return null;
        }
        String normalized = status.trim();
        if (normalized.isEmpty()) {
            throw new InvalidTaskFilterException("Status is empty");
        }
        try {
            return TaskStatus.valueOf(normalized.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidTaskFilterException("Invalid status: " + status);
        }
    }

    public TaskPriority parsePriority() {
        if (priority == null) {
            return null;
        }
        String normalized = priority.trim();
        if (normalized.isEmpty()) {
            throw new InvalidTaskFilterException("Priority is empty");
        }
        try {
            return TaskPriority.valueOf(normalized.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidTaskFilterException("Invalid priority: " + priority);
        }
    }

    public Project getProject() {
        return project;
    }

    public Set<String> normalizedTags() {
        if (tags.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> normalized = new HashSet<>();
        for (String tag : tags) {
            if (tag == null || tag.trim().isEmpty()) {
                throw new InvalidTaskFilterException("Tag is empty");
            }
            normalized.add(tag.trim().toLowerCase());
        }
        return normalized;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Long getTeamId() {
        return teamId;
    }
}
