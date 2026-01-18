package de.thws.Adapters.persistence_out;

import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.ProjectRepository;
import de.thws.Application.Ports.out.TaskRepository;
import de.thws.Application.Ports.out.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class TaskRepositoryJpaTest {

    @Inject
    TaskRepository taskRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ProjectRepository projectRepository;

    @Test
    @TestTransaction
    void saveAndQueryTasks() {
        User owner = new User("taskowner1", "taskowner1@test.com", "Abcdef!1");
        userRepository.save(owner);

        User assignee = new User("taskassignee1", "taskassignee1@test.com", "Abcdef!1");
        userRepository.save(assignee);

        Project project = new Project("Task Project", owner);
        projectRepository.save(project);

        Task task = new Task("Task One");
        task.changeDescription("desc");
        task.assignToProject(project);
        task.assignToUser(assignee);
        task.addTag("urgent");
        taskRepository.save(task);

        assertNotNull(task.getTaskId());

        Task byId = taskRepository.findById(task.getTaskId());
        assertNotNull(byId);
        assertEquals("Task One", byId.getTitle());
        assertEquals("desc", byId.getDescription());
        assertEquals(Set.of("urgent"), byId.getTags());

        List<Task> byProject = taskRepository.findByProjectId(project.getProjectId());
        assertEquals(1, byProject.size());

        List<Task> byAssignee = taskRepository.findByAssignedUserId(assignee.getUserId());
        assertEquals(1, byAssignee.size());
    }
}
