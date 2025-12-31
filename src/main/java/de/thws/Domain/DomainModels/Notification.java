package de.thws.Domain.DomainModels;

import java.time.LocalDateTime;

public class Notification {

    private LocalDateTime reminderTime;
    private ReminderStatus status;


    public Notification(LocalDateTime reminderTime, LocalDateTime now) {
        if (reminderTime == null) {
            throw new IllegalArgumentException("Reminder time cannot be null");
        }
        if (reminderTime.isBefore(now)) {
            throw new IllegalArgumentException("Reminder time must be in the future");
        }
        this.reminderTime = reminderTime;
        this.status = ReminderStatus.SCHEDULED;

    }

    public static Notification schedule(LocalDateTime reminderTime, LocalDateTime now) {
        return new Notification(reminderTime, now);

    }

    public void markAsSent() {
        if (status != ReminderStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled reminders can be sent");

        }
        this.status = ReminderStatus.SENT;

    }


    public boolean isDue(LocalDateTime now) {
        return status == ReminderStatus.SCHEDULED
                && !now.isBefore(reminderTime);

    }

    public void cancel() {
        if (status == ReminderStatus.SENT) {
            throw new IllegalStateException("Cannot cancel already sent reminder");
        }
        this.status = ReminderStatus.CANCELLED;
    }

    public ReminderStatus getStatus() {
        return status;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }
}
