package de.thws.Adapters.database_out;
import de.thws.Domain.DomainModels.ReminderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;



@Entity
@Table(name="Notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(name="reminder_time", nullable = false)
    private LocalDateTime reminderTime;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private ReminderStatus status;

    public NotificationEntity(){}

    public NotificationEntity(LocalDateTime reminderTime){

        if (reminderTime == null) {

            throw new IllegalArgumentException("Reminder time cannot be null");

        }

        this.reminderTime = reminderTime;

        this.status = ReminderStatus.SCHEDULED; // -> default

    }


    public Long getNotificationId() {
        return notificationId;
    }



    public LocalDateTime getReminderTime() {
        return reminderTime;

    }

    public ReminderStatus getStatus() {
        return status;
    }
}

