package de.thws.Application.Services;

import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Ports.in.TeamUseCase;
import de.thws.Application.Ports.out.TeamRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TeamUseCaseImpl implements TeamUseCase {
    private final TeamRepository teamRepository;

    @Inject
    public TeamUseCaseImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team create(Team team) {
        teamRepository.save(team);
        return team;
    }

    @Override
    public Team update(Team team) {
        teamRepository.save(team);
        return team;
    }

    @Override
    public Optional<Team> findById(Long teamId) {
        return Optional.ofNullable(teamRepository.findById(teamId));
    }

    @Override
    public List<Team> findByOwnerId(Long ownerId) {
        List<Team> result = teamRepository.findByOwnerId(ownerId);
        return result == null ? List.of() : result;
    }

    @Override
    public List<Team> findByUserId(Long userId) {
        List<Team> result = teamRepository.findByUserId(userId);
        return result == null ? List.of() : result;
    }

    @Override
    public void delete(Long teamId) {
        teamRepository.delete(teamId);
    }
}
