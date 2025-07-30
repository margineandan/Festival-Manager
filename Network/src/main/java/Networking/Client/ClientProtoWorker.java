package Networking.Client;

import Domain.Spectacol;
import Networking.DTOs.DTOFactory;
import Networking.DTOs.SpectacolDTO;
import Networking.ObjectProtocol.ErrorResponse;
import Networking.ObjectProtocol.IRequest;
import Networking.ObjectProtocol.IResponse;
import Networking.ProtobuffProtocol.GestiuneFestivalProto;
import Networking.ProtobuffProtocol.ProtoUtils;
import Service.IService;
import Service.Observer.Observer;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientProtoWorker implements Observer, Runnable {
    private final IService server;
    private final Socket connection;
    private final InputStream input;
    private final OutputStream output;
    private volatile boolean connected;

    public ClientProtoWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        InputStream in;
        OutputStream out;

        try {
            out = connection.getOutputStream();
            in = connection.getInputStream();
            connected = true;
        } catch (Exception e) {
            throw new RuntimeException("Error initializing client worker", e);
        }

        this.input = in;
        this.output = out;
    }

    @Override
    public void update() {}

    @Override
    public void updateSpectacolObserver(Spectacol spectacol) {
        System.out.println("Received update for spectacol: " + spectacol);
        GestiuneFestivalProto.SpectacolDTO spectacolProto = ProtoUtils.toProto(spectacol);

        GestiuneFestivalProto.FestivalResponse updateResponse = GestiuneFestivalProto.FestivalResponse.newBuilder()
                .setResponseType(GestiuneFestivalProto.FestivalResponse.ResponseType.GOT_FESTIVAL_UPDATED)
                .setSpectacol(spectacolProto)
                .build();

        sendResponse(updateResponse);
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        while(connected) {
            try {
                GestiuneFestivalProto.FestivalRequest request = GestiuneFestivalProto.FestivalRequest.parseDelimitedFrom(input);
                System.out.println("Request received: " + request);
                GestiuneFestivalProto.FestivalResponse response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException exception) {
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

    private GestiuneFestivalProto.FestivalResponse handleRequest(GestiuneFestivalProto.FestivalRequest request) {
        System.out.println("Handling request: " + request);
        GestiuneFestivalProto.FestivalResponse response = null;

        if (request.getRequestType() == GestiuneFestivalProto.FestivalRequest.RequestType.LOGIN) {
            System.out.println("Handling login request...");
            GestiuneFestivalProto.Angajat angajat = request.getAngajat();
            try {
                server.loginAngajat(angajat.getToken(), angajat.getPassword(), this);
                response = ProtoUtils.createOKRespone();
            } catch (Exception exception) {
                response = ProtoUtils.createErrorResponse(exception.getMessage());
            }
        }

        if (request.getRequestType() == GestiuneFestivalProto.FestivalRequest.RequestType.GET_SPECTACOLE) {
            System.out.println("Handling get spectacole request...");
            try {
                var spectacole = server.getAllSpectacol();
                response = ProtoUtils.createGetAllSpectacoleResponse(spectacole);
            } catch (Exception exception) {
                response = ProtoUtils.createErrorResponse(exception.getMessage());
            }
        }

        if (request.getRequestType() == GestiuneFestivalProto.FestivalRequest.RequestType.SELL_TICKET) {
            System.out.println("Handling sell ticket request...");
            try {
                GestiuneFestivalProto.SpectacolDTO spectacolProtoDTO = request.getBilet().getSpectacol();
                SpectacolDTO spectacolDTO = ProtoUtils.getSpectacolDTO(spectacolProtoDTO);
                Spectacol spectacol = DTOFactory.fromDTO(spectacolDTO);
                var numeCumparator = request.getBilet().getNumeCump();
                var nrLocuri = Integer.parseInt(request.getBilet().getNrLocuri());
                server.reserveBilet(spectacol, numeCumparator, nrLocuri);
                // response = ProtoUtils.createReserveTicketResponse(spectacol, numeCumparator, nrLocuri);
                response = ProtoUtils.createOKRespone();
            } catch (Exception exception) {
                response = ProtoUtils.createErrorResponse(exception.getMessage());
            }
        }

        if (response == null) {
            response = ProtoUtils.createErrorResponse("Unknown request");
        }

        return response;
    }

    private void sendResponse(GestiuneFestivalProto.FestivalResponse response) {
        try {
            System.out.println("Sending response: " + response.getResponseType());
            response.writeDelimitedTo(output);
            output.flush();
        } catch (IOException exception) {
            System.out.println("Error sending response: " + exception.getMessage());
        }
    }
}
