package de.thws.Adapters.persistence_out;

import de.thws.Application.Domain.DomainModels.NotificationRecord;
import de.thws.Application.Domain.DomainModels.ReminderStatus;
import de.thws.Application.Ports.out.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class NotificationRepositoryJpa implements NotificationRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<NotificationRecord> findPendingNotifications(LocalDateTime now) {
        TypedQuery<NotificationEntity> query = entityManager.createQuery(
                "SELECT n FROM NotificationEntity n " +
                        "WHERE n.status = :status AND n.reminderTime <= :now",
                NotificationEntity.class);
        query.setParameter("status", ReminderStatus.SCHEDULED);
        query.setParameter("now", now);
        List<NotificationEntity> result = query.getResultList();
        if (result == null || result.isEmpty()) {
            return List.of();
        }
        List<NotificationRecord> mapped = new java.util.ArrayList<>(result.size());
        for (NotificationEntity entity : result) {
            mapped.add(toDomain(entity));
        }
        return mapped;
    }

    @Override
    @Transactional
    public void save(NotificationRecord notification) {
        if (notification == null) {
            return;
        }
        if (notification.getNotificationId() == null) {
            NotificationEntity created = toEntity(notification);
            entityManager.persist(created);
            entityManager.flush();
            notification.setNotificationId(created.getNotificationId());
            return;
        }
        NotificationEntity entity = entityManager.find(NotificationEntity.class, notification.getNotificationId());
        if (entity == null) {
            NotificationEntity created = toEntity(notification);
            entityManager.persist(created);
            entityManager.flush();
            notification.setNotificationId(created.getNotificationId());
            return;
        }
        entity.setReminderTime(notification.getReminderTime());
        entity.setStatus(notification.getStatus());
        entity.setSentAt(notification.getSentAt());
        entity.setRetryCount(notification.getRetryCount());
        entity.setTaskId(notification.getTaskId());
        entity.setUserId(notification.getUserId());
    }

    private NotificationRecord toDomain(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }
        return new NotificationRecord(
                entity.getNotificationId(),
                entity.getReminderTime(),
                entity.getStatus(),
                entity.getSentAt(),
                entity.getRetryCount(),
                entity.getTaskId(),
                entity.getUserId());
    }

    private NotificationEntity toEntity(NotificationRecord record) {
        NotificationEntity entity = new NotificationEntity(record.getReminderTime());
        entity.setStatus(record.getStatus());
        entity.setSentAt(record.getSentAt());
        entity.setRetryCount(record.getRetryCount());
        entity.setTaskId(record.getTaskId());
        entity.setUserId(record.getUserId());
        return entity;
    }
}
