package de.thws.Application.Ports.out;

import de.thws.Application.Domain.DomainModels.Task;

import java.util.List;

public interface TaskRepository {
        Task findById(Long taskId);
        List<Task> findByProjectId(Long projectId);
        List<Task> findByAssignedUserId(Long userId);
        void save(Task task);
        void delete(Long taskId);
    }

