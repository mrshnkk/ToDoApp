package de.thws.Adapters.web_in.Controllers;

import de.thws.Adapters.web_in.LinkHeaderSupport;
import de.thws.Adapters.web_in.PageSlice;
import de.thws.Adapters.web_in.ResponseMapper;
import de.thws.Adapters.web_in.dto.ItemWithSelfLink;
import de.thws.Adapters.web_in.dto.ProjectCreateRequest;
import de.thws.Adapters.web_in.dto.ProjectResponse;
import de.thws.Adapters.web_in.dto.ProjectUpdateRequest;
import de.thws.Application.Domain.DomainModels.Project;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.in.ProjectUseCase;
import de.thws.Application.Ports.in.UserUseCase;
import io.quarkus.cache.CacheResult;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.ArrayList;
import java.util.List;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectController {
    private static final String PROJECTS_PATH = "projects";

    @Inject
    ProjectUseCase projectUseCase;

    @Inject
    UserUseCase userUseCase;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{id}")
    @CacheResult(cacheName = "projectById")
    public Response getById(@PathParam("id") Long id) {
        Project project = projectUseCase.findById(id).orElseThrow(NotFoundException::new);
        ProjectResponse response = ResponseMapper.toProjectResponse(project);
        List<Link> links = LinkHeaderSupport.resourceLinks(uriInfo, PROJECTS_PATH, project.getProjectId());
        return Response.ok(response).links(links.toArray(new Link[0])).build();
    }

    @GET
    @CacheResult(cacheName = "projectQuery")
    public Response getByQuery(
            @QueryParam("ownerId") Long ownerId,
            @QueryParam("teamId") Long teamId,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        List<Project> result;
        if (ownerId != null) {
            result = projectUseCase.findByOwnerId(ownerId);
        } else if (teamId != null) {
            result = projectUseCase.findByTeamId(teamId);
        } else {
            result = List.of();
        }
        PageSlice<Project> slice = PageSlice.from(result, page, size);
        return buildCollectionResponse(slice);
    }

    @POST
    public Response create(ProjectCreateRequest request) {
        User owner = userUseCase.findById(request.getOwnerId())
                .orElseThrow(NotFoundException::new);
        Project project = new Project(request.getName(), owner);
        if (request.getDescription() != null) {
            project.updateDescription(request.getDescription());
        }
        if (request.getTeamId() != null) {
            project.setTeamId(request.getTeamId());
        }
        ProjectResponse response = ResponseMapper.toProjectResponse(projectUseCase.create(project));
        List<Link> links = LinkHeaderSupport.resourceLinks(uriInfo, PROJECTS_PATH, response.getProjectId());
        return Response.ok(response).links(links.toArray(new Link[0])).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, ProjectUpdateRequest request) {
        Project project = projectUseCase.findById(id).orElseThrow(NotFoundException::new);
        if (request.getName() != null) {
            project.updateName(request.getName());
        }
        if (request.getDescription() != null) {
            project.updateDescription(request.getDescription());
        }
        if (request.getEndDate() != null) {
            project.setEndDate(request.getEndDate());
        }
        if (request.getTeamId() != null) {
            project.setTeamId(request.getTeamId());
        }
        ProjectResponse response = ResponseMapper.toProjectResponse(projectUseCase.update(project));
        List<Link> links = LinkHeaderSupport.resourceLinks(uriInfo, PROJECTS_PATH, response.getProjectId());
        return Response.ok(response).links(links.toArray(new Link[0])).build();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        projectUseCase.delete(id);
    }

    private Response buildCollectionResponse(PageSlice<Project> slice) {
        List<ItemWithSelfLink<ProjectResponse>> body = wrapWithSelfLinks(slice.getItems());
        List<Link> links = new ArrayList<>();
        links.addAll(LinkHeaderSupport.collectionLinks(
                uriInfo,
                slice.getPage(),
                slice.getSize(),
                slice.hasNext(),
                slice.hasPrev()));
        links.addAll(actionLinksForProjects(slice.getItems()));
        return Response.ok(body).links(links.toArray(new Link[0])).build();
    }

    private List<ItemWithSelfLink<ProjectResponse>> wrapWithSelfLinks(List<Project> projects) {
        if (projects == null || projects.isEmpty()) {
            return List.of();
        }
        List<ItemWithSelfLink<ProjectResponse>> result = new ArrayList<>(projects.size());
        for (Project project : projects) {
            Long projectId = project.getProjectId();
            String self = projectId == null ? null : LinkHeaderSupport.resourceHref(uriInfo, PROJECTS_PATH, projectId);
            result.add(new ItemWithSelfLink<>(ResponseMapper.toProjectResponse(project), self));
        }
        return result;
    }

    private List<Link> actionLinksForProjects(List<Project> projects) {
        if (projects == null || projects.isEmpty()) {
            return List.of();
        }
        List<Link> links = new ArrayList<>(projects.size() * 2);
        for (Project project : projects) {
            Long projectId = project.getProjectId();
            if (projectId != null) {
                links.addAll(LinkHeaderSupport.actionLinks(uriInfo, PROJECTS_PATH, projectId));
            }
        }
        return links;
    }
}
