package ir.co.sadad.controller;

import ir.co.sadad.domain.Teacher;
import ir.co.sadad.domain.User;
import ir.co.sadad.repository.TeacherRepository;
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
 * Test class for the TeacherController REST controller.
 *
 */
@RunWith(Arquillian.class)
public class TeacherControllerTest extends AbstractTest {

    private static final String DEFAULT_TEACHER_NO = "A";
    private static final String UPDATED_TEACHER_NO = "B";
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

    private static Teacher teacher;

    @Inject
    private TeacherRepository teacherRepository;

    private TeacherControllerClient client;

    @Deployment
    public static WebArchive createDeployment() {
        return buildArchive()
                .addClasses(
                        AbstractTest.class,
                        User.class,
                        Teacher.class,
                        TeacherRepository.class,
                        TeacherController.class,
                        TeacherControllerClient.class);
    }

    @Before
    public void buildClient() throws Exception {
        client = buildClient(TeacherControllerClient.class);
    }

    @Test
    @InSequence(1)
    public void createTeacher() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();

        // Create the Teacher
        teacher = new Teacher();
        teacher.setTeacherNo(DEFAULT_TEACHER_NO);
        teacher.setName(DEFAULT_NAME);
        teacher.setLastName(DEFAULT_LAST_NAME);
        teacher.setNationalCode(DEFAULT_NATIONAL_CODE);
        teacher.setMobile(DEFAULT_MOBILE);
        teacher.setEmail(DEFAULT_EMAIL);
        teacher.setAccountId(DEFAULT_ACCOUNT_ID);
        Response response = client.createTeacher(teacher);
        assertThat(response, hasStatus(CREATED));
        teacher = response.readEntity(Teacher.class);

        // Validate the Teacher in the database
        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers.size(), is(databaseSizeBeforeCreate + 1));
        Teacher testTeacher = teachers.get(teachers.size() - 1);
        assertThat(testTeacher.getTeacherNo(), is(DEFAULT_TEACHER_NO));
        assertThat(testTeacher.getName(), is(DEFAULT_NAME));
        assertThat(testTeacher.getLastName(), is(DEFAULT_LAST_NAME));
        assertThat(testTeacher.getNationalCode(), is(DEFAULT_NATIONAL_CODE));
        assertThat(testTeacher.getMobile(), is(DEFAULT_MOBILE));
        assertThat(testTeacher.getEmail(), is(DEFAULT_EMAIL));
        assertThat(testTeacher.getAccountId(), is(DEFAULT_ACCOUNT_ID));
    }

    @Test
    @InSequence(2)
    public void getAllTeachers() throws Exception {
        int databaseSize = teacherRepository.findAll().size();

        // Get all the teachers
        List<Teacher> teachers = client.getAllTeachers();
        assertThat(teachers.size(), is(databaseSize));
    }

    @Test
    @InSequence(3)
    public void getTeacher() throws Exception {
        // Get the teacher
        Response response = client.getTeacher(teacher.getId());
        Teacher testTeacher = response.readEntity(Teacher.class);
        assertThat(response, hasStatus(OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));
        assertThat(testTeacher.getId(), is(teacher.getId()));
        assertThat(testTeacher.getTeacherNo(), is(DEFAULT_TEACHER_NO));
        assertThat(testTeacher.getName(), is(DEFAULT_NAME));
        assertThat(testTeacher.getLastName(), is(DEFAULT_LAST_NAME));
        assertThat(testTeacher.getNationalCode(), is(DEFAULT_NATIONAL_CODE));
        assertThat(testTeacher.getMobile(), is(DEFAULT_MOBILE));
        assertThat(testTeacher.getEmail(), is(DEFAULT_EMAIL));
        assertThat(testTeacher.getAccountId(), is(DEFAULT_ACCOUNT_ID));
    }

    @Test
    @InSequence(4)
    public void getNonExistingTeacher() throws Exception {
        // Get the teacher
        assertWebException(NOT_FOUND, () -> client.getTeacher(3L));
    }

    @Test
    @InSequence(5)
    public void updateTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher
        Teacher updatedTeacher = new Teacher();
        updatedTeacher.setId(teacher.getId());
        updatedTeacher.setTeacherNo(UPDATED_TEACHER_NO);
        updatedTeacher.setName(UPDATED_NAME);
        updatedTeacher.setLastName(UPDATED_LAST_NAME);
        updatedTeacher.setNationalCode(UPDATED_NATIONAL_CODE);
        updatedTeacher.setMobile(UPDATED_MOBILE);
        updatedTeacher.setEmail(UPDATED_EMAIL);
        updatedTeacher.setAccountId(UPDATED_ACCOUNT_ID);

        Response response = client.updateTeacher(updatedTeacher);
        assertThat(response, hasStatus(OK));

        // Validate the Teacher in the database
        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers.size(), is(databaseSizeBeforeUpdate));
        Teacher testTeacher = teachers.get(teachers.size() - 1);
        assertThat(testTeacher.getTeacherNo(), is(UPDATED_TEACHER_NO));
        assertThat(testTeacher.getName(), is(UPDATED_NAME));
        assertThat(testTeacher.getLastName(), is(UPDATED_LAST_NAME));
        assertThat(testTeacher.getNationalCode(), is(UPDATED_NATIONAL_CODE));
        assertThat(testTeacher.getMobile(), is(UPDATED_MOBILE));
        assertThat(testTeacher.getEmail(), is(UPDATED_EMAIL));
        assertThat(testTeacher.getAccountId(), is(UPDATED_ACCOUNT_ID));
    }

    @Test
    @InSequence(6)
    public void removeTeacher() throws Exception {
        int databaseSizeBeforeDelete = teacherRepository.findAll().size();

        // Delete the teacher
        Response response = client.removeTeacher(teacher.getId());
        assertThat(response, hasStatus(OK));

        // Validate the database is empty
        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers.size(), is(databaseSizeBeforeDelete - 1));
    }

}
