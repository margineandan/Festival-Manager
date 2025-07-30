package Service.Spectacol;

import Domain.Spectacol;
import Service.Abstract.IAbstractService;

import java.time.LocalDateTime;

public interface ISpectacolService extends IAbstractService<Integer, Spectacol> {
    Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime from, LocalDateTime to);
    Iterable<Spectacol> findAllSpectacolByArtist(String numeArtist);
    Spectacol findSpectacolByDateAndArtist(LocalDateTime date, String numeArtist);
}
