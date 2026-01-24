package de.thws.Adapters.persistence_out.JpaRepositories;

import de.thws.Adapters.persistence_out.Entities.ProjectEntity;
import de.thws.Adapters.persistence_out.Entities.UserEntity;
import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.hydrators.ProjectHydrator;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.ProjectRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProjectRepositoryJpa implements ProjectRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Project findById(Long projectId) {
        ProjectEntity entity = entityManager.find(ProjectEntity.class, projectId);
        return toDomain(entity);
    }

    @Override
    public List<Project> findByOwnerId(Long ownerId) {
        TypedQuery<ProjectEntity> query = entityManager.createQuery(
                "SELECT p FROM ProjectEntity p WHERE p.owner.userId = :ownerId", ProjectEntity.class);
        query.setParameter("ownerId", ownerId);
        return toDomainList(query.getResultList());
    }

    @Override
    public List<Project> findByTeamId(Long teamId) {
        TypedQuery<ProjectEntity> query = entityManager.createQuery(
                "SELECT p FROM ProjectEntity p WHERE p.teamId = :teamId", ProjectEntity.class);
        query.setParameter("teamId", teamId);
        return toDomainList(query.getResultList());
    }

    @Override
    @Transactional
    public void save(Project project) {
        if (project.getProjectId() == null) {
            ProjectEntity entity = toEntity(project);
            entityManager.persist(entity);
            entityManager.flush();
            project.setProjectId(entity.getProjectId());
            return;
        }

        ProjectEntity entity = entityManager.find(ProjectEntity.class, project.getProjectId());
        if (entity == null) {
            ProjectEntity created = toEntity(project);
            entityManager.persist(created);
            entityManager.flush();
            project.setProjectId(created.getProjectId());
            return;
        }

        entity.setName(project.getName());
        entity.setDescription(project.getDescription());
        entity.setEndDate(project.getEndDate());
        entity.setOwner(toUserEntity(project.getOwner()));
        entity.setTeamId(project.getTeamId());
    }

    @Override
    @Transactional
    public void delete(Long projectId) {
        ProjectEntity entity = entityManager.find(ProjectEntity.class, projectId);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    private List<Project> toDomainList(List<ProjectEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        List<Project> result = new ArrayList<>(entities.size());
        for (ProjectEntity entity : entities) {
            result.add(toDomain(entity));
        }
        return result;
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

    private ProjectEntity toEntity(Project project) {
        User owner = project.getOwner();
        ProjectEntity entity = new ProjectEntity(project.getName(), toUserEntity(owner));
        entity.setStartDate(project.getStartDate());
        entity.setDescription(project.getDescription());
        entity.setEndDate(project.getEndDate());
        entity.setTeamId(project.getTeamId());
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

    private UserEntity toUserEntity(User user) {
        if (user == null || user.getUserId() == null) {
            return null;
        }
        return entityManager.getReference(UserEntity.class, user.getUserId());
    }
}
