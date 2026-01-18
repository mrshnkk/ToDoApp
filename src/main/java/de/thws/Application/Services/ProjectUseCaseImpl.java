package de.thws.Application.Services;

import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Ports.in.ProjectUseCase;
import de.thws.Application.Ports.out.ProjectRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProjectUseCaseImpl implements ProjectUseCase {
    private final ProjectRepository projectRepository;

    @Inject
    public ProjectUseCaseImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project create(Project project) {
        projectRepository.save(project);
        return project;
    }

    @Override
    public Optional<Project> findById(Long projectId) {
        return Optional.ofNullable(projectRepository.findById(projectId));
    }

    @Override
    public List<Project> findByOwnerId(Long ownerId) {
        List<Project> result = projectRepository.findByOwnerId(ownerId);
        return result == null ? List.of() : result;
    }

    @Override
    public List<Project> findByTeamId(Long teamId) {
        List<Project> result = projectRepository.findByTeamId(teamId);
        return result == null ? List.of() : result;
    }

    @Override
    public void delete(Long projectId) {
        projectRepository.delete(projectId);
    }
}
