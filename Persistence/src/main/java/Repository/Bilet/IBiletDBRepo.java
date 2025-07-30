package Repository.Bilet;

import Domain.Bilet;
import Domain.Spectacol;
import Repository.IRepository;

public interface IBiletDBRepo extends IRepository<Integer, Bilet> {
    Iterable<Bilet> findBiletBySpectacol(Spectacol spectacol);
}
