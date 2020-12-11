
package facades;

import dtos.MovieDTO;
import entities.Movie;
import errorhandling.MovieNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class MovieFacade {

    private static EntityManagerFactory emf;
    private static MovieFacade instance;

    private MovieFacade() {
    }

    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private static boolean movieExists(String movieTitle) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT a FROM Movie a WHERE a.title = :title", Movie.class);
            query.setParameter("title", movieTitle);
            List<Movie> adr = query.getResultList();
            if (adr.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } finally {
            em.close();
        }
    }

    public static void downvoteMovie(String movieTitle) {
        EntityManager em = emf.createEntityManager();
        try {
            if (movieExists(movieTitle)) {
                TypedQuery<Movie> query = em.createQuery("SELECT a FROM Movie a WHERE a.title = :title", Movie.class);
                query.setParameter("title", movieTitle);
                List<Movie> adr = query.getResultList();
                Movie mov = adr.get(0);
                em.getTransaction().begin();
                mov.downVote();
                em.getTransaction().commit();
            } else {
                em.getTransaction().begin();
                Movie mov = new Movie(movieTitle);
                em.persist(mov);
                mov.downVote();
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }

    public static void upvoteMovie(String movieTitle) {
        EntityManager em = emf.createEntityManager();
        try {
            if (movieExists(movieTitle)) {
                TypedQuery<Movie> query = em.createQuery("SELECT a FROM Movie a WHERE a.title = :title", Movie.class);
                query.setParameter("title", movieTitle);
                List<Movie> adr = query.getResultList();
                Movie mov = adr.get(0);
                em.getTransaction().begin();
                mov.upVote();
                em.getTransaction().commit();
            } else {
                em.getTransaction().begin();
                Movie mov = new Movie(movieTitle);
                em.persist(mov);
                mov.upVote();
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }

    public int getDownvotesByTitle(String title) throws MovieNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT a FROM Movie a WHERE a.title = :title", Movie.class);
            query.setParameter("title", title);
            List<Movie> adr = query.getResultList();
            if (adr.isEmpty()) {
                throw new MovieNotFoundException();
            }
            Movie mov = adr.get(0);
            return mov.getDislikes();
        } finally {
            em.close();
        }
    }

    public int getUpvotesByTitle(String title) throws MovieNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT a FROM Movie a WHERE a.title = :title", Movie.class);
            query.setParameter("title", title);
            List<Movie> adr = query.getResultList();
            if (adr.isEmpty()) {
                throw new MovieNotFoundException();
            }
            Movie mov = adr.get(0);
            return mov.getLikes();
        } finally {
            em.close();
        }
    }

    public ArrayList<MovieDTO> getTop5() {
        ArrayList<MovieDTO> movList = new ArrayList<MovieDTO>();
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> userQuery = em.createQuery("SELECT a FROM Movie a ORDER BY a.likes DESC", Movie.class);
            List<Movie> movs = userQuery.setMaxResults(5).getResultList();
            for (Movie m : movs) {
                movList.add(new MovieDTO(m.getTitle()));
            }
        } finally {
            em.close();
        }
        return movList;
    }

}
