package de.thws.Adapters.web_in;

import de.thws.Adapters.web_in.dto.TaskCreateRequest;
import de.thws.Adapters.web_in.dto.TaskUpdateRequest;
import de.thws.Adapters.web_in.dto.ItemWithSelfLink;
import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Domain.Services.TaskFilter;
import de.thws.Application.Ports.in.ProjectUseCase;
import de.thws.Application.Ports.in.TaskUseCase;
import de.thws.Application.Ports.in.UserUseCase;
import io.quarkus.cache.CacheResult;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskController {
    private static final String TASKS_PATH = "tasks";

    @Inject
    TaskUseCase taskUseCase;

    @Inject
    UserUseCase userUseCase;

    @Inject
    ProjectUseCase projectUseCase;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{id}")
    @CacheResult(cacheName = "taskById")
    public Response getById(@PathParam("id") Long id) {
        Task task = taskUseCase.findById(id).orElseThrow(NotFoundException::new);
        List<Link> links = new ArrayList<>(LinkHeaderSupport.resourceLinks(uriInfo, TASKS_PATH, task.getTaskId()));
        Long assignedUserId = task.getAssignedUser() != null ? task.getAssignedUser().getUserId() : null;
        links.addAll(LinkHeaderSupport.taskAssignmentLinks(uriInfo, task.getTaskId(), assignedUserId));
        return Response.ok(task).links(links.toArray(new Link[0])).build();
    }

    @GET
    @Path("/project/{projectId}")
    @CacheResult(cacheName = "tasksByProject")
    public Response getByProject(
            @PathParam("projectId") Long projectId,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        PageSlice<Task> slice = PageSlice.from(taskUseCase.findByProjectId(projectId), page, size);
        return buildCollectionResponse(slice);
    }

    @GET
    @Path("/assigned/{userId}")
    @CacheResult(cacheName = "tasksByAssignedUser")
    public Response getByAssignedUser(
            @PathParam("userId") Long userId,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        PageSlice<Task> slice = PageSlice.from(taskUseCase.findByAssignedUserId(userId), page, size);
        return buildCollectionResponse(slice);
    }

    @GET
    @CacheResult(cacheName = "taskQuery")
    public Response queryTasks(
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
        PageSlice<Task> slice = PageSlice.from(taskUseCase.queryForUser(assignedUserId, filter), page, size);
        return buildCollectionResponse(slice);
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

    @PUT
    @Path("/{id}/assign/{userId}")
    public Task assign(@PathParam("id") Long id, @PathParam("userId") Long userId) {
        Task task = taskUseCase.findById(id).orElseThrow(NotFoundException::new);
        User user = userUseCase.findById(userId).orElseThrow(NotFoundException::new);
        task.assignToUser(user);
        return taskUseCase.update(task);
    }

    @DELETE
    @Path("/{id}/assign")
    public Task unassign(@PathParam("id") Long id) {
        Task task = taskUseCase.findById(id).orElseThrow(NotFoundException::new);
        task.assignToUser(null);
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

    private Response buildCollectionResponse(PageSlice<Task> slice) {
        List<ItemWithSelfLink<Task>> body = wrapWithSelfLinks(slice.getItems());
        List<Link> links = new ArrayList<>();
        links.addAll(LinkHeaderSupport.collectionLinks(
                uriInfo,
                slice.getPage(),
                slice.getSize(),
                slice.hasNext(),
                slice.hasPrev()));
        links.addAll(actionLinksForTasks(slice.getItems()));
        return Response.ok(body).links(links.toArray(new Link[0])).build();
    }

    private List<ItemWithSelfLink<Task>> wrapWithSelfLinks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return List.of();
        }
        List<ItemWithSelfLink<Task>> result = new ArrayList<>(tasks.size());
        for (Task task : tasks) {
            Long taskId = task.getTaskId();
            String self = taskId == null ? null : LinkHeaderSupport.resourceHref(uriInfo, TASKS_PATH, taskId);
            result.add(new ItemWithSelfLink<>(task, self));
        }
        return result;
    }

    private List<Link> actionLinksForTasks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return List.of();
        }
        List<Link> links = new ArrayList<>(tasks.size() * 4);
        for (Task task : tasks) {
            Long taskId = task.getTaskId();
            if (taskId != null) {
                links.addAll(LinkHeaderSupport.actionLinks(uriInfo, TASKS_PATH, taskId));
                Long assignedUserId = task.getAssignedUser() != null ? task.getAssignedUser().getUserId() : null;
                links.addAll(LinkHeaderSupport.taskAssignmentLinks(uriInfo, taskId, assignedUserId));
            }
        }
        return links;
    }
}
