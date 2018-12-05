package ir.co.sadad.controller;

import ir.co.sadad.domain.Student;
import ir.co.sadad.repository.StudentRepository;
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
 * REST controller for managing Student.
 */
@Path("/api/student")
@RolesAllowed(USER)
public class StudentController {

    @Inject
    private Logger log;

    @Inject
    private StudentRepository studentRepository;

    private static final String ENTITY_NAME = "userServiceStudent";

    /**
     * POST : Create a new student.
     *
     * @param student the student to create
     * @return the Response with status 201 (Created) and with body the new
     * student, or with status 400 (Bad Request) if the student has already an
     * ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Timed
    @Operation(summary = "create a new student", description = "Create a new student")
    @APIResponse(responseCode = "201", description = "Created")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStudent(Student student) throws URISyntaxException {
        log.debug("REST request to save Student : {}", student);
        studentRepository.create(student);
        return HeaderUtil.createEntityCreationAlert(Response.created(new URI("/resources/api/student/" + student.getId())),
                ENTITY_NAME, student.getId().toString())
                .entity(student).build();
    }

    /**
     * PUT : Updates an existing student.
     *
     * @param student the student to update
     * @return the Response with status 200 (OK) and with body the updated
     * student, or with status 400 (Bad Request) if the student is not valid, or
     * with status 500 (Internal Server Error) if the student couldn't be
     * updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Timed
    @Operation(summary = "update student", description = "Updates an existing student")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @APIResponse(responseCode = "500", description = "Internal Server Error")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(Student student) throws URISyntaxException {
        log.debug("REST request to update Student : {}", student);
        studentRepository.edit(student);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), ENTITY_NAME, student.getId().toString())
                .entity(student).build();
    }

    /**
     * GET : get all the students.
     *
     * @return the Response with status 200 (OK) and the list of students in
     * body
     *
     */
    @Timed
    @Operation(summary = "get all the students")
    @APIResponse(responseCode = "200", description = "OK")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout
    public List<Student> getAllStudents() {
        log.debug("REST request to get all Students");
        List<Student> students = studentRepository.findAll();
        return students;
    }

    /**
     * GET /:id : get the "id" student.
     *
     * @param id the id of the student to retrieve
     * @return the Response with status 200 (OK) and with body the student, or
     * with status 404 (Not Found)
     */
    @Timed
    @Operation(summary = "get the student")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "Not Found")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") Long id) {
        log.debug("REST request to get Student : {}", id);
        Student student = studentRepository.find(id);
        return Optional.ofNullable(student)
                .map(result -> Response.status(Response.Status.OK).entity(student).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * DELETE /:id : remove the "id" student.
     *
     * @param id the id of the student to delete
     * @return the Response with status 200 (OK)
     */
    @Timed
    @Operation(summary = "remove the student")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "Not Found")
    @DELETE
    @Path("/{id}")
    public Response removeStudent(@PathParam("id") Long id) {
        log.debug("REST request to delete Student : {}", id);
        studentRepository.remove(studentRepository.find(id));
        return HeaderUtil.createEntityDeletionAlert(Response.ok(), ENTITY_NAME, id.toString()).build();
    }

}
