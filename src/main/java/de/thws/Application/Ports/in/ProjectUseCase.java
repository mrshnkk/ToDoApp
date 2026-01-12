package de.thws.Application.Ports.in;

import de.thws.Application.Domain.DomainModels.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectUseCase {
    Project create(Project project);
    Optional<Project> findById(Long projectId);
    List<Project> findByOwnerId(Long ownerId);
    List<Project> findByTeamId(Long teamId);
    void delete(Long projectId);
}
