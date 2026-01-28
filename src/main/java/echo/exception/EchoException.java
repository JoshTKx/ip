package echo.exception;

/**
 * Represents exceptions specific to the Echo application.
 * Used to signal errors in user commands or application operations.
 */
public class EchoException extends Exception {
    /**
     * Constructs an EchoException with the specified error message.
     *
     * @param message The detailed error message explaining what went wrong.
     */
    public EchoException(String message) {
        super(message);
    }
}