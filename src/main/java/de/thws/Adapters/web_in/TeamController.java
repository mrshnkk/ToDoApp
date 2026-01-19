package de.thws.Adapters.web_in;

import de.thws.Adapters.web_in.dto.TeamCreateRequest;
import de.thws.Adapters.web_in.dto.TeamUpdateRequest;
import de.thws.Adapters.web_in.dto.ItemWithSelfLink;
import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.in.TeamUseCase;
import de.thws.Application.Ports.in.UserUseCase;
import io.quarkus.cache.CacheResult;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.ArrayList;
import java.util.List;

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamController {
    private static final String TEAMS_PATH = "teams";

    @Inject
    TeamUseCase teamUseCase;

    @Inject
    UserUseCase userUseCase;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{id}")
    @CacheResult(cacheName = "teamById")
    public Response getById(@PathParam("id") Long id) {
        Team team = teamUseCase.findById(id).orElseThrow(NotFoundException::new);
        List<Link> links = new ArrayList<>(LinkHeaderSupport.resourceLinks(uriInfo, TEAMS_PATH, team.getTeamId()));
        links.addAll(LinkHeaderSupport.teamMemberLinks(uriInfo, team.getTeamId()));
        return Response.ok(team).links(links.toArray(new Link[0])).build();
    }

    @GET
    @CacheResult(cacheName = "teamQuery")
    public Response getByQuery(
            @QueryParam("ownerId") Long ownerId,
            @QueryParam("userId") Long userId,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        List<Team> result;
        if (ownerId != null) {
            result = teamUseCase.findByOwnerId(ownerId);
        } else if (userId != null) {
            result = teamUseCase.findByUserId(userId);
        } else {
            result = List.of();
        }
        PageSlice<Team> slice = PageSlice.from(result, page, size);
        return buildCollectionResponse(slice);
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

    @GET
    @Path("/{id}/members")
    public Response getMembers(@PathParam("id") Long id) {
        teamUseCase.findById(id).orElseThrow(NotFoundException::new);
        return buildMembersResponse(id, teamUseCase.findMembers(id));
    }

    @POST
    @Path("/{id}/members/{userId}")
    @Consumes(MediaType.WILDCARD)
    public Response addMember(@PathParam("id") Long id, @PathParam("userId") Long userId) {
        teamUseCase.findById(id).orElseThrow(NotFoundException::new);
        userUseCase.findById(userId).orElseThrow(NotFoundException::new);
        teamUseCase.addMember(id, userId);
        return buildMembersResponse(id, teamUseCase.findMembers(id));
    }

    @DELETE
    @Path("/{id}/members/{userId}")
    @Consumes(MediaType.WILDCARD)
    public Response removeMember(@PathParam("id") Long id, @PathParam("userId") Long userId) {
        teamUseCase.findById(id).orElseThrow(NotFoundException::new);
        teamUseCase.removeMember(id, userId);
        return buildMembersResponse(id, teamUseCase.findMembers(id));
    }

    private Response buildCollectionResponse(PageSlice<Team> slice) {
        List<ItemWithSelfLink<Team>> body = wrapWithSelfLinks(slice.getItems());
        List<Link> links = new ArrayList<>();
        links.addAll(LinkHeaderSupport.collectionLinks(
                uriInfo,
                slice.getPage(),
                slice.getSize(),
                slice.hasNext(),
                slice.hasPrev()));
        links.addAll(actionLinksForTeams(slice.getItems()));
        return Response.ok(body).links(links.toArray(new Link[0])).build();
    }

    private List<ItemWithSelfLink<Team>> wrapWithSelfLinks(List<Team> teams) {
        if (teams == null || teams.isEmpty()) {
            return List.of();
        }
        List<ItemWithSelfLink<Team>> result = new ArrayList<>(teams.size());
        for (Team team : teams) {
            Long teamId = team.getTeamId();
            String self = teamId == null ? null : LinkHeaderSupport.resourceHref(uriInfo, TEAMS_PATH, teamId);
            result.add(new ItemWithSelfLink<>(team, self));
        }
        return result;
    }

    private List<Link> actionLinksForTeams(List<Team> teams) {
        if (teams == null || teams.isEmpty()) {
            return List.of();
        }
        List<Link> links = new ArrayList<>(teams.size() * 3);
        for (Team team : teams) {
            Long teamId = team.getTeamId();
            if (teamId != null) {
                links.addAll(LinkHeaderSupport.actionLinks(uriInfo, TEAMS_PATH, teamId));
                links.addAll(LinkHeaderSupport.teamMemberLinks(uriInfo, teamId));
            }
        }
        return links;
    }

    private Response buildMembersResponse(Long teamId, List<User> members) {
        List<Link> links = new ArrayList<>();
        links.addAll(LinkHeaderSupport.teamMemberLinks(uriInfo, teamId));
        links.addAll(actionLinksForMembers(teamId, members));
        return Response.ok(members).links(links.toArray(new Link[0])).build();
    }

    private List<Link> actionLinksForMembers(Long teamId, List<User> members) {
        if (members == null || members.isEmpty()) {
            return List.of();
        }
        List<Link> links = new ArrayList<>(members.size());
        for (User member : members) {
            Long userId = member.getUserId();
            if (userId != null) {
                links.add(LinkHeaderSupport.teamMemberActionLink(uriInfo, teamId, userId, "remove-member"));
            }
        }
        return links;
    }
}
