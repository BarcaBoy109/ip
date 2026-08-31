package kotha;

/** Represents an invalid command or task operation in Kotha. */
public class KothaException extends Exception {
    /** Creates an exception with the supplied user-facing message. */
    public KothaException(String message) {
        super(message);
    }
}
