package de.thws.Domain.DomainModels;

import java.time.LocalDateTime;

public class TeamMember{
    private final long teamId;
    private final User user;
    private TeamRole role;
    private LocalDateTime joinedAt;
    public TeamMember(Long teamId, User user, TeamRole role, LocalDateTime joinedAt) {
        this.teamId = teamId;
        this.user = user;
        this.role = role;
        this.joinedAt = joinedAt;
    }
    public User getUser() {
        return user;
    }
    public TeamRole getRole() {
        return role;
    }

}
