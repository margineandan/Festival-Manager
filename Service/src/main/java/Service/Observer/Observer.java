package Service.Observer;

import Domain.Spectacol;

public interface Observer {
    void update();
    void updateSpectacolObserver(Spectacol spectacol);
}
