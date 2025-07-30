package Service.Spectacol;

import Domain.Spectacol;
import Repository.Spectacol.ISpectacolDBRepo;
import Service.Abstract.AbstractService;

import java.time.LocalDateTime;

public class SpectacolService extends AbstractService<Integer, Spectacol, ISpectacolDBRepo> implements ISpectacolService {

    public SpectacolService(ISpectacolDBRepo repo) {
        super(repo);
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime from, LocalDateTime to) {
        return getRepostiory().findAllSpectacolByDates(from, to);
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByArtist(String numeArtist) {
        return getRepostiory().findAllSpectacolByArtist(numeArtist);
    }

    @Override
    public Spectacol findSpectacolByDateAndArtist(LocalDateTime date, String numeArtist) {
        return getRepostiory().findSpectacolByDateAndArtist(date, numeArtist);
    }
}
