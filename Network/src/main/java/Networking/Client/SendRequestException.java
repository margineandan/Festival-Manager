package Networking.Client;

public class SendRequestException extends Exception {
    public SendRequestException(String message) {
        super(message);
    }

    public SendRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
