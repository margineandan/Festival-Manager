package Networking.Server;

import java.net.Socket;

public abstract class ConcurrentServer extends AbstractServer {
    public ConcurrentServer(int port) {
        super(port);
    }

    @Override
    protected void processRequest(Socket clientSocket) {
        Thread thread = createWorker(clientSocket);
        thread.start();
    }

    protected abstract Thread createWorker(Socket clientSocket);
}
