package facades;

import utils.EMF_Creator;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;

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
            em.persist(new User("aaa", "bbb"));
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
        assertEquals("aaa", facade.getVeryfiedUser("aaa", "bbb").getUserName(), "Expects two rows in the database");
    }

    @Test
    public void testRegisterUser() throws AuthenticationException {
        facade.registerUser("fiske", "juice");
        assertEquals("fiske", facade.getVeryfiedUser("fiske", "juice").getUserName(), "Expects two rows in the database");
    }

    @Test
    void testRegisteredUserEmptyUsername() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            facade.registerUser("", "juice");
        });
    }

    @Test
    void testRegisteredUserEmptyPassword() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            facade.registerUser("fiske", "");
        });
    }
}