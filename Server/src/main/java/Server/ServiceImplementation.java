package Server;

import Domain.Angajat;
import Domain.Bilet;
import Domain.Spectacol;
import Domain.Validators.ValidationException;
import Service.IService;
import Service.Observer.Observer;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceImplementation implements IService {
    private final IService innerService;
    HashMap<String, Observer> loggedAngajati = new HashMap<>();

    public ServiceImplementation(IService innerService) {
        this.innerService = innerService;
    }

    @Override
    public void registerAngajat(Angajat angajat) {
        innerService.registerAngajat(angajat);
    }

    @Override
    public Angajat loginAngajat(String username, String password, Observer client) {
        Angajat angajat = innerService.loginAngajat(username, password, client);
        if (angajat != null && client != null) {
            if (loggedAngajati.containsKey(username)) {
                throw new ValidationException("Employee already logged in!");
            } else {
                loggedAngajati.put(username, client);
            }
        }
        else {
            throw new ValidationException("Invalid username or password!");
        }

        return angajat;
    }

    @Override
    public void reserveBilet(Spectacol spectacol, String numeCumparator, int nrLocuri) {
        Spectacol currentSpectacol = innerService.findSpectacolByDateAndArtist(
                spectacol.getDataSpectacol(),
                spectacol.getNumeArtist()
        );

        innerService.reserveBilet(currentSpectacol, numeCumparator, nrLocuri);

        Spectacol updated = innerService.findSpectacolByDateAndArtist(
                currentSpectacol.getDataSpectacol(),
                currentSpectacol.getNumeArtist()
        );

        System.out.println("Updated spectacol: " + updated.getNrLocuriDisponibile());
        System.out.println("Observers count = "+ loggedAngajati.size());
        notifyObservers(updated);
    }

    @Override
    public void updateSpectacol(Spectacol spectacol) {
        innerService.updateSpectacol(spectacol);
    }

    @Override
    public void deleteSpectacol(int spectacolID) {
        innerService.deleteSpectacol(spectacolID);
    }

    @Override
    public void addSpectacol(Spectacol spectacol) {
        innerService.addSpectacol(spectacol);
    }

    @Override
    public Spectacol findSpectacolByDateAndArtist(LocalDateTime date, String numeArtist) {
        return innerService.findSpectacolByDateAndArtist(date, numeArtist);
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime from, LocalDateTime to) {
        return innerService.findAllSpectacolByDates(from, to);
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime day) {
        return innerService.findAllSpectacolByDates(day);
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByArtist(String numeArtist) {
        return innerService.findAllSpectacolByArtist(numeArtist);
    }

    @Override
    public Iterable<Angajat> getAllAngajat() {
        return innerService.getAllAngajat();
    }

    @Override
    public Iterable<Bilet> getAllBilet() {
        return innerService.getAllBilet();
    }

    @Override
    public Iterable<Spectacol> getAllSpectacol() {
        return innerService.getAllSpectacol();
    }

    @Override
    public void notifyObservers(Spectacol spectacol) {
        System.out.println("Notifying observers...: " + loggedAngajati.size());
        for (Observer observer : loggedAngajati.values()) {
            System.out.println("Notifying observer: " + observer);
            observer.updateSpectacolObserver(spectacol);
        }
    }

    public void removeObserver(Observer observer) {
        List<String> toRemove = new ArrayList<>();
        synchronized (loggedAngajati) {
            for (Map.Entry<String, Observer> entry : loggedAngajati.entrySet()) {
                if (entry.getValue().equals(observer)) {
                    toRemove.add(entry.getKey());
                }
            }
            toRemove.forEach(loggedAngajati::remove);
        }
    }

    @Override
    public void logout(Angajat angajat) {
        if (angajat != null) {
            loggedAngajati.remove(angajat.getUsername());
        }
        innerService.logout(angajat);
        System.out.println("Logged out: " + angajat.getUsername());
    }
}
