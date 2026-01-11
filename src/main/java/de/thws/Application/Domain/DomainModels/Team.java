package de.thws.Application.Domain.DomainModels;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private String teamName;
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


    public void updateTeam(String teamName, String description) {
        this.teamName = teamName;

    }

    public void addMember(User user, TeamRole teamRole) {
        teamMembers.add(new TeamMember(user, teamRole));
    }

    //Username is a unique identifier for every user (in case if username is not changed)
    // -> every user can be a team member -> unique id in the domain model is a username and has nothing to do with the DB
    public void removeMember(String username) {
        teamMembers.removeIf(tm -> tm.getUser().getEmail().equals(username));  //implements a func Interface
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
}
