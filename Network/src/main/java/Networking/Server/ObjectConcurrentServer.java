package Networking.Server;

import Networking.Client.ClientObjectWorker;
import Service.IService;

import java.net.Socket;

public class ObjectConcurrentServer extends ConcurrentServer {
    private final IService server;

    public ObjectConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket clientSocket) {
        ClientObjectWorker worker = new ClientObjectWorker(server, clientSocket);
        return new Thread(worker);
    }
}
