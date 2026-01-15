package de.thws.Adapters.web_in;

import de.thws.Adapters.web_in.dto.TaskCreateRequest;
import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.in.ProjectUseCase;
import de.thws.Application.Ports.in.TaskUseCase;
import de.thws.Application.Ports.in.UserUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

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
    public List<Task> getByProject(@PathParam("projectId") Long projectId) {
        return taskUseCase.findByProjectId(projectId);
    }

    @GET
    @Path("/assigned/{userId}")
    public List<Task> getByAssignedUser(@PathParam("userId") Long userId) {
        return taskUseCase.findByAssignedUserId(userId);
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

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        taskUseCase.delete(id);
    }
}
