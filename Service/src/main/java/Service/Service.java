package Service;

import Domain.Angajat;
import Domain.Bilet;
import Domain.Spectacol;
import Domain.Validators.ValidationException;
import Service.Angajat.IAngajatService;
import Service.Bilet.IBiletService;
import Service.Observer.Observable;
import Service.Observer.Observer;
import Service.Spectacol.ISpectacolService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Service implements IService, Observable {
    private final IAngajatService angajatService;
    private final IBiletService biletService;
    private final ISpectacolService spectacolService;

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Spectacol spectacol) {
        for (Observer observer : observers) {
            observer.updateSpectacolObserver(spectacol);
        }
    }

    @Override
    public void logout(Angajat angajat) {}

    public Service(IAngajatService angajatService, IBiletService biletService, ISpectacolService spectacolService) {
        this.angajatService = angajatService;
        this.biletService = biletService;
        this.spectacolService = spectacolService;
    }

    @Override
    public void registerAngajat(Angajat angajat) {
        angajatService.registerAngajat(angajat);
    }

    @Override
    public Angajat loginAngajat(String username, String password, Observer client) {
        return angajatService.loginAngajat(username, password);
    }

    @Override
    public void reserveBilet(Spectacol spectacol, String numeCumparator, int nrLocuri) {
        if (nrLocuri > spectacol.getNrLocuriDisponibile()) {
            throw new ValidationException("Spectacol doesn't have enough seats to accomodate your request.");
        }

        Bilet bilet = new Bilet(numeCumparator, nrLocuri, spectacol);
        biletService.save(bilet);

        spectacol.setNrLocuriDisponibile(spectacol.getNrLocuriDisponibile() - nrLocuri);
        spectacol.setNrLocuriOcupate(spectacol.getNrLocuriOcupate() + nrLocuri);
        spectacolService.update(spectacol);

        notifyObservers(spectacol);
    }

    @Override
    public void updateSpectacol(Spectacol spectacol) {
        spectacolService.update(spectacol);
        notifyObservers(spectacol);
    }

    @Override
    public void deleteSpectacol(int spectacolID) {
        spectacolService.delete(spectacolID);
        Optional<Spectacol> spectacol = spectacolService.findOne(spectacolID);
        notifyObservers(spectacol.get());
    }

    @Override
    public void addSpectacol(Spectacol spectacol) {
        spectacolService.save(spectacol);
        notifyObservers(spectacol);
    }

    @Override
    public Spectacol findSpectacolByDateAndArtist(LocalDateTime date, String numeArtist) {
        return spectacolService.findSpectacolByDateAndArtist(date, numeArtist);
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime from, LocalDateTime to) {
        return spectacolService.findAllSpectacolByDates(from, to);
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime day) {
        return findAllSpectacolByDates (
                LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), 0, 0),
                LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), 23, 59)
        );
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByArtist(String numeArtist) {
        return spectacolService.findAllSpectacolByArtist(numeArtist);
    }

    @Override
    public Iterable<Angajat> getAllAngajat() {
        return angajatService.findAll();
    }

    @Override
    public Iterable<Bilet> getAllBilet() {
        return biletService.findAll();
    }

    @Override
    public Iterable<Spectacol> getAllSpectacol() {
        return spectacolService.findAll();
    }
}