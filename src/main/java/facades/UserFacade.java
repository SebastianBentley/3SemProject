package facades;

import dtos.MovieDTO;
import entities.Movie;
import entities.Role;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import security.errorhandling.AuthenticationException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User registerUser(String username, String password) {

        EntityManager em = emf.createEntityManager();

        if (username.equals("") || password.equals("")) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        User user = new User(username, password);
        Role userRole = new Role("user");
        user.addRole(userRole);
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        return user;
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

    public void addMovieToSaved(String movieTitle, String userName) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> userQuery = em.createQuery("SELECT a FROM User a WHERE a.userName = :username", User.class);
            userQuery.setParameter("username", userName);
            List<User> adr2 = userQuery.getResultList();
            User usr = adr2.get(0);

            if (movieExists(movieTitle)) {
                TypedQuery<Movie> movieQuery = em.createQuery("SELECT a FROM Movie a WHERE a.title = :title", Movie.class);

                movieQuery.setParameter("title", movieTitle);
                List<Movie> adr = movieQuery.getResultList();
                Movie mov = adr.get(0);

                boolean movieExists = false;
                for (Movie m : usr.getMovieList()) {
                    if (m.getTitle().equals(mov.getTitle())) {
                        movieExists = true;
                    }
                }

                if (!movieExists) {
                    em.getTransaction().begin();
                    usr.addMovie(mov);
                    em.getTransaction().commit();
                }

            } else {
                em.getTransaction().begin();
                Movie mov = new Movie(movieTitle);
                em.persist(mov);
                usr.addMovie(mov);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }

    public ArrayList<MovieDTO> getSavedListByUser(String userName) {
        ArrayList<MovieDTO> movList = new ArrayList<MovieDTO>();
        EntityManager em = emf.createEntityManager();
        User usr;
        try {
            TypedQuery<User> userQuery = em.createQuery("SELECT a FROM User a WHERE a.userName = :username", User.class);
            userQuery.setParameter("username", userName);
            List<User> adr2 = userQuery.getResultList();
            usr = adr2.get(0);
            for (Movie m : usr.getMovieList()) {
                movList.add(new MovieDTO(m.getTitle()));
            }
        } finally {
            em.close();
        }
        return movList;
    }

    public String changePassword(String userName, String newPassword) {
        EntityManager em = emf.createEntityManager();
        User usr;
        try {
            TypedQuery<User> userQuery = em.createQuery("SELECT a FROM User a WHERE a.userName = :username", User.class);
            userQuery.setParameter("username", userName);
            List<User> adr2 = userQuery.getResultList();
            usr = adr2.get(0);

            em.getTransaction().begin();
            usr.setUserPass(newPassword);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return usr.getUserName();
    }

}
