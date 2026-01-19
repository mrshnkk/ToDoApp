package de.thws.Application.Ports.out;

import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.User;

import java.util.List;

public interface TeamRepository {
    Team findById(Long teamId);
    List<Team> findByOwnerId(Long ownerId);
    List<Team> findByUserId(Long userId);
    List<User> findMembers(Long teamId);
    void addMember(Long teamId, Long userId);
    void removeMember(Long teamId, Long userId);
    void save(Team team);
    void delete(Long teamId);
}
