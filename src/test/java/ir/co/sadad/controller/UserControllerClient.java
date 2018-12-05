package ir.co.sadad.controller;

import ir.co.sadad.domain.User;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RegisterRestClient
@Path("/api/user")
public interface UserControllerClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(User user);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id);

    @DELETE
    @Path("/{id}")
    public Response removeUser(@PathParam("id") Long id);

}
