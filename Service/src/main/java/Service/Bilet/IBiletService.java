package Service.Bilet;

import Domain.Bilet;
import Domain.Spectacol;
import Service.Abstract.IAbstractService;

public interface IBiletService extends IAbstractService<Integer, Bilet> {
    Iterable<Bilet> findBiletBySpectacol(Spectacol spectacol);
}