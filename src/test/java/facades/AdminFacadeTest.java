package facades;

import dtos.UserDTO;
import entities.Movie;
import entities.Role;
import entities.User;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
public class AdminFacadeTest {

    private static EntityManagerFactory emf;
    private static AdminFacade facade;
    private User userAdmin;
    private User userUser;

    public AdminFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = AdminFacade.getAdminFacade(emf);
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
            Role adminRole = new Role("admin");
            Role userRole = new Role("user");
            em.persist(userRole);
            em.persist(adminRole);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.createQuery("DELETE from User").executeUpdate();
            userUser = new User("user", "userpassword");
            userUser.addRole(userRole);
            userAdmin = new User("admin", "adminpassword");
            userAdmin.addRole(adminRole);
            em.persist(userAdmin);
            em.persist(userUser);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void getAllUsersSize() {
        ArrayList<UserDTO> list = facade.getAllUsers();
        assertEquals(1, list.size());
    }

    @Test
    public void getAllUsersNameUser() {
        ArrayList<UserDTO> list = facade.getAllUsers();
        UserDTO user = list.get(0);
        assertEquals("user", user.getUsername());
    }
    
    @Test
    public void deleteUser() {
        int userSize = facade.getAllUsers().size();
        facade.deleteUser("user");
        int newUserSize = facade.getAllUsers().size();
        assertEquals(userSize-1, newUserSize);
    }
     
}