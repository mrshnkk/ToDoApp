package de.thws.Application.Ports.in;

import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.User;

import java.util.List;
import java.util.Optional;

public interface TeamUseCase {
    Team create(Team team);
    Team update(Team team);
    Optional<Team> findById(Long teamId);
    List<Team> findByOwnerId(Long ownerId);
    List<Team> findByUserId(Long userId);
    List<User> findMembers(Long teamId);
    void addMember(Long teamId, Long userId);
    void removeMember(Long teamId, Long userId);
    Team createTeamWithMembers(Long ownerId, String teamName, String description, List<String> memberUsernames);
    Team addMembersByUsername(Long teamId, List<String> memberUsernames);
    void delete(Long teamId);
}
