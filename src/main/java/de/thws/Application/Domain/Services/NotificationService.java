package de.thws.Application.Domain.Services;

import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Ports.out.TaskRepository;
import java.time.LocalDateTime;

public class NotificationService {
    private final TaskRepository taskRepository;

    public NotificationService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
}
