package de.thws.Application.Domain.DomainModels;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private Long teamId;
    private String teamName;
    private String description;
    private List<TeamMember> teamMembers;
    private final LocalDateTime createdAt;
    private final User owner;

    public Team(String teamName, User owner) {
        this.teamName = teamName;
        this.createdAt = LocalDateTime.now();
        this.owner = owner;
        this.teamMembers = new ArrayList<>();
        this.teamMembers.add(new TeamMember(owner, TeamRole.OWNER));
    }

    public Team(Long teamId, String teamName, String description, User owner, LocalDateTime createdAt) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.description = description;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.owner = owner;
        this.teamMembers = new ArrayList<>();
        if (owner != null) {
            this.teamMembers.add(new TeamMember(owner, TeamRole.OWNER));
        }
    }

    public Team(String teamName, String description, User owner) {
        this(teamName, owner);
        this.description = description;
    }


    public void updateTeam(String teamName, String description) {
        this.teamName = teamName;
        this.description = description;
    }
    

    public void addMember(User user, TeamRole teamRole) {
        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }
        if (teamRole == null) {
            throw new IllegalArgumentException("Team role is required");
        }
        if (isMember(user)) {
            throw new IllegalArgumentException("User already in team");
        }
        teamMembers.add(new TeamMember(user, teamRole));
    }

    //Username is a unique identifier for every user (in case if username is not changed)
    // -> every user can be a team member -> unique id in the domain model is a username and has nothing to do with the DB
    public void removeMember(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        String normalized = username.trim();
        teamMembers.removeIf(tm -> tm.getUser().getUsername().equals(normalized));  //implements a func Interface
    }

    private TeamMember findMemberByUsername(String username) {
        for (TeamMember tm : teamMembers) {
            if (tm.getUser().getUsername().equals(username))
                return tm;
        }
        return null;
    }


    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public String getTeamName() {
        return teamName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        if (this.teamId != null) {
            throw new IllegalStateException("Team ID already set");
        }
        this.teamId = teamId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getOwner() {
        return owner;
    }


    public void assertCanAssignProjects(User user) {
        TeamMember member = findMember(user);
        if (member == null) {
            throw new IllegalStateException("User is not a team member");
        }
        if (!member.getRole().canManageProjects()) {
            throw new IllegalStateException("User has no permission to assign projects");
        }
    }

    private TeamMember findMember(User user) {
        return teamMembers.stream()
                .filter(m -> m.getUser().equals(user))
                .findFirst()
                .orElse(null);
    }

    private boolean isMember(User user) {
        Long userId = user.getUserId();
        String username = normalizeUsername(user.getUsername());
        for (TeamMember member : teamMembers) {
            User existing = member.getUser();
            if (existing == null) {
                continue;
            }
            if (userId != null && userId.equals(existing.getUserId())) {
                return true;
            }
            if (userId == null && username != null) {
                String existingUsername = normalizeUsername(existing.getUsername());
                if (username.equals(existingUsername)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            return null;
        }
        String normalized = username.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
