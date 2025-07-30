package Service.Observer;

import Domain.Spectacol;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Spectacol spectacol);
}
