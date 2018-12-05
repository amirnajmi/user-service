package ir.co.sadad.controller;

import ir.co.sadad.domain.Employee;
import ir.co.sadad.repository.EmployeeRepository;
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
 * REST controller for managing Employee.
 */
@Path("/api/employee")
//@RolesAllowed(USER)
public class EmployeeController {

    @Inject
    private Logger log;

    @Inject
    private EmployeeRepository employeeRepository;

    private static final String ENTITY_NAME = "userServiceEmployee";

    /**
     * POST : Create a new employee.
     *
     * @param employee the employee to create
     * @return the Response with status 201 (Created) and with body the new
     * employee, or with status 400 (Bad Request) if the employee has already an
     * ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Timed
    @Operation(summary = "create a new employee", description = "Create a new employee")
    @APIResponse(responseCode = "201", description = "Created")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEmployee(Employee employee) throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employee);
        employeeRepository.create(employee);
        return HeaderUtil.createEntityCreationAlert(Response.created(new URI("/resources/api/employee/" + employee.getId())),
                ENTITY_NAME, employee.getId().toString())
                .entity(employee).build();
    }

    /**
     * PUT : Updates an existing employee.
     *
     * @param employee the employee to update
     * @return the Response with status 200 (OK) and with body the updated
     * employee, or with status 400 (Bad Request) if the employee is not valid,
     * or with status 500 (Internal Server Error) if the employee couldn't be
     * updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Timed
    @Operation(summary = "update employee", description = "Updates an existing employee")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @APIResponse(responseCode = "500", description = "Internal Server Error")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmployee(Employee employee) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", employee);
        employeeRepository.edit(employee);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), ENTITY_NAME, employee.getId().toString())
                .entity(employee).build();
    }

    /**
     * GET : get all the employees.
     *
     * @return the Response with status 200 (OK) and the list of employees in
     * body
     *
     */
    @Timed
    @Operation(summary = "get all the employees")
    @APIResponse(responseCode = "200", description = "OK")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout
    public List<Employee> getAllEmployees() {
        log.debug("REST request to get all Employees");
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    /**
     * GET /:id : get the "id" employee.
     *
     * @param id the id of the employee to retrieve
     * @return the Response with status 200 (OK) and with body the employee, or
     * with status 404 (Not Found)
     */
    @Timed
    @Operation(summary = "get the employee")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "Not Found")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployee(@PathParam("id") Long id) {
        log.debug("REST request to get Employee : {}", id);
        Employee employee = employeeRepository.find(id);
        return Optional.ofNullable(employee)
                .map(result -> Response.status(Response.Status.OK).entity(employee).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * DELETE /:id : remove the "id" employee.
     *
     * @param id the id of the employee to delete
     * @return the Response with status 200 (OK)
     */
    @Timed
    @Operation(summary = "remove the employee")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "Not Found")
    @DELETE
    @Path("/{id}")
    public Response removeEmployee(@PathParam("id") Long id) {
        log.debug("REST request to delete Employee : {}", id);
        employeeRepository.remove(employeeRepository.find(id));
        return HeaderUtil.createEntityDeletionAlert(Response.ok(), ENTITY_NAME, id.toString()).build();
    }

}
