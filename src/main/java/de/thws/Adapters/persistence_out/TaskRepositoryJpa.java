package de.thws.Adapters.persistence_out;

import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.Task;
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
        Task task = new Task(entity.getTitle());
        task.setTaskId(entity.getTaskId());
        if (entity.getDescription() != null) {
            task.changeDescription(entity.getDescription());
        }
        if (entity.getDeadline() != null) {
            task.setDeadline(entity.getDeadline());
        }
        if (entity.getPriority() != null) {
            task.changePriority(entity.getPriority());
        }
        if (entity.getStatus() != null) {
            task.changeStatus(entity.getStatus());
        }
        if (entity.getAssignedUser() != null) {
            task.assignToUser(toDomain(entity.getAssignedUser()));
        }
        if (entity.getProject() != null) {
            task.assignToProject(toDomain(entity.getProject()));
        }
        if (entity.getTags() != null) {
            for (String tag : entity.getTags()) {
                task.addTag(tag);
            }
        }
        return task;
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
        Project project = new Project(entity.getName(), owner);
        project.setProjectId(entity.getProjectId());
        project.setTeamId(entity.getTeamId());
        project.updateDescription(entity.getDescription());
        return project;
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
