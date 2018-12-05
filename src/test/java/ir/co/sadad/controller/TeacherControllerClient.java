package ir.co.sadad.controller;

import ir.co.sadad.domain.Teacher;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RegisterRestClient
@Path("/api/teacher")
public interface TeacherControllerClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTeacher(Teacher teacher);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTeacher(Teacher teacher);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Teacher> getAllTeachers();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getTeacher(@PathParam("id") Long id);

    @DELETE
    @Path("/{id}")
    public Response removeTeacher(@PathParam("id") Long id);

}
