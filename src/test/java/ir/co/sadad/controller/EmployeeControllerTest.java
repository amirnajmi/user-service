package ir.co.sadad.controller;

import ir.co.sadad.domain.Employee;
import ir.co.sadad.domain.User;
import ir.co.sadad.repository.EmployeeRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.valid4j.matchers.http.HttpResponseMatchers.hasContentType;
import static org.valid4j.matchers.http.HttpResponseMatchers.hasStatus;

/**
 * Test class for the EmployeeController REST controller.
 *
 */
@RunWith(Arquillian.class)
public class EmployeeControllerTest extends AbstractTest {

    private static final String DEFAULT_EMPLOYEE_NO = "A";
    private static final String UPDATED_EMPLOYEE_NO = "B";
    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";
    private static final String DEFAULT_LAST_NAME = "A";
    private static final String UPDATED_LAST_NAME = "B";
    private static final String DEFAULT_NATIONAL_CODE = "A";
    private static final String UPDATED_NATIONAL_CODE = "B";
    private static final String DEFAULT_MOBILE = "A";
    private static final String UPDATED_MOBILE = "B";
    private static final String DEFAULT_EMAIL = "A";
    private static final String UPDATED_EMAIL = "B";
    private static final String DEFAULT_ACCOUNT_ID = "A";
    private static final String UPDATED_ACCOUNT_ID = "B";

    private static Employee employee;

    @Inject
    private EmployeeRepository employeeRepository;

    private EmployeeControllerClient client;

    @Deployment
    public static WebArchive createDeployment() {
        return buildArchive()
                .addClasses(
                        AbstractTest.class,
                        User.class,
                        Employee.class,
                        EmployeeRepository.class,
                        EmployeeController.class,
                        EmployeeControllerClient.class);
    }

    @Before
    public void buildClient() throws Exception {
        client = buildClient(EmployeeControllerClient.class);
    }

    @Test
    @InSequence(1)
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee
        employee = new Employee();
        employee.setEmployeeNo(DEFAULT_EMPLOYEE_NO);
        employee.setName(DEFAULT_NAME);
        employee.setLastName(DEFAULT_LAST_NAME);
        employee.setNationalCode(DEFAULT_NATIONAL_CODE);
        employee.setMobile(DEFAULT_MOBILE);
        employee.setEmail(DEFAULT_EMAIL);
        employee.setAccountId(DEFAULT_ACCOUNT_ID);
        Response response = client.createEmployee(employee);
        assertThat(response, hasStatus(CREATED));
        employee = response.readEntity(Employee.class);

        // Validate the Employee in the database
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees.size(), is(databaseSizeBeforeCreate + 1));
        Employee testEmployee = employees.get(employees.size() - 1);
        assertThat(testEmployee.getEmployeeNo(), is(DEFAULT_EMPLOYEE_NO));
        assertThat(testEmployee.getName(), is(DEFAULT_NAME));
        assertThat(testEmployee.getLastName(), is(DEFAULT_LAST_NAME));
        assertThat(testEmployee.getNationalCode(), is(DEFAULT_NATIONAL_CODE));
        assertThat(testEmployee.getMobile(), is(DEFAULT_MOBILE));
        assertThat(testEmployee.getEmail(), is(DEFAULT_EMAIL));
        assertThat(testEmployee.getAccountId(), is(DEFAULT_ACCOUNT_ID));
    }

    @Test
    @InSequence(2)
    public void getAllEmployees() throws Exception {
        int databaseSize = employeeRepository.findAll().size();

        // Get all the employees
        List<Employee> employees = client.getAllEmployees();
        assertThat(employees.size(), is(databaseSize));
    }

    @Test
    @InSequence(3)
    public void getEmployee() throws Exception {
        // Get the employee
        Response response = client.getEmployee(employee.getId());
        Employee testEmployee = response.readEntity(Employee.class);
        assertThat(response, hasStatus(OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));
        assertThat(testEmployee.getId(), is(employee.getId()));
        assertThat(testEmployee.getEmployeeNo(), is(DEFAULT_EMPLOYEE_NO));
        assertThat(testEmployee.getName(), is(DEFAULT_NAME));
        assertThat(testEmployee.getLastName(), is(DEFAULT_LAST_NAME));
        assertThat(testEmployee.getNationalCode(), is(DEFAULT_NATIONAL_CODE));
        assertThat(testEmployee.getMobile(), is(DEFAULT_MOBILE));
        assertThat(testEmployee.getEmail(), is(DEFAULT_EMAIL));
        assertThat(testEmployee.getAccountId(), is(DEFAULT_ACCOUNT_ID));
    }

    @Test
    @InSequence(4)
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        assertWebException(NOT_FOUND, () -> client.getEmployee(3L));
    }

    @Test
    @InSequence(5)
    public void updateEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(employee.getId());
        updatedEmployee.setEmployeeNo(UPDATED_EMPLOYEE_NO);
        updatedEmployee.setName(UPDATED_NAME);
        updatedEmployee.setLastName(UPDATED_LAST_NAME);
        updatedEmployee.setNationalCode(UPDATED_NATIONAL_CODE);
        updatedEmployee.setMobile(UPDATED_MOBILE);
        updatedEmployee.setEmail(UPDATED_EMAIL);
        updatedEmployee.setAccountId(UPDATED_ACCOUNT_ID);

        Response response = client.updateEmployee(updatedEmployee);
        assertThat(response, hasStatus(OK));

        // Validate the Employee in the database
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees.size(), is(databaseSizeBeforeUpdate));
        Employee testEmployee = employees.get(employees.size() - 1);
        assertThat(testEmployee.getEmployeeNo(), is(UPDATED_EMPLOYEE_NO));
        assertThat(testEmployee.getName(), is(UPDATED_NAME));
        assertThat(testEmployee.getLastName(), is(UPDATED_LAST_NAME));
        assertThat(testEmployee.getNationalCode(), is(UPDATED_NATIONAL_CODE));
        assertThat(testEmployee.getMobile(), is(UPDATED_MOBILE));
        assertThat(testEmployee.getEmail(), is(UPDATED_EMAIL));
        assertThat(testEmployee.getAccountId(), is(UPDATED_ACCOUNT_ID));
    }

    @Test
    @InSequence(6)
    public void removeEmployee() throws Exception {
        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        Response response = client.removeEmployee(employee.getId());
        assertThat(response, hasStatus(OK));

        // Validate the database is empty
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees.size(), is(databaseSizeBeforeDelete - 1));
    }

}
