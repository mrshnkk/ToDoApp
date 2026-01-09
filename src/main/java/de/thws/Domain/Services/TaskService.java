package de.thws.Domain.Services;

import de.thws.Domain.DomainModels.Task;
import de.thws.Domain.DomainModels.TaskPriority;
import de.thws.Domain.DomainModels.TaskStatus;
import de.thws.Domain.DomainModels.User;
import de.thws.Ports.output.TaskRepository;
import de.thws.Ports.output.UserRepository;

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
            TaskStatus status
    ) {

        // 5. Validation
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty");
        }

        if (priority == null) {
            throw new IllegalArgumentException("Priority must be set");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status must be set");
        }

        if (deadline != null && deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline must not be in the past");
        }

        // 6. Create Task
        Task task = new Task(title);
        task.changehangePriority(priority);
        task.changeStatus(status);
        if (description != null) {
            task.changeDescription(description);
        }
        if (deadline != null) {
            task.setDeadline(deadline);
        }
        if (userId != null) {
            User assignedUser = userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
            task.assignToUser(assignedUser);
        }

        // 7. Persist
        taskRepo.save(task);

        return task;
    }
}
