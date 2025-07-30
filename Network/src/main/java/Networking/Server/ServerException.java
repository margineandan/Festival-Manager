package Networking.Server;

import java.io.IOException;

public class ServerException extends Exception {
    public ServerException(String closingServerError, IOException e) {}
}
