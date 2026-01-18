package de.thws.Application.Ports.out;

import de.thws.Adapters.persistence_out.NotificationEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository {
    List<NotificationEntity> findPendingNotifications(LocalDateTime now);
    void save(NotificationEntity notification);
}
