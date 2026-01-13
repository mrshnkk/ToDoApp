package de.thws.Application.Domain.Services;

import de.thws.Application.Domain.DomainModels.*;
import de.thws.Application.Ports.out.TaskRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TaskQueryService {
    private final TaskRepository taskRepository;

    public TaskQueryService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksForUser(Long userId, TaskFilter filter) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }

        List<Task> tasks = taskRepository.findByAssignedUserId(userId);
        List<Task> safeTasks = tasks == null ? Collections.emptyList() : tasks;

        if (filter == null) {
            return new ArrayList<>(safeTasks);
        }

        TaskStatus status = filter.parseStatus();
        TaskPriority priority = filter.parsePriority();
        Project project = filter.getProject();
        Set<String> tags = filter.normalizedTags();
        LocalDate dueDate = filter.getDueDate();
        Long teamId = filter.getTeamId();

        if (safeTasks.isEmpty()) {
            return Collections.emptyList();
        }

        List<Task> result = new ArrayList<>();
        for (Task task : safeTasks) {
            if (status != null && task.getStatus() != status) {
                continue;
            }
            TaskPriority taskPriority = TaskFieldAccessor.getPriority(task);
            if (priority != null && taskPriority != priority) {
                continue;
            }
            Project taskProject = null;
            if (project != null || teamId != null) {
                taskProject = TaskFieldAccessor.getProject(task);
            }
            if (project != null) {
                if (taskProject == null || !taskProject.equals(project)) {
                    continue;
                }
            }
            if (dueDate != null) {
                LocalDate taskDueDate = TaskFieldAccessor.getDueDate(task);
                if (taskDueDate == null || !taskDueDate.equals(dueDate)) {
                    continue;
                }
            }
            if (teamId != null) {
                if (taskProject == null) {
                    continue;
                }
                Long taskTeamId = taskProject.getTeamId();
                if (taskTeamId == null || !taskTeamId.equals(teamId)) {
                    continue;
                }
            }
            if (!tags.isEmpty()) {
                Set<String> taskTags = TaskFieldAccessor.getTags(task);
                if (taskTags.isEmpty() || !taskTags.containsAll(tags)) {
                    continue;
                }
            }
            result.add(task);
        }
        return result;
    }
}
