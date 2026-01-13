package de.thws.Application.Domain.Services;

import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.TaskRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskQueryServiceTest {

    @Test
    void filtersByDueDate() {
        Long userId = 1L;
        Task taskOne = new Task("Task A");
        taskOne.setDeadline(LocalDate.of(2024, 6, 10));
        Task taskTwo = new Task("Task B");
        taskTwo.setDeadline(LocalDate.of(2024, 6, 11));

        TaskQueryService service = serviceWithTasks(userId, List.of(taskOne, taskTwo));

        TaskFilter filter = new TaskFilter(null, null, null, null, LocalDate.of(2024, 6, 10), null);

        List<Task> result = service.getTasksForUser(userId, filter);

        assertEquals(1, result.size());
        assertEquals("Task A", result.getFirst().getTitle());
    }

    @Test
    void filtersByTeamId() {
        Long userId = 2L;
        Project teamOneProject = new Project(
                "Project A",
                new TestUser("owner1", "owner1@test.com", "Abcdef!1"));
        setTeamId(teamOneProject, 10L);

        Project teamTwoProject = new Project(
                "Project B",
                new TestUser("owner2", "owner2@test.com", "Abcdef!1"));
        setTeamId(teamTwoProject, 20L);

        Task teamOneTask = new Task("Team One Task");
        teamOneTask.assignToProject(teamOneProject);

        Task teamTwoTask = new Task("Team Two Task");
        teamTwoTask.assignToProject(teamTwoProject);

        TaskQueryService service = serviceWithTasks(userId, List.of(teamOneTask, teamTwoTask));

        TaskFilter filter = new TaskFilter(null, null, null, null, null, 10L);

        List<Task> result = service.getTasksForUser(userId, filter);

        assertEquals(1, result.size());
        assertEquals("Team One Task", result.getFirst().getTitle());
    }

    private static TaskQueryService serviceWithTasks(Long userId, List<Task> tasks) {
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        repo.setTasksForUser(userId, tasks);
        return new TaskQueryService(repo);
    }

    private static void setTeamId(Project project, Long teamId) {
        try {
            Field field = Project.class.getDeclaredField("teamId");
            field.setAccessible(true);
            field.set(project, teamId);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Unable to set teamId", ex);
        }
    }

    private static class InMemoryTaskRepository implements TaskRepository {
        private final Map<Long, List<Task>> tasksByUserId = new HashMap<>();

        void setTasksForUser(Long userId, List<Task> tasks) {
            tasksByUserId.put(userId, tasks);
        }

        @Override
        public Task findById(Long taskId) {
            return null;
        }

        @Override
        public List<Task> findByProjectId(Long projectId) {
            return List.of();
        }

        @Override
        public List<Task> findByAssignedUserId(Long userId) {
            return tasksByUserId.getOrDefault(userId, List.of());
        }

        @Override
        public void save(Task task) {
            throw new UnsupportedOperationException("Not needed for these tests");
        }

        @Override
        public void delete(Long taskId) {
            throw new UnsupportedOperationException("Not needed for these tests");
        }
    }

    private static class TestUser extends User {
        TestUser(String username, String email, String password) {
            super(username, email, password);
        }

        @Override
        public void validateUsername(String username) {
            if (username == null) {
                throw new IllegalArgumentException("Username is required");
            }
            if (username.length() < 4 || username.length() > 16) {
                throw new IllegalArgumentException("Username has to be between 4 and 16 characters");
            }
            if (username.contains(" ")) {
                throw new IllegalArgumentException("Username should not contain spaces.");
            }
        }
    }
}