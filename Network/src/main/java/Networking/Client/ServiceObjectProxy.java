package Networking.Client;

import Domain.Angajat;
import Domain.Bilet;
import Domain.Spectacol;
import Networking.DTOs.DTOFactory;
import Networking.ObjectProtocol.*;
import Service.IService;
import Service.Observer.Observer;

import java.time.LocalDateTime;
import java.util.Arrays;

public class ServiceObjectProxy extends ServiceObjectProxyBase implements IService {
    Observer client;

    public ServiceObjectProxy(String host, int port) {
        super(host, port);
    }


    @Override
    protected void handleUpdate(UpdateResponse response) {
        if (response instanceof UpdateSpectacolResponse) {
            var spectacolDTO = ((UpdateSpectacolResponse) response).getSpectacolDTO();
            System.out.println("Received update for spectacol: " + spectacolDTO);

            try {
                client.updateSpectacolObserver(DTOFactory.fromDTO(spectacolDTO));
            } catch (Exception exception) {
                System.out.println("Error handling update: " + exception.getMessage());
            }
        }
    }

    @Override
    public void registerAngajat(Angajat angajat) {}

    @Override
    public Angajat loginAngajat(String username, String password, Observer client) {
        initializeConnection();
        sendRequest(new LoginAngajatRequest(username, password));
        var response = readResponse();
        this.client = client;

        Angajat angajat = null;
        if (response instanceof LoginAngajatResponse) {
            System.out.println("Login response: " + response);
            angajat = DTOFactory.fromDTO(((LoginAngajatResponse) response).getAngajat());
        }
        else if (response instanceof ErrorResponse) {
            closeConnection();
            throw new RuntimeException("Error logging in: " + ((ErrorResponse) response).getErrorMessage());
        }
        else {
            closeConnection();
            throw new RuntimeException("Error logging in: " + response);
        }

        return angajat;
    }


    @Override
    public void reserveBilet(Spectacol spectacol, String numeCumparator, int nrLocuri) {
        testConnectionOpen();
        sendRequest(new ReserveBiletRequest(DTOFactory.getDTO(spectacol), numeCumparator, nrLocuri));
        var response = readResponse();
        if (!(response instanceof ReserveBiletResponse)) {
            throw new RuntimeException("Error reserving ticket: " + response);
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
        sendRequest(new GetAllSpectacoleRequest());
        var response = readResponse();
        Iterable<Spectacol> spectacole = null;
        if (response instanceof GetAllSpectacoleResponse) {
            spectacole = () -> Arrays.stream(DTOFactory.fromDTO(((GetAllSpectacoleResponse) response)
                    .getSpectacole())).iterator();
        }
        else if (response instanceof ErrorResponse) {
            throw new RuntimeException("Error getting all spectacole: " + ((ErrorResponse) response).getErrorMessage());
        }
        else {
            throw new RuntimeException("Error getting all spectacole: " + response);
        }

        return spectacole;
    }

    @Override
    public void notifyObservers(Spectacol spectacol) {}

    @Override
    public void removeObserver(Observer observer) {}

    @Override
    public void logout(Angajat angajat) {
        closeConnection();
    }
}
