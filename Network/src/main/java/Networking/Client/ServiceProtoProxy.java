package Networking.Client;

import Domain.Angajat;
import Domain.Bilet;
import Domain.Spectacol;
import Networking.DTOs.DTOFactory;
import Networking.DTOs.SpectacolDTO;
import Networking.ObjectProtocol.*;
import Networking.ProtobuffProtocol.GestiuneFestivalProto;
import Networking.ProtobuffProtocol.ProtoUtils;
import Service.IService;
import Service.Observer.Observer;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class ServiceProtoProxy implements IService {
    Observer client;
    private final String host;
    private final int port;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket connection;
    private final BlockingQueue<GestiuneFestivalProto.FestivalResponse> responseQueue = new LinkedBlockingQueue<>();
    private volatile boolean finished;

    public ServiceProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    protected void initializeConnection() {
        try {
            connection = new Socket(host, port);
            outputStream = connection.getOutputStream();
            inputStream = connection.getInputStream();
            finished = false;
            startReader();
        } catch (Exception exception) {
            throw new RuntimeException("Error initializing connection", exception);
        }
    }

    private void startReader() {
        var readerThread = new Thread(new ReaderThread());
        readerThread.start();
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    GestiuneFestivalProto.FestivalResponse response = GestiuneFestivalProto.FestivalResponse.parseDelimitedFrom(inputStream);
                    System.out.println("Response received: " + response.getResponseType());

                    if (isUpdateResponse(response.getResponseType())) {
                        handleUpdate(response);
                    }
                    else {
                        try {
                            responseQueue.put(response);
                        } catch (InterruptedException exception) {
                            System.out.println("ReaderThread: " + exception.getMessage());
                            throw new RuntimeException(exception);
                        }
                    }
                } catch (EOFException exception) {
                    continue;
                } catch (IOException exception) {
                    System.out.println("ReaderThread: " + exception.getMessage());
                    throw new RuntimeException(exception);
                }
            }
        }
    }

    protected void handleUpdate(GestiuneFestivalProto.FestivalResponse response) {
        if (response.getResponseType() == GestiuneFestivalProto.FestivalResponse.ResponseType.GOT_FESTIVAL_UPDATED) {
            System.out.println("Received update for festival: " + response.getSpectacol());
            SpectacolDTO spectacolDTO = ProtoUtils.getSpectacolDTO(response.getSpectacol());
            System.out.println("Received update for spectacol: " + spectacolDTO);

            try {
                client.updateSpectacolObserver(DTOFactory.fromDTO(spectacolDTO));
            } catch (Exception exception) {
                System.out.println("Error handling update: " + exception.getMessage());
            }
        }
    }

    private boolean isUpdateResponse(GestiuneFestivalProto.FestivalResponse.ResponseType type){
        return Objects.requireNonNull(type) == GestiuneFestivalProto.FestivalResponse.ResponseType.GOT_FESTIVAL_UPDATED;
    }

    protected void testConnectionOpen() {
        if (connection == null || connection.isClosed()) {
            throw new RuntimeException("Connection is closed");
        }
    }

    public void closeConnection() {
        System.out.println("Closing connection...");
        finished = true;
        try {
            Thread.sleep(1000);
        } catch (Exception exception) {
            throw new RuntimeException("Error closing connection: " + exception);
        }
        try {
            inputStream.close();
            outputStream.close();
            connection.close();
            connection = null;
            System.out.println("Connection closed");
        } catch (IOException exception) {
            System.out.println("Error closing connection: " + exception.getMessage());
        }
    }

    protected void sendRequest(GestiuneFestivalProto.FestivalRequest request) {
        try {
            System.out.println("Sending request: " + request);
            request.writeDelimitedTo(outputStream);
            outputStream.flush();
            System.out.println("Request sent: " + request);
        } catch (IOException exception) {
            throw new RuntimeException("Error sending request: " + exception);
        }
    }

    protected GestiuneFestivalProto.FestivalResponse readResponse() {
        GestiuneFestivalProto.FestivalResponse response = null;
        try {
            response = responseQueue.take();
        } catch (InterruptedException exception) {
            throw new RuntimeException("Error receiving response: " + exception);
        }

        return response;
    }

    @Override
    public void registerAngajat(Angajat angajat) {}

    @Override
    public Angajat loginAngajat(String username, String password, Observer client) {
        initializeConnection();
        this.client = client;

        GestiuneFestivalProto.FestivalRequest festivalRequest = GestiuneFestivalProto.FestivalRequest.newBuilder()
                .setRequestType(GestiuneFestivalProto.FestivalRequest.RequestType.LOGIN)
                .setAngajat(GestiuneFestivalProto.Angajat.newBuilder()
                        .setToken(username)
                        .setPassword(password)
                        .build())
                .build();

        sendRequest(festivalRequest);
        System.out.println("Request sent: " + festivalRequest);

        GestiuneFestivalProto.FestivalResponse response = readResponse();
        if (response.getResponseType() == GestiuneFestivalProto.FestivalResponse.ResponseType.ERROR) {
            String errorMessage = response.getError();
            closeConnection();
            throw new RuntimeException("Error logging in: " + errorMessage);
        }

        return ProtoUtils.getAngajat(response.getAngajat());
    }

    @Override
    public void reserveBilet(Spectacol spectacol, String numeCumparator, int nrLocuri) {
        testConnectionOpen();
        sendRequest(ProtoUtils.createReserveTicketRequest(spectacol, numeCumparator, nrLocuri));
        System.out.println("Request sent: " + spectacol + ", " + numeCumparator + ", " + nrLocuri);

        GestiuneFestivalProto.FestivalResponse response = readResponse();
        if (response.getResponseType() == GestiuneFestivalProto.FestivalResponse.ResponseType.ERROR) {
            String errorMessage = response.getError();
            closeConnection();
            throw new RuntimeException("Error reserving ticket: " + errorMessage);
        }
    }

    @Override
    public void updateSpectacol(Spectacol spectacol) {}

    @Override
    public void deleteSpectacol(int spectacolID) {}

    @Override
    public void addSpectacol(Spectacol spectacol) {}

    @Override
    public Spectacol findSpectacolByDateAndArtist(LocalDateTime date, String numeArtist) {
        return null;
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime from, LocalDateTime to) {
        return null;
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime day) {
        return null;
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByArtist(String numeArtist) {
        return null;
    }

    @Override
    public Iterable<Angajat> getAllAngajat() {
        return null;
    }

    @Override
    public Iterable<Bilet> getAllBilet() {
        return null;
    }

    @Override
    public Iterable<Spectacol> getAllSpectacol() {
        testConnectionOpen();
        sendRequest(ProtoUtils.createGetAllSpectacoleRequest());
        System.out.println("Request sent: getAllSpectacole");

        GestiuneFestivalProto.FestivalResponse response = readResponse();
        if (response.getResponseType() == GestiuneFestivalProto.FestivalResponse.ResponseType.ERROR) {
            String errorMessage = response.getError();
            closeConnection();
            throw new RuntimeException("Error logging in: " + errorMessage);
        }

        return response.getSpectacoleList().stream()
                .map(ProtoUtils::getSpectacolDTO)
                .map(DTOFactory::fromDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void notifyObservers(Spectacol spectacol) {}

    @Override
    public void removeObserver(Observer observer) {}

    @Override
    public void logout(Angajat angajat) {
        testConnectionOpen();

        /*
        sendRequest(ProtoUtils.createLogoutRequest(angajat));
        System.out.println("Request sent: logout");

        GestiuneFestivalProto.FestivalResponse response = readResponse();
        if (response.getResponseType() == GestiuneFestivalProto.FestivalResponse.ResponseType.ERROR) {
            String errorMessage = response.getError();
            closeConnection();
            throw new RuntimeException("Error logging out: " + errorMessage);
        }
         */

        closeConnection();
    }
}
