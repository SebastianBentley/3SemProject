package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.MovieDTO;
import entities.User;
import errorhandling.MovieNotFoundException;
import facades.UserFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private Gson gson = new Gson();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("select u from User u", entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user/addtosaved")
    public String addToSaved(String jsonString) throws IOException, MovieNotFoundException {
        String username;
        String title;
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        username = json.get("username").getAsString();
        title = json.get("Title").getAsString();
        USER_FACADE.addMovieToSaved(title, username);
        return "{\"msg\":\"Movie saved\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user/getsavedlist/{username}")
    public String getSavedList(@PathParam("username") String username) throws MovieNotFoundException {
        ArrayList<MovieDTO> savedList = USER_FACADE.getSavedListByUser(username);
        return gson.toJson(savedList);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user/changepassword")
    public String changePassword(String jsonString) throws MovieNotFoundException {
        String username;
        String newPassword;

        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        username = json.get("username").getAsString();
        newPassword = json.get("password").getAsString();
        if (newPassword.equals("") || newPassword.isEmpty()) {
            return "{\"msg\":\"Password can not be empty!\"}";
        } else {
            USER_FACADE.changePassword(username, newPassword);
            return "{\"msg\":\"Your password has been changed\"}";
        }

    }
}
