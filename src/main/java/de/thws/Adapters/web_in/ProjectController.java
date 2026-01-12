package de.thws.Adapters.web_in;

import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Ports.in.ProjectUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectController {

    @Inject
    ProjectUseCase projectUseCase;

    @GET
    @Path("/{id}")
    public Project getById(@PathParam("id") Long id) {
        return projectUseCase.findById(id).orElseThrow(NotFoundException::new);
    }

    @GET
    public List<Project> getByQuery(@QueryParam("ownerId") Long ownerId, @QueryParam("teamId") Long teamId) {
        if (ownerId != null) {
            return projectUseCase.findByOwnerId(ownerId);
        }
        if (teamId != null) {
            return projectUseCase.findByTeamId(teamId);
        }
        return List.of();
    }

    @POST
    public Project create(Project project) {
        return projectUseCase.create(project);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        projectUseCase.delete(id);
    }
}
