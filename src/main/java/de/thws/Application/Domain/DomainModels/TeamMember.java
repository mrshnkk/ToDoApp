package de.thws.Application.Domain.DomainModels;

import java.time.LocalDateTime;

public class TeamMember{
    private final User user;
    private TeamRole role;
    private final LocalDateTime joinedAt;
    public TeamMember(User user, TeamRole role) {
        this.user = user;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }
    TeamMember(User user, TeamRole role, LocalDateTime joinedAt) {
        this.user = user;
        this.role = role;
        this.joinedAt = joinedAt != null ? joinedAt : LocalDateTime.now();
    }
    public static TeamMember fromPersisted(User user, TeamRole role, LocalDateTime joinedAt) {
        return new TeamMember(user, role, joinedAt);
    }
    public User getUser() {
        return user;
    }
    public TeamRole getRole() {
        return role;
    }
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

}
