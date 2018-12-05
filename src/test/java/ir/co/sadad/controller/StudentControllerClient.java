package ir.co.sadad.controller;

import ir.co.sadad.domain.Student;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RegisterRestClient
@Path("/api/student")
public interface StudentControllerClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStudent(Student student);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(Student student);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getAllStudents();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getStudent(@PathParam("id") Long id);

    @DELETE
    @Path("/{id}")
    public Response removeStudent(@PathParam("id") Long id);

}
