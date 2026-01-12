package de.thws.Application.Ports.in;

import de.thws.Application.Domain.DomainModels.Task;

import java.util.List;
import java.util.Optional;

public interface TaskUseCase {
    Task create(Task task);
    Optional<Task> findById(Long taskId);
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssignedUserId(Long userId);
    void delete(Long taskId);
}
