package ir.co.sadad.controller;

import ir.co.sadad.domain.User;
import ir.co.sadad.repository.UserRepository;
import ir.co.sadad.controller.util.HeaderUtil;
import static ir.co.sadad.security.AuthoritiesConstants.USER;
import org.slf4j.Logger;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

/**
 * REST controller for managing User.
 */
@Path("/api/user")
@RolesAllowed(USER)
public class UserController {

    @Inject
    private Logger log;

    @Inject
    private UserRepository userRepository;

    private static final String ENTITY_NAME = "userServiceUser";

    /**
     * POST : Create a new user.
     *
     * @param user the user to create
     * @return the Response with status 201 (Created) and with body the new
     * user, or with status 400 (Bad Request) if the user has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Timed
    @Operation(summary = "create a new user", description = "Create a new user")
    @APIResponse(responseCode = "201", description = "Created")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) throws URISyntaxException {
        log.debug("REST request to save User : {}", user);
        userRepository.create(user);
        return HeaderUtil.createEntityCreationAlert(Response.created(new URI("/resources/api/user/" + user.getId())),
                ENTITY_NAME, user.getId().toString())
                .entity(user).build();
    }

    /**
     * PUT : Updates an existing user.
     *
     * @param user the user to update
     * @return the Response with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the user is not valid, or with status
     * 500 (Internal Server Error) if the user couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Timed
    @Operation(summary = "update user", description = "Updates an existing user")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @APIResponse(responseCode = "500", description = "Internal Server Error")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(User user) throws URISyntaxException {
        log.debug("REST request to update User : {}", user);
        userRepository.edit(user);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), ENTITY_NAME, user.getId().toString())
                .entity(user).build();
    }

    /**
     * GET : get all the users.
     *
     * @return the Response with status 200 (OK) and the list of users in body
     *
     */
    @Timed
    @Operation(summary = "get all the users")
    @APIResponse(responseCode = "200", description = "OK")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout
    public List<User> getAllUsers() {
        log.debug("REST request to get all Users");
        List<User> users = userRepository.findAll();
        return users;
    }

    /**
     * GET /:id : get the "id" user.
     *
     * @param id the id of the user to retrieve
     * @return the Response with status 200 (OK) and with body the user, or with
     * status 404 (Not Found)
     */
    @Timed
    @Operation(summary = "get the user")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "Not Found")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Long id) {
        log.debug("REST request to get User : {}", id);
        User user = userRepository.find(id);
        return Optional.ofNullable(user)
                .map(result -> Response.status(Response.Status.OK).entity(user).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * DELETE /:id : remove the "id" user.
     *
     * @param id the id of the user to delete
     * @return the Response with status 200 (OK)
     */
    @Timed
    @Operation(summary = "remove the user")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "Not Found")
    @DELETE
    @Path("/{id}")
    public Response removeUser(@PathParam("id") Long id) {
        log.debug("REST request to delete User : {}", id);
        userRepository.remove(userRepository.find(id));
        return HeaderUtil.createEntityDeletionAlert(Response.ok(), ENTITY_NAME, id.toString()).build();
    }

}
