package Repository.Spectacol;

import Domain.Spectacol;
import Repository.IRepository;

import java.time.LocalDateTime;

public interface ISpectacolDBRepo extends IRepository<Integer, Spectacol> {
    Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime from, LocalDateTime to);
    Iterable<Spectacol> findAllSpectacolByArtist(String numeArtist);
    Spectacol findSpectacolByDateAndArtist(LocalDateTime date, String numeArtist);
}