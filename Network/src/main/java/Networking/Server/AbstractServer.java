package Networking.Server;

import java.io.IOException;
import java.net.*;

public abstract class AbstractServer {
    private final int port;
    private ServerSocket serverSocket = null;

    public AbstractServer(int port) {
        this.port = port;
    }

    public void start() throws ServerException {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);
            while (true) {
                System.out.println("Waiting for client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                processRequest(clientSocket);
            }
        } catch (IOException e) {
            throw new ServerException("Error starting server", e);
        } finally {
            if (serverSocket != null) {
                stop();
            }
        }
    }

    public void stop() throws ServerException {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new ServerException("Error closing server", e);
        }
    }

    protected abstract void processRequest(Socket clientSocket);
}
