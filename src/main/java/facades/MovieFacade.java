/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Movie;
import errorhandling.MovieNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Tas
 */
public class MovieFacade {

    private static EntityManagerFactory emf;
    private static MovieFacade instance;

    private MovieFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
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

}
