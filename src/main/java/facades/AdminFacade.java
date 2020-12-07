package facades;

import dtos.MovieDTO;
import dtos.UserDTO;
import entities.Movie;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import security.errorhandling.AuthenticationException;

public class AdminFacade {
    
    private static EntityManagerFactory emf;
    private static AdminFacade instance;

    private AdminFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static AdminFacade getAdminFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AdminFacade();
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
    
    
    public ArrayList<UserDTO> getAllUsers(){
        ArrayList<UserDTO> userList = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> userQuery = em.createQuery("SELECT a FROM User a", User.class);
            List<User> users = userQuery.getResultList();
            for (User u : users) {
                userList.add(new UserDTO(u.getUserName()));
            }
        } finally {
            em.close();
        }
        return userList;
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
    
    
}
