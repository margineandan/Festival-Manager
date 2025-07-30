package Networking.Client;

import Domain.Spectacol;
import Networking.DTOs.DTOFactory;
import Networking.ObjectProtocol.*;
import Service.IService;
import Service.Observer.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.stream.StreamSupport;

public class ClientObjectWorker implements Observer, Runnable {
    private final IService server;
    private final Socket connection;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        ObjectInputStream in;
        ObjectOutputStream out;

        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (Exception e) {
            throw new RuntimeException("Error initializing client worker", e);
        }
        this.input = in;
        this.output = out;
    }

    @Override
    public void update() {}

    private void sendResponse(IResponse response) throws IOException {
        System.out.println("Sending response: " + response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    private IResponse handleRequest(IRequest request) {
        System.out.println("Handling request: " + request);
        IResponse response = null;

        if (request instanceof LoginAngajatRequest) {
            System.out.println("Handling LoginAngajatRequest");
            try {
                var username = ((LoginAngajatRequest) request).getUsername();
                var password = ((LoginAngajatRequest) request).getPassword();
                var angajat = server.loginAngajat(username, password, this);
                response = new LoginAngajatResponse(DTOFactory.getDTO(angajat));
            } catch (Exception exception) {
                response = new ErrorResponse(exception.getMessage());
            }
        }
        if (request instanceof GetAllSpectacoleRequest) {
            System.out.println("Handling GetAllSpectacoleRequest");
            try {
                response = new GetAllSpectacoleResponse(StreamSupport.stream(server.getAllSpectacol().spliterator(), false)
                        .toArray(Spectacol[]::new));
            } catch (Exception exception) {
                response = new ErrorResponse(exception.getMessage());
            }
        }
        if (request instanceof ReserveBiletRequest) {
            System.out.println("Handling ReserveBiletRequest");
            try {
                var spectacol = DTOFactory.fromDTO(((ReserveBiletRequest) request).getSpectacol());
                var numeCumparator = ((ReserveBiletRequest) request).getCumparatorName();
                var nrLocuri = ((ReserveBiletRequest) request).getSeats();
                server.reserveBilet(spectacol, numeCumparator, nrLocuri);
                response = new ReserveBiletResponse();
            } catch (Exception exception) {
                response = new ErrorResponse(exception.getMessage());
            }
        }
        if (response == null) {
            response = new ErrorResponse("Unknown request");
        }

        return response;
    }

    @Override
    public void updateSpectacolObserver(Spectacol spectacol) {
        System.out.println("Spectacol updated: " + spectacol);
        var spectacolDTO = DTOFactory.getDTO(spectacol);
        try {
            sendResponse(new UpdateSpectacolResponse(spectacolDTO));
        } catch (IOException exception) {
            System.out.println("Error sending response: " + exception.getMessage());
        }
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        while(connected) {
            try {
                Object request = input.readObject();
                IResponse response = handleRequest((IRequest) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException exception) {
                connected = false;
                server.removeObserver(this);
                System.out.println("Error: " + exception.getMessage());
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            System.out.println("Error: " + exception.getMessage());
        } finally {
            try {
                input.close();
                output.close();
                connection.close();
            } catch (IOException exception) {
                System.out.println("Error closing connection: " + exception.getMessage());
            }
        }
    }
}
