package de.thws.Adapters.scheduler;

import de.thws.Application.Ports.in.NotificationUseCase;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Logger;

@ApplicationScoped
public class
NotificationScheduler {
    private static final Logger LOGGER = Logger.getLogger(NotificationScheduler.class.getName());

    private final NotificationUseCase notificationUseCase;

    @Inject
    public NotificationScheduler(NotificationUseCase notificationUseCase) {
        this.notificationUseCase = notificationUseCase;
    }

    @Scheduled(every = "60s")
    void processNotifications() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LOGGER.fine("Running notification scheduler at " + now);
        notificationUseCase.processPendingNotifications(now);
    }
}
