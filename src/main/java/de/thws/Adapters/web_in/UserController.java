package de.thws.Adapters.web_in;

import de.thws.Adapters.web_in.dto.UserCreateRequest;
import de.thws.Adapters.web_in.dto.UserResponse;
import de.thws.Adapters.web_in.dto.UserUpdateRequest;
import de.thws.Application.Domain.DomainModels.User;
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

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private static final String USERS_PATH = "users";

    @Inject
    UserUseCase userUseCase;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{id}")
    @CacheResult(cacheName = "userById")
    public Response getById(@PathParam("id") Long id) {
        User user = userUseCase.findById(id).orElseThrow(NotFoundException::new);
        UserResponse response = ResponseMapper.toUserResponse(user);
        List<Link> links = LinkHeaderSupport.resourceLinks(uriInfo, USERS_PATH, user.getUserId());
        return Response.ok(response).links(links.toArray(new Link[0])).build();
    }

    @GET
    @CacheResult(cacheName = "userQuery")
    public Response getByQuery(@QueryParam("username") String username, @QueryParam("email") String email) {
        User user;
        if (username != null) {
            user = userUseCase.findByUsername(username).orElseThrow(NotFoundException::new);
        } else if (email != null) {
            user = userUseCase.findByEmail(email).orElseThrow(NotFoundException::new);
        } else {
            throw new NotFoundException();
        }
        UserResponse response = ResponseMapper.toUserResponse(user);
        List<Link> links = LinkHeaderSupport.resourceLinks(uriInfo, USERS_PATH, user.getUserId());
        return Response.ok(response).links(links.toArray(new Link[0])).build();
    }

    @POST
    public UserResponse create(UserCreateRequest request) {
        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());
        return ResponseMapper.toUserResponse(userUseCase.create(user));
    }

    @PUT
    @Path("/{id}")
    public UserResponse update(@PathParam("id") Long id, UserUpdateRequest request) {
        User user = userUseCase.findById(id).orElseThrow(NotFoundException::new);
        if (request.getUsername() != null) {
            user.changeUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            user.changeEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.changePassword(request.getPassword());
        }
        return ResponseMapper.toUserResponse(userUseCase.update(user));
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        userUseCase.delete(id);
    }
}
