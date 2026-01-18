package de.thws.Adapters.web_in;

import de.thws.Adapters.web_in.dto.TeamCreateRequest;
import de.thws.Adapters.web_in.dto.TeamUpdateRequest;
import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.in.TeamUseCase;
import de.thws.Application.Ports.in.UserUseCase;
import jakarta.inject.Inject;
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

import java.util.List;

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamController {

    @Inject
    TeamUseCase teamUseCase;

    @Inject
    UserUseCase userUseCase;

    @GET
    @Path("/{id}")
    public Team getById(@PathParam("id") Long id) {
        return teamUseCase.findById(id).orElseThrow(NotFoundException::new);
    }

    @GET
    public List<Team> getByQuery(
            @QueryParam("ownerId") Long ownerId,
            @QueryParam("userId") Long userId,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        if (ownerId != null) {
            return paginate(teamUseCase.findByOwnerId(ownerId), page, size);
        }
        if (userId != null) {
            return paginate(teamUseCase.findByUserId(userId), page, size);
        }
        return List.of();
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

    @POST
    public Team create(TeamCreateRequest request) {
        User owner = userUseCase.findById(request.getOwnerId())
                .orElseThrow(NotFoundException::new);
        Team team = request.getDescription() == null
                ? new Team(request.getTeamName(), owner)
                : new Team(request.getTeamName(), request.getDescription(), owner);
        return teamUseCase.create(team);
    }

    @PUT
    @Path("/{id}")
    public Team update(@PathParam("id") Long id, TeamUpdateRequest request) {
        Team team = teamUseCase.findById(id).orElseThrow(NotFoundException::new);
        String teamName = request.getTeamName() != null ? request.getTeamName() : team.getTeamName();
        String description = request.getDescription() != null ? request.getDescription() : team.getDescription();
        team.updateTeam(teamName, description);
        return teamUseCase.update(team);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        teamUseCase.delete(id);
    }
}
