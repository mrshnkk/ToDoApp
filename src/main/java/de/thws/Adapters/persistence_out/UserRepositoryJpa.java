package de.thws.Adapters.persistence_out;

import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryJpa implements UserRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<User> findById(Long userId) {
        UserEntity entity = entityManager.find(UserEntity.class, userId);
        return Optional.ofNullable(toDomain(entity));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        TypedQuery<UserEntity> query = entityManager.createQuery(
                "SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class);
        query.setParameter("username", username);
        List<UserEntity> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDomain(result.get(0)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        TypedQuery<UserEntity> query = entityManager.createQuery(
                "SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class);
        query.setParameter("email", email);
        List<UserEntity> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDomain(result.get(0)));
    }

    @Override
    @Transactional
    public void save(User user) {
        if (user.getUserId() == null) {
            UserEntity entity = toEntity(user);
            entityManager.persist(entity);
            entityManager.flush();
            user.setUserId(entity.getUserId());
            return;
        }

        UserEntity entity = entityManager.find(UserEntity.class, user.getUserId());
        if (entity == null) {
            UserEntity created = toEntity(user);
            entityManager.persist(created);
            entityManager.flush();
            user.setUserId(created.getUserId());
            return;
        }

        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setCreatedAt(user.getCreatedAt());
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        UserEntity entity = entityManager.find(UserEntity.class, userId);
        if (entity != null) {
            entityManager.remove(entity);
        }
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

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getCreatedAt());
    }
}
