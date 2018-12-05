package ir.co.sadad.controller;

import ir.co.sadad.domain.Student;
import ir.co.sadad.domain.User;
import ir.co.sadad.repository.StudentRepository;
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
 * Test class for the StudentController REST controller.
 *
 */
@RunWith(Arquillian.class)
public class StudentControllerTest extends AbstractTest {

    private static final String DEFAULT_STUDENT_NO = "A";
    private static final String UPDATED_STUDENT_NO = "B";
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

    private static Student student;

    @Inject
    private StudentRepository studentRepository;

    private StudentControllerClient client;

    @Deployment
    public static WebArchive createDeployment() {
        return buildArchive()
                .addClasses(
                        AbstractTest.class,
                        User.class,
                        Student.class,
                        StudentRepository.class,
                        StudentController.class,
                        StudentControllerClient.class);
    }

    @Before
    public void buildClient() throws Exception {
        client = buildClient(StudentControllerClient.class);
    }

    @Test
    @InSequence(1)
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student
        student = new Student();
        student.setStudentNo(DEFAULT_STUDENT_NO);
        student.setName(DEFAULT_NAME);
        student.setLastName(DEFAULT_LAST_NAME);
        student.setNationalCode(DEFAULT_NATIONAL_CODE);
        student.setMobile(DEFAULT_MOBILE);
        student.setEmail(DEFAULT_EMAIL);
        student.setAccountId(DEFAULT_ACCOUNT_ID);
        Response response = client.createStudent(student);
        assertThat(response, hasStatus(CREATED));
        student = response.readEntity(Student.class);

        // Validate the Student in the database
        List<Student> students = studentRepository.findAll();
        assertThat(students.size(), is(databaseSizeBeforeCreate + 1));
        Student testStudent = students.get(students.size() - 1);
        assertThat(testStudent.getStudentNo(), is(DEFAULT_STUDENT_NO));
        assertThat(testStudent.getName(), is(DEFAULT_NAME));
        assertThat(testStudent.getLastName(), is(DEFAULT_LAST_NAME));
        assertThat(testStudent.getNationalCode(), is(DEFAULT_NATIONAL_CODE));
        assertThat(testStudent.getMobile(), is(DEFAULT_MOBILE));
        assertThat(testStudent.getEmail(), is(DEFAULT_EMAIL));
        assertThat(testStudent.getAccountId(), is(DEFAULT_ACCOUNT_ID));
    }

    @Test
    @InSequence(2)
    public void getAllStudents() throws Exception {
        int databaseSize = studentRepository.findAll().size();

        // Get all the students
        List<Student> students = client.getAllStudents();
        assertThat(students.size(), is(databaseSize));
    }

    @Test
    @InSequence(3)
    public void getStudent() throws Exception {
        // Get the student
        Response response = client.getStudent(student.getId());
        Student testStudent = response.readEntity(Student.class);
        assertThat(response, hasStatus(OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));
        assertThat(testStudent.getId(), is(student.getId()));
        assertThat(testStudent.getStudentNo(), is(DEFAULT_STUDENT_NO));
        assertThat(testStudent.getName(), is(DEFAULT_NAME));
        assertThat(testStudent.getLastName(), is(DEFAULT_LAST_NAME));
        assertThat(testStudent.getNationalCode(), is(DEFAULT_NATIONAL_CODE));
        assertThat(testStudent.getMobile(), is(DEFAULT_MOBILE));
        assertThat(testStudent.getEmail(), is(DEFAULT_EMAIL));
        assertThat(testStudent.getAccountId(), is(DEFAULT_ACCOUNT_ID));
    }

    @Test
    @InSequence(4)
    public void getNonExistingStudent() throws Exception {
        // Get the student
        assertWebException(NOT_FOUND, () -> client.getStudent(3L));
    }

    @Test
    @InSequence(5)
    public void updateStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = new Student();
        updatedStudent.setId(student.getId());
        updatedStudent.setStudentNo(UPDATED_STUDENT_NO);
        updatedStudent.setName(UPDATED_NAME);
        updatedStudent.setLastName(UPDATED_LAST_NAME);
        updatedStudent.setNationalCode(UPDATED_NATIONAL_CODE);
        updatedStudent.setMobile(UPDATED_MOBILE);
        updatedStudent.setEmail(UPDATED_EMAIL);
        updatedStudent.setAccountId(UPDATED_ACCOUNT_ID);

        Response response = client.updateStudent(updatedStudent);
        assertThat(response, hasStatus(OK));

        // Validate the Student in the database
        List<Student> students = studentRepository.findAll();
        assertThat(students.size(), is(databaseSizeBeforeUpdate));
        Student testStudent = students.get(students.size() - 1);
        assertThat(testStudent.getStudentNo(), is(UPDATED_STUDENT_NO));
        assertThat(testStudent.getName(), is(UPDATED_NAME));
        assertThat(testStudent.getLastName(), is(UPDATED_LAST_NAME));
        assertThat(testStudent.getNationalCode(), is(UPDATED_NATIONAL_CODE));
        assertThat(testStudent.getMobile(), is(UPDATED_MOBILE));
        assertThat(testStudent.getEmail(), is(UPDATED_EMAIL));
        assertThat(testStudent.getAccountId(), is(UPDATED_ACCOUNT_ID));
    }

    @Test
    @InSequence(6)
    public void removeStudent() throws Exception {
        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        Response response = client.removeStudent(student.getId());
        assertThat(response, hasStatus(OK));

        // Validate the database is empty
        List<Student> students = studentRepository.findAll();
        assertThat(students.size(), is(databaseSizeBeforeDelete - 1));
    }

}
