package errorhandling;

/**
 *
 * @author lam@cphbusiness.dk
 */
public class MovieNotFoundException extends Exception {

    public MovieNotFoundException() {
        super("Movie not found");
    }

}
