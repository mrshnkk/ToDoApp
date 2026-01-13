package de.thws.Application.Domain.Services;

import de.thws.Application.Domain.DomainModels.Notification;
import de.thws.Application.Domain.DomainModels.ReminderStatus;
import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Ports.out.TaskRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationServiceTest {

    @Test
    void scheduleReminderSucceeds() {
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        NotificationService service = new NotificationService(repo);

        Task task = new Task("Task A");
        repo.addTask(1L, task);

        LocalDateTime now = LocalDateTime.of(2024, 6, 10, 10, 0);
        LocalDateTime reminder = now.plusHours(1);

        Task result = service.scheduleReminder(1L, reminder, now);

        assertSame(task, result);
        assertSame(task, repo.getLastSaved());

        Notification notification = getNotification(task);
        assertNotNull(notification);
        assertEquals(reminder, notification.getReminderTime());
        assertEquals(ReminderStatus.SCHEDULED, notification.getStatus());
    }

    @Test
    void scheduleReminderThrowsWhenTaskMissing() {
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        NotificationService service = new NotificationService(repo);

        LocalDateTime now = LocalDateTime.of(2024, 6, 10, 10, 0);

        assertThrows(TaskNotFoundException.class,
                () -> service.scheduleReminder(99L, now.plusHours(1), now));
    }

    @Test
    void scheduleReminderThrowsWhenTaskIdNull() {
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        NotificationService service = new NotificationService(repo);

        LocalDateTime now = LocalDateTime.of(2024, 6, 10, 10, 0);

        assertThrows(IllegalArgumentException.class,
                () -> service.scheduleReminder(null, now.plusHours(1), now));
    }

    @Test
    void scheduleReminderThrowsWhenTimeInPast() {
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        NotificationService service = new NotificationService(repo);

        Task task = new Task("Task Past");
        repo.addTask(2L, task);

        LocalDateTime now = LocalDateTime.of(2024, 6, 10, 10, 0);

        assertThrows(IllegalArgumentException.class,
                () -> service.scheduleReminder(2L, now.minusMinutes(1), now));
    }

    private static Notification getNotification(Task task) {
        try {
            Field field = Task.class.getDeclaredField("notification");
            field.setAccessible(true);
            return (Notification) field.get(task);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Unable to read notification", ex);
        }
    }

    private static class InMemoryTaskRepository implements TaskRepository {
        private final Map<Long, Task> tasks = new HashMap<>();
        private Task lastSaved;

        void addTask(Long id, Task task) {
            tasks.put(id, task);
        }

        Task getLastSaved() {
            return lastSaved;
        }

        @Override
        public Task findById(Long taskId) {
            return tasks.get(taskId);
        }

        @Override
        public List<Task> findByProjectId(Long projectId) {
            return List.of();
        }

        @Override
        public List<Task> findByAssignedUserId(Long userId) {
            return List.of();
        }

        @Override
        public void save(Task task) {
            lastSaved = task;
        }

        @Override
        public void delete(Long taskId) {
            throw new UnsupportedOperationException("Not needed for these tests");
        }
    }
}
