package de.thws.Adapters.persistence_out;

import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.TeamRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TeamRepositoryJpa implements TeamRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Team findById(Long teamId) {
        TeamEntity entity = entityManager.find(TeamEntity.class, teamId);
        return toDomain(entity);
    }

    @Override
    public List<Team> findByOwnerId(Long ownerId) {
        TypedQuery<TeamEntity> query = entityManager.createQuery(
                "SELECT t FROM TeamEntity t WHERE t.owner.userId = :ownerId", TeamEntity.class);
        query.setParameter("ownerId", ownerId);
        return toDomainList(query.getResultList());
    }

    @Override
    public List<Team> findByUserId(Long userId) {
        TypedQuery<TeamEntity> query = entityManager.createQuery(
                "SELECT DISTINCT t FROM TeamEntity t JOIN t.teamMembers m WHERE m.user.userId = :userId",
                TeamEntity.class);
        query.setParameter("userId", userId);
        return toDomainList(query.getResultList());
    }

    @Override
    @Transactional
    public void save(Team team) {
        if (team.getTeamId() == null) {
            TeamEntity entity = toEntity(team);
            entityManager.persist(entity);
            entityManager.flush();
            team.setTeamId(entity.getTeamId());
            return;
        }

        TeamEntity entity = entityManager.find(TeamEntity.class, team.getTeamId());
        if (entity == null) {
            TeamEntity created = toEntity(team);
            entityManager.persist(created);
            entityManager.flush();
            team.setTeamId(created.getTeamId());
            return;
        }

        entity.setTeamName(team.getTeamName());
        entity.setDescription(team.getDescription());
        entity.setCreatedAt(team.getCreatedAt());
        entity.setOwner(toUserEntity(team.getOwner()));
    }

    @Override
    @Transactional
    public void delete(Long teamId) {
        TeamEntity entity = entityManager.find(TeamEntity.class, teamId);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    private List<Team> toDomainList(List<TeamEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        List<Team> result = new ArrayList<>(entities.size());
        for (TeamEntity entity : entities) {
            result.add(toDomain(entity));
        }
        return result;
    }

    private Team toDomain(TeamEntity entity) {
        if (entity == null) {
            return null;
        }
        User owner = toDomain(entity.getOwner());
        Team team = new Team(entity.getTeamName(), entity.getDescription(), owner);
        team.setTeamId(entity.getTeamId());
        return team;
    }

    private TeamEntity toEntity(Team team) {
        TeamEntity entity = new TeamEntity(
                team.getTeamName(),
                toUserEntity(team.getOwner()),
                team.getCreatedAt());
        entity.setDescription(team.getDescription());
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
