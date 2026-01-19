package de.thws.Adapters.persistence_out;

import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.TeamRole;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.TeamRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
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
    public List<User> findMembers(Long teamId) {
        TypedQuery<UserEntity> query = entityManager.createQuery(
                "SELECT m.user FROM TeamMemberEntity m WHERE m.team.teamId = :teamId",
                UserEntity.class);
        query.setParameter("teamId", teamId);
        List<User> members = new ArrayList<>(toDomainUserList(query.getResultList()));
        TeamEntity team = entityManager.find(TeamEntity.class, teamId);
        if (team != null && team.getOwner() != null) {
            User owner = toDomain(team.getOwner());
            if (owner != null && owner.getUserId() != null) {
                boolean hasOwner = members.stream()
                        .anyMatch(member -> owner.getUserId().equals(member.getUserId()));
                if (!hasOwner) {
                    members.add(owner);
                }
            }
        }
        return members;
    }

    @Override
    @Transactional
    public void addMember(Long teamId, Long userId) {
        TeamEntity team = entityManager.find(TeamEntity.class, teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team not found: " + teamId);
        }
        UserEntity user = entityManager.find(UserEntity.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        if (memberExists(teamId, userId)) {
            return;
        }
        TeamMemberEntity member = new TeamMemberEntity(team, user, TeamRole.MEMBER, LocalDateTime.now());
        entityManager.persist(member);
    }

    @Override
    @Transactional
    public void removeMember(Long teamId, Long userId) {
        entityManager.createQuery(
                "DELETE FROM TeamMemberEntity m WHERE m.team.teamId = :teamId AND m.user.userId = :userId")
                .setParameter("teamId", teamId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void save(Team team) {
        if (team.getTeamId() == null) {
            TeamEntity entity = toEntity(team);
            entityManager.persist(entity);
            entityManager.flush();
            team.setTeamId(entity.getTeamId());
            addOwnerMemberIfMissing(entity);
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

    private List<User> toDomainUserList(List<UserEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        List<User> result = new ArrayList<>(entities.size());
        for (UserEntity entity : entities) {
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

    private boolean memberExists(Long teamId, Long userId) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(m) FROM TeamMemberEntity m WHERE m.team.teamId = :teamId AND m.user.userId = :userId",
                Long.class)
                .setParameter("teamId", teamId)
                .setParameter("userId", userId)
                .getSingleResult();
        return count != null && count > 0;
    }

    private void addOwnerMemberIfMissing(TeamEntity entity) {
        if (entity == null || entity.getTeamId() == null || entity.getOwner() == null) {
            return;
        }
        Long ownerId = entity.getOwner().getUserId();
        if (ownerId == null || memberExists(entity.getTeamId(), ownerId)) {
            return;
        }
        TeamMemberEntity member = new TeamMemberEntity(
                entity,
                entity.getOwner(),
                TeamRole.OWNER,
                entity.getCreatedAt() != null ? entity.getCreatedAt() : LocalDateTime.now());
        entityManager.persist(member);
    }
}
