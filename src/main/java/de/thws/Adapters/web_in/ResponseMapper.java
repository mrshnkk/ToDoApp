package de.thws.Adapters.web_in;

import de.thws.Adapters.web_in.dto.ProjectResponse;
import de.thws.Adapters.web_in.dto.TaskResponse;
import de.thws.Adapters.web_in.dto.TeamResponse;
import de.thws.Adapters.web_in.dto.UserResponse;
import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.User;

import java.util.ArrayList;
import java.util.List;

public final class ResponseMapper {
    private ResponseMapper() {
    }

    public static UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt());
    }

    public static ProjectResponse toProjectResponse(Project project) {
        if (project == null) {
            return null;
        }
        Long ownerId = project.getOwner() != null ? project.getOwner().getUserId() : null;
        return new ProjectResponse(
                project.getProjectId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                ownerId,
                project.getTeamId());
    }

    public static TeamResponse toTeamResponse(Team team) {
        if (team == null) {
            return null;
        }
        Long ownerId = team.getOwner() != null ? team.getOwner().getUserId() : null;
        return new TeamResponse(
                team.getTeamId(),
                team.getTeamName(),
                team.getDescription(),
                team.getCreatedAt(),
                ownerId);
    }

    public static TaskResponse toTaskResponse(Task task) {
        if (task == null) {
            return null;
        }
        Long assignedUserId = task.getAssignedUser() != null ? task.getAssignedUser().getUserId() : null;
        Long projectId = task.getProject() != null ? task.getProject().getProjectId() : null;
        return new TaskResponse(
                task.getTaskId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.getPriority() != null ? task.getPriority().name() : null,
                task.getStatus() != null ? task.getStatus().name() : null,
                task.getTags(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                projectId,
                assignedUserId);
    }

    public static List<UserResponse> toUserResponses(List<User> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }
        List<UserResponse> result = new ArrayList<>(users.size());
        for (User user : users) {
            result.add(toUserResponse(user));
        }
        return result;
    }
}
