package de.thws.Adapters.persistence_out;
import de.thws.Application.Domain.DomainModels.ReminderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(name="reminder_time", nullable = false)
    private LocalDateTime reminderTime;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private ReminderStatus status;

    @Column(name="sent_at")
    private LocalDateTime sentAt;

    @Column(name="retry_count", nullable = false)
    private int retryCount = 0;

    @Column(name="task_id")
    private Long taskId;

    @Column(name="user_id")
    private Long userId;

    public NotificationEntity(){}

    public NotificationEntity(LocalDateTime reminderTime){
        if (reminderTime == null) {
            throw new IllegalArgumentException("Reminder time cannot be null");
        }

        this.reminderTime = reminderTime;
        this.status = ReminderStatus.SCHEDULED; // -> default
        this.retryCount = 0;
    }


    public Long getNotificationId() {
        return notificationId;
    }



    public LocalDateTime getReminderTime() {
        return reminderTime;

    }

    public ReminderStatus getStatus() {
        return status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setStatus(ReminderStatus status) {
        this.status = status;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

