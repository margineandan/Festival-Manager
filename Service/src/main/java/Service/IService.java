package Service;

import Domain.Angajat;
import Domain.Bilet;
import Domain.Spectacol;
import Service.Observer.Observer;

import java.time.LocalDateTime;

public interface IService {
    void registerAngajat(Angajat angajat);
    Angajat loginAngajat(String username, String password, Observer client);
    void reserveBilet(Spectacol spectacol, String numeCumparator, int nrLocuri);
    void updateSpectacol(Spectacol spectacol);
    void deleteSpectacol(int spectacolID);
    void addSpectacol(Spectacol spectacol);
    Spectacol findSpectacolByDateAndArtist(LocalDateTime date, String numeArtist);
    Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime from, LocalDateTime to);
    Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime day);
    Iterable<Spectacol> findAllSpectacolByArtist(String numeArtist);
    Iterable<Angajat> getAllAngajat();
    Iterable<Bilet> getAllBilet();
    Iterable<Spectacol> getAllSpectacol();
    void notifyObservers(Spectacol spectacol);
    void removeObserver(Observer observer);
    void logout(Angajat angajat);
}