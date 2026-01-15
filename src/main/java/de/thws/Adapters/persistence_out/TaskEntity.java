package de.thws.Adapters.persistence_out;
import de.thws.Application.Domain.DomainModels.TaskPriority;
import de.thws.Application.Domain.DomainModels.TaskStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;



@Entity
@Table(name="tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long taskId;


    @Column(name="title", nullable = false )
    private String title;

    @Column(name="description", length = 1000)
    private String description;

    private LocalDate deadline;



    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;


    @Enumerated(EnumType.STRING)
    @Column(name="priority", nullable = false)
    private TaskPriority priority;


    @ManyToOne         //if the task is personal -> null, command task -> FK
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @ElementCollection
    @CollectionTable(name="task_tags",
            joinColumns = @JoinColumn(name="task_id"))
    @Column(name="tag")
    private Set<String> tags = new HashSet<>();

    @Column(name="createdAt", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private NotificationEntity notification;

    private LocalDateTime updatedAt;



    @ManyToOne
    @JoinColumn(name="assigned_User")
    private UserEntity assignedUser;



    public TaskEntity(){}



    public TaskEntity(String title, TaskPriority priority, TaskStatus status, LocalDateTime createdAt){
        this.title=title;
        this.priority=priority;
        this.status=status;
        this.createdAt=createdAt;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setAssignedUser(UserEntity user) {
        this.assignedUser = user;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    public Long getTaskId() {
        return taskId;
    }


    public String getTitle() {
        return title;
    }


    public TaskPriority getPriority() {
        return priority;
    }


    public TaskStatus getStatus() {
        return status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public Set<String> getTags() {
        return tags;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UserEntity getAssignedUser() {
        return assignedUser;
    }


    public void setNotification(NotificationEntity notification) {
        this.notification = notification;
    }


    public void removeNotification() {
        this.notification = null;

    }
}
