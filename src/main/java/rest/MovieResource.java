package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.CombinedDTO;
import dtos.MovieDTO;
import dtos.ResponseDTO;
import entities.Movie;
import errorhandling.MovieNotFoundException;
import facades.MovieFacade;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private ExecutorService threadPool = Executors.newCachedThreadPool();

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
    
    @GET
    @Path("extern")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJokes() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        

        Callable<MovieDTO> gotTask = new Callable<MovieDTO>() {
            @Override
            public MovieDTO call() throws Exception {
                String got = HttpUtils.fetchData("http://www.omdbapi.com/?t=game+of+thrones" + apiKey);
                MovieDTO gotDTO = gson.fromJson(got, MovieDTO.class);
                return gotDTO;
            }
        };
        Callable<MovieDTO> swTask = new Callable<MovieDTO>() {
            @Override
            public MovieDTO call() throws Exception {
                String sw = HttpUtils.fetchData("http://www.omdbapi.com/?t=star+wars" + apiKey);
                MovieDTO swDTO = gson.fromJson(sw, MovieDTO.class);
                return swDTO;
            }
        };
        Callable<MovieDTO> bbTask = new Callable<MovieDTO>() {
            @Override
            public MovieDTO call() throws Exception {
                String bb = HttpUtils.fetchData("http://www.omdbapi.com/?t=breaking+bad" + apiKey);
                MovieDTO bbDTO = gson.fromJson(bb, MovieDTO.class);
                return bbDTO;
            }
        };

        Future<MovieDTO> futureGot = threadPool.submit(gotTask);
        Future<MovieDTO> futureSw = threadPool.submit(swTask);
        Future<MovieDTO> futureHp = threadPool.submit(bbTask);
        MovieDTO movie1 = futureGot.get(2, TimeUnit.SECONDS);
        MovieDTO movie2 = futureSw.get(2, TimeUnit.SECONDS);
        MovieDTO movie3 = futureHp.get(2, TimeUnit.SECONDS);

        CombinedDTO combined = new CombinedDTO(movie1, movie2, movie3);
        String json = GSON.toJson(combined);
        return json;
    }
    

}
