package facades;

import dtos.MovieDTO;
import entities.Movie;
import utils.EMF_Creator;
import entities.Role;
import entities.User;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    private User user;

    public UserFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE from Role").executeUpdate();
            em.persist(new Role("user"));
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.createQuery("DELETE from User").executeUpdate();
            em.persist(new User("Some txt", "More text"));
            user = new User("aaa", "bbb");
            Movie movie = new Movie("testMovie");
            user.addMovie(movie);
            em.persist(movie);
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method 
    @Test
    public void testGetVerifiedUser() throws AuthenticationException {
        assertEquals("aaa", facade.getVeryfiedUser("aaa", "bbb").getUserName(), "Expects correct name for a user");
    }

    @Test
    public void testRegisterUser() throws AuthenticationException {
        facade.registerUser("fiske", "juice");
        assertEquals("fiske", facade.getVeryfiedUser("fiske", "juice").getUserName(), "Expects user exist after register");
    }

    @Test
    public void testGetSavedListSize() {
        ArrayList<MovieDTO> result = facade.getSavedListByUser("aaa");
        assertEquals("testMovie", result.get(0).getTitle(), "assert that name of movie matches");
    }

    @Test
    public void testAddToSavedListMovieDoesNotExist() {
        facade.addMovieToSaved("FiskeJuiceTheMovie", "aaa");
        ArrayList<MovieDTO> result = facade.getSavedListByUser("aaa");
        assertEquals("FiskeJuiceTheMovie", result.get(1).getTitle(), "assert that name of added movie matches");
    }

    @Test
    public void testAddToSavedListMovieExist() {
        facade.addMovieToSaved("testMovie", "aaa");
        ArrayList<MovieDTO> result = facade.getSavedListByUser("aaa");
        assertEquals(1, result.size(), "assert that no movie was added, because it already exist");
    }

    @Test
    public void testRegisteredUserEmptyUsername() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            facade.registerUser("", "juice");
        });
    }

    @Test
    public void testRegisteredUserEmptyPassword() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            facade.registerUser("fiske", "");
        });
    }

    @Test
    public void testChangePassword() {
        EntityManager em = emf.createEntityManager();
        User newuser;
        String userName = "aaa";
        String newPassword = "aab";
        facade.changePassword(userName, newPassword);
        try {
            em.getTransaction().begin();
            newuser = em.find(User.class, userName);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        assertTrue(newuser.verifyPassword("aab"));
    }
}
