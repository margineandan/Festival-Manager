package Service.Bilet;

import Domain.Bilet;
import Domain.Spectacol;
import Repository.Bilet.IBiletDBRepo;
import Service.Abstract.AbstractService;

public class BiletService extends AbstractService<Integer, Bilet, IBiletDBRepo> implements IBiletService {

    public BiletService(IBiletDBRepo repo) {
        super(repo);
    }

    @Override
    public Iterable<Bilet> findBiletBySpectacol(Spectacol spectacol) {
        return getRepostiory().findBiletBySpectacol(spectacol);
    }
}

