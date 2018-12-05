package ir.co.sadad.controller;

import ir.co.sadad.domain.User;
import ir.co.sadad.repository.UserRepository;
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
 * Test class for the UserController REST controller.
 *
 */
@RunWith(Arquillian.class)
public class UserControllerTest extends AbstractTest {

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

    private static User user;

    @Inject
    private UserRepository userRepository;

    private UserControllerClient client;

    @Deployment
    public static WebArchive createDeployment() {
        return buildArchive()
                .addClasses(
                        AbstractTest.class,
                        User.class,
                        UserRepository.class,
                        UserController.class,
                        UserControllerClient.class);
    }

    @Before
    public void buildClient() throws Exception {
        client = buildClient(UserControllerClient.class);
    }

    @Test
    @InSequence(1)
    public void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User
        user = new User();
        user.setName(DEFAULT_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setNationalCode(DEFAULT_NATIONAL_CODE);
        user.setMobile(DEFAULT_MOBILE);
        user.setEmail(DEFAULT_EMAIL);
        user.setAccountId(DEFAULT_ACCOUNT_ID);
        Response response = client.createUser(user);
        assertThat(response, hasStatus(CREATED));
        user = response.readEntity(User.class);

        // Validate the User in the database
        List<User> users = userRepository.findAll();
        assertThat(users.size(), is(databaseSizeBeforeCreate + 1));
        User testUser = users.get(users.size() - 1);
        assertThat(testUser.getName(), is(DEFAULT_NAME));
        assertThat(testUser.getLastName(), is(DEFAULT_LAST_NAME));
        assertThat(testUser.getNationalCode(), is(DEFAULT_NATIONAL_CODE));
        assertThat(testUser.getMobile(), is(DEFAULT_MOBILE));
        assertThat(testUser.getEmail(), is(DEFAULT_EMAIL));
        assertThat(testUser.getAccountId(), is(DEFAULT_ACCOUNT_ID));
    }

    @Test
    @InSequence(2)
    public void getAllUsers() throws Exception {
        int databaseSize = userRepository.findAll().size();

        // Get all the users
        List<User> users = client.getAllUsers();
        assertThat(users.size(), is(databaseSize));
    }

    @Test
    @InSequence(3)
    public void getUser() throws Exception {
        // Get the user
        Response response = client.getUser(user.getId());
        User testUser = response.readEntity(User.class);
        assertThat(response, hasStatus(OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));
        assertThat(testUser.getId(), is(user.getId()));
        assertThat(testUser.getName(), is(DEFAULT_NAME));
        assertThat(testUser.getLastName(), is(DEFAULT_LAST_NAME));
        assertThat(testUser.getNationalCode(), is(DEFAULT_NATIONAL_CODE));
        assertThat(testUser.getMobile(), is(DEFAULT_MOBILE));
        assertThat(testUser.getEmail(), is(DEFAULT_EMAIL));
        assertThat(testUser.getAccountId(), is(DEFAULT_ACCOUNT_ID));
    }

    @Test
    @InSequence(4)
    public void getNonExistingUser() throws Exception {
        // Get the user
        assertWebException(NOT_FOUND, () -> client.getUser(3L));
    }

    @Test
    @InSequence(5)
    public void updateUser() throws Exception {
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setName(UPDATED_NAME);
        updatedUser.setLastName(UPDATED_LAST_NAME);
        updatedUser.setNationalCode(UPDATED_NATIONAL_CODE);
        updatedUser.setMobile(UPDATED_MOBILE);
        updatedUser.setEmail(UPDATED_EMAIL);
        updatedUser.setAccountId(UPDATED_ACCOUNT_ID);

        Response response = client.updateUser(updatedUser);
        assertThat(response, hasStatus(OK));

        // Validate the User in the database
        List<User> users = userRepository.findAll();
        assertThat(users.size(), is(databaseSizeBeforeUpdate));
        User testUser = users.get(users.size() - 1);
        assertThat(testUser.getName(), is(UPDATED_NAME));
        assertThat(testUser.getLastName(), is(UPDATED_LAST_NAME));
        assertThat(testUser.getNationalCode(), is(UPDATED_NATIONAL_CODE));
        assertThat(testUser.getMobile(), is(UPDATED_MOBILE));
        assertThat(testUser.getEmail(), is(UPDATED_EMAIL));
        assertThat(testUser.getAccountId(), is(UPDATED_ACCOUNT_ID));
    }

    @Test
    @InSequence(6)
    public void removeUser() throws Exception {
        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the user
        Response response = client.removeUser(user.getId());
        assertThat(response, hasStatus(OK));

        // Validate the database is empty
        List<User> users = userRepository.findAll();
        assertThat(users.size(), is(databaseSizeBeforeDelete - 1));
    }

}
