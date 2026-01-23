package de.thws.Application.Ports.out;

import de.thws.Application.Domain.DomainModels.NotificationRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository {
    List<NotificationRecord> findPendingNotifications(LocalDateTime now);
    void save(NotificationRecord notification);
}
