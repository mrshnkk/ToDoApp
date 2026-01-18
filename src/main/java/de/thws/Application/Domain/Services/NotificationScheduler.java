package de.thws.Application.Domain.Services;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Logger;

@ApplicationScoped
public class NotificationScheduler {
    private static final Logger LOGGER = Logger.getLogger(NotificationScheduler.class.getName());

    private final NotificationService notificationService;

    @Inject
    public NotificationScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(every = "60s")
    void processNotifications() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LOGGER.fine("Running notification scheduler at " + now);
        notificationService.processPendingNotifications(now);
    }
}
