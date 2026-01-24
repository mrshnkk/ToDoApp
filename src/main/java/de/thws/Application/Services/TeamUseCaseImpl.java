package de.thws.Application.Services;

import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.TeamMember;
import de.thws.Application.Domain.DomainModels.TeamRole;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Domain.Exceptions.DuplicateTeamMemberException;
import de.thws.Application.Exceptions.TeamNotFoundException;
import de.thws.Application.Exceptions.UserNotFoundException;
import de.thws.Application.Ports.in.TeamUseCase;
import de.thws.Application.Ports.out.TeamRepository;
import de.thws.Application.Ports.out.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TeamUseCaseImpl implements TeamUseCase {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Inject
    public TeamUseCaseImpl(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
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
    public List<User> findMembers(Long teamId) {
        List<User> result = teamRepository.findMembers(teamId);
        return result == null ? List.of() : result;
    }

    @Override
    public void addMember(Long teamId, Long userId) {
        teamRepository.addMember(teamId, userId);
    }

    @Override
    public void removeMember(Long teamId, Long userId) {
        teamRepository.removeMember(teamId, userId);
    }

    @Override
    public void delete(Long teamId) {
        teamRepository.delete(teamId);
    }

    @Override
    public Team createTeamWithMembers(Long ownerId, String teamName, String description, List<String> memberUsernames) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Owner not found: " + ownerId));

        Team team = new Team(teamName, description, owner);

        if (memberUsernames != null) {
            for (String username : memberUsernames) {
                addMemberByUsername(team, username);
            }
        }

        teamRepository.save(team);
        return team;
    }

    @Override
    public Team addMembersByUsername(Long teamId, List<String> memberUsernames) {
        if (teamId == null) {
            throw new IllegalArgumentException("Team id is required");
        }

        Team team = teamRepository.findById(teamId);
        if (team == null) {
            throw new TeamNotFoundException("Team not found: " + teamId);
        }

        if (memberUsernames != null) {
            for (String username : memberUsernames) {
                addMemberByUsername(team, username);
            }
        }

        teamRepository.save(team);
        return team;
    }

    private void addMemberByUsername(Team team, String username) {
        String normalized = normalizeUsername(username);
        if (isAlreadyMember(team, normalized)) {
            throw new DuplicateTeamMemberException("User already in team: " + normalized);
        }

        User user = userRepository.findByUsername(normalized)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + normalized));

        team.addMember(user, TeamRole.MEMBER);
    }

    private boolean isAlreadyMember(Team team, String username) {
        for (TeamMember member : team.getTeamMembers()) {
            if (member.getUser().getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private String normalizeUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        return username.trim();
    }
}
