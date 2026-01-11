package de.thws.Application.Domain.Services;

import de.thws.Application.Domain.DomainModels.*;
import de.thws.Application.Ports.out.*;
import java.time.LocalDate;

public class TaskService {
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    public TaskService(TaskRepository taskRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    // UC-02: Create new task
    public Task createTask(
            Long userId,
            String title,
            String description,
            LocalDate deadline,
            TaskPriority priority,
            TaskStatus status)
    {
        // 5. Validation
        if (deadline != null && deadline.isBefore(LocalDate.now())) {
            throw new InvalidTaskDeadlineException("Deadline must not be in the past");
        }
        // 6. Create Task
        Task task = new Task(title);
        task.changePriority(priority);
        task.changeStatus(status);
        if (description != null) {
            task.changeDescription(description);
        }
        if (deadline != null) {
            task.setDeadline(deadline);
        }
        if (userId != null) {
            User assignedUser = userRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
            task.assignToUser(assignedUser);
        }
        // 7. Persist
        taskRepo.save(task);

        return task;
    }
}
