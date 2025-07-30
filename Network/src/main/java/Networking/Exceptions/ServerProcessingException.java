package Networking.Exceptions;

public class ServerProcessingException extends Throwable {
    public ServerProcessingException(String message) {
        super(message);
    }

    public ServerProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerProcessingException(Throwable cause) {
        super(cause);
    }

    public ServerProcessingException(Exception exception) {
        super(exception);
    }
}
