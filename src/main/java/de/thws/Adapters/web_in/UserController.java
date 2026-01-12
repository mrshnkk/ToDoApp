package de.thws.Adapters.web_in;

import de.thws.Application.Domain.DomainModels.User;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserUseCase userUseCase;

    @GET
    @Path("/{id}")
    public User getById(@PathParam("id") Long id) {
        return userUseCase.findById(id).orElseThrow(NotFoundException::new);
    }

    @GET
    public User getByQuery(@QueryParam("username") String username, @QueryParam("email") String email) {
        if (username != null) {
            return userUseCase.findByUsername(username).orElseThrow(NotFoundException::new);
        }
        if (email != null) {
            return userUseCase.findByEmail(email).orElseThrow(NotFoundException::new);
        }
        throw new NotFoundException();
    }

    @POST
    public User create(User user) {
        return userUseCase.create(user);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        userUseCase.delete(id);
    }
}
