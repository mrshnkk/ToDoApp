package de.thws.Application.Domain.Services;

import de.thws.Application.Domain.DomainModels.*;
import de.thws.Application.Ports.out.*;
import java.util.List;

public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team createTeamWithMembers(Long ownerId, String teamName, String description, List<String> memberUsernames) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Owner not found: " + ownerId));

        Team team = new Team(teamName, owner);

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
