package de.thws.Domain.Services;

import de.thws.Domain.DomainModels.Project;
import de.thws.Domain.DomainModels.Task;
import de.thws.Domain.DomainModels.TaskPriority;
import de.thws.Domain.DomainModels.TaskStatus;
import de.thws.Ports.output.TaskRepository;

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
            if (project != null) {
                Project taskProject = TaskFieldAccessor.getProject(task);
                if (taskProject == null || !taskProject.equals(project)) {
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
