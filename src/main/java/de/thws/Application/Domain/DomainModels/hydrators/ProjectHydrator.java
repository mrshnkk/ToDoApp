package de.thws.Application.Domain.DomainModels.hydrators;

import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ProjectHydrator {
    private ProjectHydrator() {
    }

    public static Project fromPersisted(
            Long projectId,
            String name,
            String description,
            LocalDateTime startDate,
            LocalDate endDate,
            User owner,
            Long teamId) {
        return new Project(projectId, name, description, startDate, endDate, owner, teamId);
    }
}
