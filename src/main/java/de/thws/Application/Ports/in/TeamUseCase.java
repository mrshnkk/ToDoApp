package de.thws.Application.Ports.in;

import de.thws.Application.Domain.DomainModels.Team;

import java.util.List;
import java.util.Optional;

public interface TeamUseCase {
    Team create(Team team);
    Optional<Team> findById(Long teamId);
    List<Team> findByOwnerId(Long ownerId);
    List<Team> findByUserId(Long userId);
    void delete(Long teamId);
}
