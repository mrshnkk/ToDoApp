package de.thws.Adapters.persistence_out.JpaRepositories;

import de.thws.Adapters.persistence_out.Entities.ProjectEntity;
import de.thws.Adapters.persistence_out.Entities.TaskEntity;
import de.thws.Adapters.persistence_out.Entities.UserEntity;
import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.hydrators.ProjectHydrator;
import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Domain.DomainModels.hydrators.TaskHydrator;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class TaskRepositoryJpa implements TaskRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Task findById(Long taskId) {
        TaskEntity entity = entityManager.find(TaskEntity.class, taskId);
        return toDomain(entity);
    }

    @Override
    public List<Task> findByProjectId(Long projectId) {
        TypedQuery<TaskEntity> query = entityManager.createQuery(
                "SELECT t FROM TaskEntity t WHERE t.project.projectId = :projectId", TaskEntity.class);
        query.setParameter("projectId", projectId);
        return toDomainList(query.getResultList());
    }

    @Override
    public List<Task> findByAssignedUserId(Long userId) {
        TypedQuery<TaskEntity> query = entityManager.createQuery(
                "SELECT t FROM TaskEntity t WHERE t.assignedUser.userId = :userId", TaskEntity.class);
        query.setParameter("userId", userId);
        return toDomainList(query.getResultList());
    }

    @Override
    @Transactional
    public void save(Task task) {
        if (task.getTaskId() == null) {
            TaskEntity entity = toEntity(task);
            entityManager.persist(entity);
            entityManager.flush();
            task.setTaskId(entity.getTaskId());
            return;
        }

        TaskEntity entity = entityManager.find(TaskEntity.class, task.getTaskId());
        if (entity == null) {
            TaskEntity created = toEntity(task);
            entityManager.persist(created);
            entityManager.flush();
            task.setTaskId(created.getTaskId());
            return;
        }

        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setDeadline(task.getDeadline());
        entity.setPriority(task.getPriority());
        entity.setStatus(task.getStatus());
        entity.setUpdatedAt(task.getUpdatedAt());
        entity.setAssignedUser(toUserEntity(task.getAssignedUser()));
        entity.setProject(toProjectEntity(task.getProject()));
        entity.getTags().clear();
        Set<String> tags = task.getTags();
        if (tags != null) {
            entity.getTags().addAll(tags);
        }
    }

    @Override
    @Transactional
    public void delete(Long taskId) {
        TaskEntity entity = entityManager.find(TaskEntity.class, taskId);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    private List<Task> toDomainList(List<TaskEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        List<Task> result = new ArrayList<>(entities.size());
        for (TaskEntity entity : entities) {
            result.add(toDomain(entity));
        }
        return result;
    }

    private Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }
        return TaskHydrator.fromPersisted(
                entity.getTaskId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDeadline(),
                entity.getPriority(),
                entity.getStatus(),
                toDomain(entity.getProject()),
                entity.getTags(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                toDomain(entity.getAssignedUser()),
                null);
    }

    private TaskEntity toEntity(Task task) {
        TaskEntity entity = new TaskEntity(
                task.getTitle(),
                task.getPriority(),
                task.getStatus(),
                task.getCreatedAt());
        entity.setDescription(task.getDescription());
        entity.setDeadline(task.getDeadline());
        entity.setUpdatedAt(task.getUpdatedAt());
        entity.setAssignedUser(toUserEntity(task.getAssignedUser()));
        entity.setProject(toProjectEntity(task.getProject()));
        if (task.getTags() != null) {
            entity.getTags().addAll(task.getTags());
        }
        return entity;
    }

    private User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.fromPersisted(
                entity.getUserId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getCreatedAt());
    }

    private Project toDomain(ProjectEntity entity) {
        if (entity == null) {
            return null;
        }
        User owner = toDomain(entity.getOwner());
        return ProjectHydrator.fromPersisted(
                entity.getProjectId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                owner,
                entity.getTeamId());
    }

    private UserEntity toUserEntity(User user) {
        if (user == null || user.getUserId() == null) {
            return null;
        }
        return entityManager.getReference(UserEntity.class, user.getUserId());
    }

    private ProjectEntity toProjectEntity(Project project) {
        if (project == null || project.getProjectId() == null) {
            return null;
        }
        return entityManager.getReference(ProjectEntity.class, project.getProjectId());
    }
}
