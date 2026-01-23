package de.thws.Application.Domain.DomainModels;

import java.time.LocalDateTime;

public class NotificationRecord {
    private Long notificationId;
    private LocalDateTime reminderTime;
    private ReminderStatus status;
    private LocalDateTime sentAt;
    private int retryCount;
    private Long taskId;
    private Long userId;

    public NotificationRecord(
            Long notificationId,
            LocalDateTime reminderTime,
            ReminderStatus status,
            LocalDateTime sentAt,
            int retryCount,
            Long taskId,
            Long userId) {
        if (reminderTime == null) {
            throw new IllegalArgumentException("Reminder time cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.notificationId = notificationId;
        this.reminderTime = reminderTime;
        this.status = status;
        this.sentAt = sentAt;
        this.retryCount = retryCount;
        this.taskId = taskId;
        this.userId = userId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        if (reminderTime == null) {
            throw new IllegalArgumentException("Reminder time cannot be null");
        }
        this.reminderTime = reminderTime;
    }

    public ReminderStatus getStatus() {
        return status;
    }

    public void setStatus(ReminderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
