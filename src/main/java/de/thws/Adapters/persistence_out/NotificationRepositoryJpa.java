package de.thws.Adapters.persistence_out;

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
    public List<NotificationEntity> findPendingNotifications(LocalDateTime now) {
        TypedQuery<NotificationEntity> query = entityManager.createQuery(
                "SELECT n FROM NotificationEntity n " +
                        "WHERE n.status = :status AND n.reminderTime <= :now",
                NotificationEntity.class);
        query.setParameter("status", ReminderStatus.SCHEDULED);
        query.setParameter("now", now);
        List<NotificationEntity> result = query.getResultList();
        return result == null ? List.of() : result;
    }

    @Override
    @Transactional
    public void save(NotificationEntity notification) {
        if (notification == null) {
            return;
        }
        if (notification.getNotificationId() == null) {
            entityManager.persist(notification);
            return;
        }
        entityManager.merge(notification);
    }
}
