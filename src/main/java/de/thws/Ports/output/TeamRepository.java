package de.thws.Ports.output;

import de.thws.Domain.DomainModels.Team;

import java.util.List;

public interface TeamRepository {
    Team findById(Long teamId);
    List<Team> findByOwnerId(Long ownerId);
    List<Team> findByUserId(Long userId);
    void save(Team team);
    void delete(Long teamId);
}
