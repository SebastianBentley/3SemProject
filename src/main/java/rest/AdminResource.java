package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.AdminFacade;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import utils.EMF_Creator;

@Path("admin")
public class AdminResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private Gson gson = new Gson();

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final AdminFacade ADMIN_FACADE = AdminFacade.getAdminFacade(EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allUsers")
    @RolesAllowed("admin")
    public String getAllUsers() {
        return GSON.toJson(ADMIN_FACADE.getAllUsers());
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteuser/{username}")
    @RolesAllowed("admin")
    public String adminDeleteUser(@PathParam("username") String username) {
        ADMIN_FACADE.deleteUser(username);
        return "{\"msg\":\"User " + username + " has been deleted\"}";
    }   
}
