package de.thws.Adapters.web_in;

import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Ports.in.TeamUseCase;
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

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamController {

    @Inject
    TeamUseCase teamUseCase;

    @GET
    @Path("/{id}")
    public Team getById(@PathParam("id") Long id) {
        return teamUseCase.findById(id).orElseThrow(NotFoundException::new);
    }

    @GET
    public List<Team> getByQuery(@QueryParam("ownerId") Long ownerId, @QueryParam("userId") Long userId) {
        if (ownerId != null) {
            return teamUseCase.findByOwnerId(ownerId);
        }
        if (userId != null) {
            return teamUseCase.findByUserId(userId);
        }
        return List.of();
    }

    @POST
    public Team create(Team team) {
        return teamUseCase.create(team);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        teamUseCase.delete(id);
    }
}
