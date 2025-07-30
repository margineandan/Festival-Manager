package Service.Angajat;

import Domain.Angajat;
import Domain.Validators.ValidationException;
import Repository.Angajat.IAngajatDBRepo;
import Service.Abstract.AbstractService;
import Service.Utils.Utils;

public class AngajatService extends AbstractService<Integer, Angajat, IAngajatDBRepo> implements IAngajatService {

    public AngajatService(IAngajatDBRepo repo) {
        super(repo);
    }

    @Override
    public void registerAngajat(Angajat angajat) {
        String username = angajat.getUsername();
        String password = Utils.computeSha256Hash(angajat.getPassword());
        Angajat newAngajat = new Angajat(username, password);

        if (getRepostiory().findAngajatByCredentials(username, password) != null) {
            throw new ValidationException("User already exists!");
        }

        save(newAngajat);
    }

    @Override
    public Angajat loginAngajat(String username, String password) {
        return getRepostiory().findAngajatByCredentials(username, Utils.computeSha256Hash(password));
    }
}