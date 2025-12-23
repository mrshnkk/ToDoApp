package de.thws.Domain.DomainModels;

import java.time.LocalDateTime;
public class Notification {
    Long reminderId;
    Long taskId;
    LocalDateTime reminderTime;
    Boolean sent;

    public Notification(Long reminderId, Long taskId, LocalDateTime reminderTime, Boolean sent) {
        this.reminderId = reminderId;
        this.taskId = taskId;
        this.reminderTime = reminderTime;
        this.sent = sent;
    }
    public Long getReminderId() {
        return reminderId;
    }
    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }
    public Long getTaskId() {
        return taskId;
    }
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    public LocalDateTime getReminderTime() {
        return reminderTime;
    }
    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }
    public Boolean getSent() {
        return sent;
    }
    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public void scheduleReminder(LocalDateTime reminderTime) {
        if (reminderTime == null) {
            throw new IllegalArgumentException("Reminder time cannot be null");
        }
        this.reminderTime = reminderTime;
        this.sent = false;
    }
    public void sendReminder() {
        if (reminderTime== null){
            throw new IllegalStateException("Reminder time cannot be null");
        }
        this.sent = true;
    }
    public void cancelReminder() {
        this.reminderTime = null;
        this.sent = null;
    }
}
