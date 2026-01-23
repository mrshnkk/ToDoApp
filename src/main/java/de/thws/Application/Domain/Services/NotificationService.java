package de.thws.Application.Domain.Services;

import de.thws.Application.Domain.DomainModels.NotificationRecord;
import de.thws.Application.Domain.DomainModels.ReminderStatus;
import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Domain.DomainModels.TaskStatus;
import de.thws.Application.Ports.out.NotificationRepository;
import de.thws.Application.Ports.out.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class NotificationService {
    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());
    private static final int MAX_RETRIES = 3;

    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;

    @Inject
    public NotificationService(TaskRepository taskRepository, NotificationRepository notificationRepository) {
        this.taskRepository = taskRepository;
        this.notificationRepository = notificationRepository;
    }

    public Task scheduleReminder(Long taskId, LocalDateTime reminderTime, LocalDateTime now) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task id is required");
        }
        Task task = taskRepository.findById(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task not found: " + taskId);
        }

        task.scheduleReminder(reminderTime, now);
        taskRepository.save(task);
        return task;
    }

    public void processPendingNotifications(LocalDateTime now) {
        if (now == null) {
            throw new IllegalArgumentException("Current time is required");
        }
        List<NotificationRecord> pending = notificationRepository.findPendingNotifications(now);
        if (pending.isEmpty()) {
            return;
        }

        for (NotificationRecord notification : pending) {
            Long taskId = notification.getTaskId();
            if (taskId == null) {
                notification.setStatus(ReminderStatus.FAILED);
                notificationRepository.save(notification);
                LOGGER.warning("Notification has no taskId. Marked as FAILED.");
                continue;
            }

            Task task = taskRepository.findById(taskId);
            if (task == null) {
                notification.setStatus(ReminderStatus.CANCELLED);
                notificationRepository.save(notification);
                LOGGER.info("Task missing for notification " + notification.getNotificationId()
                        + ". Marked as CANCELLED.");
                continue;
            }

            TaskStatus status = task.getStatus();
            if (status == TaskStatus.DONE || status == TaskStatus.CANCELLED) {
                notification.setStatus(ReminderStatus.CANCELLED);
                notificationRepository.save(notification);
                LOGGER.info("Task " + taskId + " is " + status + ". Notification cancelled.");
                continue;
            }

            if (sendNotification(notification)) {
                notification.setStatus(ReminderStatus.SENT);
                notification.setSentAt(now);
                notificationRepository.save(notification);
                LOGGER.info("Notification " + notification.getNotificationId() + " sent.");
                continue;
            }

            notification.incrementRetryCount();
            if (notification.getRetryCount() >= MAX_RETRIES) {
                notification.setStatus(ReminderStatus.FAILED);
                LOGGER.warning("Notification " + notification.getNotificationId()
                        + " failed after " + notification.getRetryCount() + " attempts.");
            } else {
                LOGGER.warning("Notification " + notification.getNotificationId()
                        + " delivery failed. Retry " + notification.getRetryCount() + "/" + MAX_RETRIES + ".");
            }
            notificationRepository.save(notification);
        }
    }

    public boolean sendNotification(NotificationRecord notification) {
        try {
            ZoneId userZone = ZoneOffset.UTC;
            LocalDateTime reminderTime = notification.getReminderTime();
            ZonedDateTime utcReminder = reminderTime.atZone(ZoneOffset.UTC);
            ZonedDateTime localReminder = utcReminder.withZoneSameInstant(userZone);

            LOGGER.info("Send notification " + notification.getNotificationId()
                    + " to user " + notification.getUserId()
                    + " for task " + notification.getTaskId()
                    + " at " + localReminder.toLocalDateTime() + " (" + userZone + ").");
            return true;
        } catch (RuntimeException ex) {
            LOGGER.warning("Notification " + notification.getNotificationId()
                    + " delivery error: " + ex.getMessage());
            return false;
        }
    }
}
