package ir.co.sadad.controller;

import ir.co.sadad.domain.Teacher;
import ir.co.sadad.repository.TeacherRepository;
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
 * REST controller for managing Teacher.
 */
@Path("/api/teacher")
@RolesAllowed(USER)
public class TeacherController {

    @Inject
    private Logger log;

    @Inject
    private TeacherRepository teacherRepository;

    private static final String ENTITY_NAME = "userServiceTeacher";

    /**
     * POST : Create a new teacher.
     *
     * @param teacher the teacher to create
     * @return the Response with status 201 (Created) and with body the new
     * teacher, or with status 400 (Bad Request) if the teacher has already an
     * ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Timed
    @Operation(summary = "create a new teacher", description = "Create a new teacher")
    @APIResponse(responseCode = "201", description = "Created")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTeacher(Teacher teacher) throws URISyntaxException {
        log.debug("REST request to save Teacher : {}", teacher);
        teacherRepository.create(teacher);
        return HeaderUtil.createEntityCreationAlert(Response.created(new URI("/resources/api/teacher/" + teacher.getId())),
                ENTITY_NAME, teacher.getId().toString())
                .entity(teacher).build();
    }

    /**
     * PUT : Updates an existing teacher.
     *
     * @param teacher the teacher to update
     * @return the Response with status 200 (OK) and with body the updated
     * teacher, or with status 400 (Bad Request) if the teacher is not valid, or
     * with status 500 (Internal Server Error) if the teacher couldn't be
     * updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Timed
    @Operation(summary = "update teacher", description = "Updates an existing teacher")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @APIResponse(responseCode = "500", description = "Internal Server Error")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTeacher(Teacher teacher) throws URISyntaxException {
        log.debug("REST request to update Teacher : {}", teacher);
        teacherRepository.edit(teacher);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), ENTITY_NAME, teacher.getId().toString())
                .entity(teacher).build();
    }

    /**
     * GET : get all the teachers.
     *
     * @return the Response with status 200 (OK) and the list of teachers in
     * body
     *
     */
    @Timed
    @Operation(summary = "get all the teachers")
    @APIResponse(responseCode = "200", description = "OK")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout
    public List<Teacher> getAllTeachers() {
        log.debug("REST request to get all Teachers");
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers;
    }

    /**
     * GET /:id : get the "id" teacher.
     *
     * @param id the id of the teacher to retrieve
     * @return the Response with status 200 (OK) and with body the teacher, or
     * with status 404 (Not Found)
     */
    @Timed
    @Operation(summary = "get the teacher")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "Not Found")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeacher(@PathParam("id") Long id) {
        log.debug("REST request to get Teacher : {}", id);
        Teacher teacher = teacherRepository.find(id);
        return Optional.ofNullable(teacher)
                .map(result -> Response.status(Response.Status.OK).entity(teacher).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * DELETE /:id : remove the "id" teacher.
     *
     * @param id the id of the teacher to delete
     * @return the Response with status 200 (OK)
     */
    @Timed
    @Operation(summary = "remove the teacher")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "Not Found")
    @DELETE
    @Path("/{id}")
    public Response removeTeacher(@PathParam("id") Long id) {
        log.debug("REST request to delete Teacher : {}", id);
        teacherRepository.remove(teacherRepository.find(id));
        return HeaderUtil.createEntityDeletionAlert(Response.ok(), ENTITY_NAME, id.toString()).build();
    }

}
