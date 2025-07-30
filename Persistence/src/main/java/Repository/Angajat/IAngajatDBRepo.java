package Repository.Angajat;

import Domain.Angajat;
import Repository.IRepository;

public interface IAngajatDBRepo extends IRepository<Integer, Angajat> {
    Angajat findAngajatByCredentials(String username, String password);
}
