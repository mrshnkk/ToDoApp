package de.thws.Adapters.persistence_out;

import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.ProjectRepository;
import de.thws.Application.Ports.out.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class ProjectRepositoryJpaTest {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    UserRepository userRepository;

    @Test
    @TestTransaction
    void saveAndQueryProject() {
        User owner = new User("projowner1", "projowner1@test.com", "Abcdef!1");
        userRepository.save(owner);

        Project project = new Project("Project Alpha", owner);
        project.setTeamId(42L);
        projectRepository.save(project);

        assertNotNull(project.getProjectId());

        Project byId = projectRepository.findById(project.getProjectId());
        assertNotNull(byId);
        assertEquals("Project Alpha", byId.getName());

        List<Project> byOwner = projectRepository.findByOwnerId(owner.getUserId());
        assertEquals(1, byOwner.size());

        List<Project> byTeam = projectRepository.findByTeamId(42L);
        assertEquals(1, byTeam.size());
    }
}
