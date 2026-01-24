package de.thws.Application.Domain.DomainModels.hydrators;

import de.thws.Application.Domain.DomainModels.Notification;
import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Domain.DomainModels.TaskPriority;
import de.thws.Application.Domain.DomainModels.TaskStatus;
import de.thws.Application.Domain.DomainModels.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

// A single factory for rebuilding Task from persisted state.
public final class TaskHydrator {
    private TaskHydrator() {
    }

    public static Task fromPersisted(
            Long taskId,
            String title,
            String description,
            LocalDate deadline,
            TaskPriority priority,
            TaskStatus status,
            Project project,
            Set<String> tags,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            User assignedUser,
            Notification notification) {
        return new Task(taskId, title, description, deadline, priority, status, project, tags, createdAt, updatedAt,
                assignedUser, notification);
    }
}
