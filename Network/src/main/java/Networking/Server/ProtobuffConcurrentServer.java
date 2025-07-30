package Networking.Server;

import Networking.Client.ClientProtoWorker;
import Service.IService;

import java.net.Socket;

public class ProtobuffConcurrentServer extends ConcurrentServer {
    private final IService server;

    public ProtobuffConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket clientSocket) {
        ClientProtoWorker worker = new ClientProtoWorker(server, clientSocket);
        return new Thread(worker);
    }
}
