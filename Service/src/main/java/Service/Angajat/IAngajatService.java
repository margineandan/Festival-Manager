package Service.Angajat;

import Domain.Angajat;
import Service.Abstract.IAbstractService;

public interface IAngajatService extends IAbstractService<Integer, Angajat> {
    void registerAngajat(Angajat angajat);
    Angajat loginAngajat(String username, String password);
}
