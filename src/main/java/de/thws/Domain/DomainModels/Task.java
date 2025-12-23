package de.thws.Domain.DomainModels;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class Task {
    private Long taskId;
    private String title;
    private String description;
    private LocalDate deadline;
    private TaskPriority priority;
    private TaskStatus status;
    private Project project;
    private Set<String> tags;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User assignedUser;

    public Task(Long taskId, String title, String description, LocalDate deadline, TaskPriority priority, TaskStatus status, Project project, Set<String> tags, User assignedUser) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
        this.project = project;
        this.tags = tags; //TODO
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.assignedUser = assignedUser;
    }

    public void updateTask(String title, String description, LocalDate deadline, TaskPriority priority , Set<String> tags, User assignedUser) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.assignedUser= assignedUser;
        this.tags = tags; //TODO
        this.updatedAt= LocalDateTime.now();
    }

    public void changeStatus(TaskStatus newStatus){
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
    public void assignToUser(User user){
        this.assignedUser = user;
        this.updatedAt = LocalDateTime.now();
    }
    public void addTag(String tags){
        this.tags.add(tags);
        this.updatedAt = LocalDateTime.now();
    }
}
