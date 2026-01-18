package de.thws.Application.Services;

import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Ports.in.TaskUseCase;
import de.thws.Application.Ports.out.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskUseCaseImpl implements TaskUseCase {
    private final TaskRepository taskRepository;

    @Inject
    public TaskUseCaseImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Task task) {
        taskRepository.save(task);
        return task;
    }

    @Override
    public Optional<Task> findById(Long taskId) {
        return Optional.ofNullable(taskRepository.findById(taskId));
    }

    @Override
    public List<Task> findByProjectId(Long projectId) {
        List<Task> result = taskRepository.findByProjectId(projectId);
        return result == null ? List.of() : result;
    }

    @Override
    public List<Task> findByAssignedUserId(Long userId) {
        List<Task> result = taskRepository.findByAssignedUserId(userId);
        return result == null ? List.of() : result;
    }

    @Override
    public void delete(Long taskId) {
        taskRepository.delete(taskId);
    }
}
