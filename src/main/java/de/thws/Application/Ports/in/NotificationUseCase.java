package de.thws.Application.Ports.in;

import java.time.LocalDateTime;

public interface NotificationUseCase {
    void processPendingNotifications(LocalDateTime now);
}
