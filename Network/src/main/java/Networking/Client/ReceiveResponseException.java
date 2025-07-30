package Networking.Client;

public class ReceiveResponseException extends Exception {
    public ReceiveResponseException(String message) {
        super(message);
    }

    public ReceiveResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
