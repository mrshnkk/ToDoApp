package de.thws.Adapters.web_in;

import de.thws.Adapters.web_in.dto.TaskCreateRequest;
import de.thws.Adapters.web_in.dto.TaskUpdateRequest;
import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Domain.Services.TaskFilter;
import de.thws.Application.Ports.in.ProjectUseCase;
import de.thws.Application.Ports.in.TaskUseCase;
import de.thws.Application.Ports.in.UserUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskController {

    @Inject
    TaskUseCase taskUseCase;

    @Inject
    UserUseCase userUseCase;

    @Inject
    ProjectUseCase projectUseCase;

    @GET
    @Path("/{id}")
    public Task getById(@PathParam("id") Long id) {
        return taskUseCase.findById(id).orElseThrow(NotFoundException::new);
    }

    @GET
    @Path("/project/{projectId}")
    public List<Task> getByProject(
            @PathParam("projectId") Long projectId,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        return paginate(taskUseCase.findByProjectId(projectId), page, size);
    }

    @GET
    @Path("/assigned/{userId}")
    public List<Task> getByAssignedUser(
            @PathParam("userId") Long userId,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        return paginate(taskUseCase.findByAssignedUserId(userId), page, size);
    }

    @GET
    public List<Task> queryTasks(
            @QueryParam("assignedUserId") Long assignedUserId,
            @QueryParam("status") String status,
            @QueryParam("priority") String priority,
            @QueryParam("projectId") Long projectId,
            @QueryParam("tags") String tags,
            @QueryParam("dueDate") String dueDate,
            @QueryParam("teamId") Long teamId,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        if (assignedUserId == null) {
            throw new BadRequestException("assignedUserId is required");
        }
        Project project = null;
        if (projectId != null) {
            project = projectUseCase.findById(projectId).orElseThrow(NotFoundException::new);
        }
        LocalDate dueDateValue = null;
        if (dueDate != null) {
            dueDateValue = LocalDate.parse(dueDate);
        }
        Set<String> tagSet = parseTags(tags);
        TaskFilter filter = new TaskFilter(status, priority, project, tagSet, dueDateValue, teamId);
        return paginate(taskUseCase.queryForUser(assignedUserId, filter), page, size);
    }

    @POST
    public Task create(TaskCreateRequest request) {
        Task task = new Task(request.getTitle());
        if (request.getDescription() != null) {
            task.changeDescription(request.getDescription());
        }
        if (request.getDeadline() != null) {
            task.setDeadline(request.getDeadline());
        }
        if (request.getPriority() != null) {
            task.changePriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            task.changeStatus(request.getStatus());
        }
        if (request.getAssignedUserId() != null) {
            User user = userUseCase.findById(request.getAssignedUserId())
                    .orElseThrow(NotFoundException::new);
            task.assignToUser(user);
        }
        if (request.getProjectId() != null) {
            Project project = projectUseCase.findById(request.getProjectId())
                    .orElseThrow(NotFoundException::new);
            task.assignToProject(project);
        }
        if (request.getTags() != null) {
            for (String tag : request.getTags()) {
                task.addTag(tag);
            }
        }
        return taskUseCase.create(task);
    }

    @PUT
    @Path("/{id}")
    public Task update(@PathParam("id") Long id, TaskUpdateRequest request) {
        Task task = taskUseCase.findById(id).orElseThrow(NotFoundException::new);
        if (request.getTitle() != null) {
            task.renameTask(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.changeDescription(request.getDescription());
        }
        if (request.getDeadline() != null) {
            task.setDeadline(request.getDeadline());
        }
        if (request.getPriority() != null) {
            task.changePriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            task.changeStatus(request.getStatus());
        }
        if (request.getAssignedUserId() != null) {
            User user = userUseCase.findById(request.getAssignedUserId())
                    .orElseThrow(NotFoundException::new);
            task.assignToUser(user);
        }
        if (request.getProjectId() != null) {
            Project project = projectUseCase.findById(request.getProjectId())
                    .orElseThrow(NotFoundException::new);
            task.assignToProject(project);
        }
        if (request.getTags() != null) {
            task.getTags().clear();
            for (String tag : request.getTags()) {
                task.addTag(tag);
            }
        }
        return taskUseCase.update(task);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        taskUseCase.delete(id);
    }

    private static Set<String> parseTags(String tags) {
        if (tags == null || tags.trim().isEmpty()) {
            return Set.of();
        }
        String[] parts = tags.split(",");
        Set<String> result = new HashSet<>();
        for (String part : parts) {
            String tag = part.trim();
            if (!tag.isEmpty()) {
                result.add(tag);
            }
        }
        return result;
    }

    private static <T> List<T> paginate(List<T> items, Integer page, Integer size) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }
        int safePage = page == null ? 0 : Math.max(0, page);
        int safeSize = size == null ? 20 : Math.max(1, size);
        int fromIndex = safePage * safeSize;
        if (fromIndex >= items.size()) {
            return List.of();
        }
        int toIndex = Math.min(items.size(), fromIndex + safeSize);
        return items.subList(fromIndex, toIndex);
    }
}
