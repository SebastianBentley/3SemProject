package facades;

import entities.Movie;
import entities.Role;
import entities.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

public class MovieFacadeTest {

    private static EntityManagerFactory emf;
    private static MovieFacade facade;

    public MovieFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = MovieFacade.getMovieFacade(emf);
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
        em.getTransaction().begin();
        em.createQuery("DELETE from Movie").executeUpdate();
        em.getTransaction().commit();
        em.getTransaction().begin();
        Movie mov = new Movie("morfar");
        Movie mov2 = new Movie("SÅ GIV OS DOG EN GOD EDITOR FFS nummer to");
        em.persist(mov);
        em.persist(mov2);
        em.getTransaction().commit();

    }

    @Test
    public void testUpvoteExistingMovie() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        facade.upvoteMovie("morfar");
        TypedQuery<Movie> query = em.createQuery("SELECT a FROM Movie a WHERE a.title = :title", Movie.class);
        query.setParameter("title", "morfar");
        List<Movie> adr = query.getResultList();
        Movie mov = adr.get(0);
        em.getTransaction().commit();
        assertEquals(1, mov.getLikes());
    }

    @Test
    public void testDownvoteExistingMovie() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        facade.downvoteMovie("morfar");
        TypedQuery<Movie> query = em.createQuery("SELECT a FROM Movie a WHERE a.title = :title", Movie.class);
        query.setParameter("title", "morfar");
        List<Movie> adr = query.getResultList();
        Movie mov = adr.get(0);
        em.getTransaction().commit();
        assertEquals(1, mov.getDislikes());
    }

    @Test
    public void testUpvoteNewMovie() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        facade.upvoteMovie("testtingenLOL");
        TypedQuery<Movie> query = em.createQuery("SELECT a FROM Movie a", Movie.class);
        List<Movie> adr = query.getResultList();
        assertEquals(3, adr.size());
        assertEquals(1, adr.get(2).getLikes());
    }

    @Test
    public void testDownvoteNewMovie() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        facade.downvoteMovie("juicekartoffel");
        TypedQuery<Movie> query = em.createQuery("SELECT a FROM Movie a", Movie.class);
        List<Movie> adr = query.getResultList();
        assertEquals(3, adr.size());
        assertEquals(1, adr.get(2).getDislikes());
    }

}
