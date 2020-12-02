package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.MovieDTO;
import dtos.ResponseDTO;
import entities.Movie;
import errorhandling.MovieNotFoundException;
import facades.MovieFacade;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;

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
import utils.HttpUtils;

@Path("movie")
public class MovieResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private Gson gson = new Gson();

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final MovieFacade MOVIE_FACADE = MovieFacade.getMovieFacade(EMF);

    private final String apiKey = "&apikey=d2448796";
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
    @Path("search/{title}")
    public String searchForMovie(@PathParam("title") String title) throws IOException, MovieNotFoundException {
        title = title.replaceAll(" ", "+");
        String movie;
        try {
            movie = HttpUtils.fetchData("http://www.omdbapi.com/?t=" + title + apiKey);
        } catch (IOException e) {
            throw new MovieNotFoundException();
        }
        ResponseDTO rDTO = gson.fromJson(movie, ResponseDTO.class);
        if (rDTO.getResponse().equals("False")) {
            throw new MovieNotFoundException();
        } else {
            MovieDTO mDTO = gson.fromJson(movie, MovieDTO.class);
            return gson.toJson(mDTO);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rating/downvote")
    public String downvoteMovie(String jsonString) throws IOException, MovieNotFoundException {
        String title;
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        title = json.get("Title").getAsString();
        MOVIE_FACADE.downvoteMovie(title);
        return "{\"msg\":\"Movie downvoted\"}";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rating/upvote")
    public String upvoteMovie(String jsonString) throws IOException, MovieNotFoundException {
        String title;
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        title = json.get("Title").getAsString();
        MOVIE_FACADE.upvoteMovie(title);
        return "{\"msg\":\"Movie upvoted\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rating/getrating/{title}")
    public String getMovieUpvotes(@PathParam("title") String title) throws MovieNotFoundException {
        int upvotes = MOVIE_FACADE.getUpvotesByTitle(title);
        int downvotes = MOVIE_FACADE.getDownvotesByTitle(title);
        return "{ \"upvotes\": " + upvotes + ",\n"
                + "\"downvotes\":" + downvotes
                + "}";
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("top5")
    public String getTop5() {
        return GSON.toJson(MOVIE_FACADE.getTop5());
    }
    
    
    

}
