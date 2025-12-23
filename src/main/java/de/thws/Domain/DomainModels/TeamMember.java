package de.thws.Domain.DomainModels;

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
    public User getUser() {
        return user;
    }
    public TeamRole getRole() {
        return role;
    }

}
