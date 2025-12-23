package de.thws.Ports.output;
import de.thws.Domain.DomainModels.Project;
import java.util.List;

public interface ProjectRepository {
    Project findById(Long projectId);
    List<Project> findByOwnerId(Long ownerId);
    List<Project> findByTeamId(Long teamId);
    void save(Project project);
    void delete(Long projectId);
}
