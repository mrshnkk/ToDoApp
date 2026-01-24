package de.thws.Application.Domain.DomainModels.hydrators;

import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.User;

import java.time.LocalDateTime;

public final class TeamHydrator {
    private TeamHydrator() {
    }

    public static Team fromPersisted(
            Long teamId,
            String teamName,
            String description,
            User owner,
            LocalDateTime createdAt) {
        return new Team(teamId, teamName, description, owner, createdAt);
    }
}
