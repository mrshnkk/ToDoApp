package de.thws.Adapters.web_in;

import de.thws.Application.Domain.DomainModels.Task;
import de.thws.Application.Ports.in.TaskUseCase;
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
    public Task create(Task task) {
        return taskUseCase.create(task);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        taskUseCase.delete(id);
    }
}
