package ir.co.sadad.controller;

import ir.co.sadad.domain.Employee;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RegisterRestClient
@Path("/api/employee")
public interface EmployeeControllerClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEmployee(Employee employee);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmployee(Employee employee);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> getAllEmployees();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getEmployee(@PathParam("id") Long id);

    @DELETE
    @Path("/{id}")
    public Response removeEmployee(@PathParam("id") Long id);

}
