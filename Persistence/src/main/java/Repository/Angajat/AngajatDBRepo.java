package Repository.Angajat;

import Domain.Angajat;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;
import Repository.Utils.AbstractDBRepoUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class AngajatDBRepo extends AbstractDBRepoUtils<Integer, Angajat> implements IAngajatDBRepo {
    private static final Logger logger = LogManager.getLogger(AngajatDBRepo.class);
    private final Validator<Angajat> validator;

    public AngajatDBRepo(Properties properties, Validator<Angajat> validator) {
        super(properties);
        this.validator = validator;
        logger.info("AngajatDBRepo initialized with properties: {}", properties);
    }

    @Override
    public Angajat getEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");

        Angajat angajat = new Angajat(id, username, password);
        angajat.setId(id);
        return angajat;
    }

    /**
     * @return an {@code Iterable<E>}
     */
    @Override
    public Iterable<Angajat> findAll() {
        String query = "SELECT * FROM Angajat";
        return findAllEntity(query);
    }

    /**
     * @param id -the id of the entity to be returned
     *                <p>
     *                id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    @Override
    public Optional<Angajat> findOne(Integer id) {
        String query = "SELECT * FROM Angajat WHERE id = ?";
        return Optional.ofNullable(findFirst(query, id));
    }

    /**
     * @param entity entity must be not null
     * <p>
     * - the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    @Override
    public void save(Angajat entity) {
        validator.validate(entity);
        logger.info("Validated {} successfully.", entity);

        String query = "INSERT INTO Angajat (username, password) VALUES (?, ?)";
        executeQuery(query, entity.getUsername(), entity.getPassword());

        logger.info("Inserted {} successfully.", entity);
    }

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * <p>
     * - null if there is no entity with the given id,
     * <p>
     * - the removed entity, otherwise
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM Angajat WHERE id = ?";
        executeQuery(query, id);

        logger.info("Deleted Angajat with ID = {} successfully.", id);
    }

    /**
     * @param entity entity must not be null* @return an {@code Optional}
     *               <p>
     *               - null if the entity was updated
     *               <p>
     *               - otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    @Override
    public void update(Angajat entity) {}

    @Override
    public Angajat findAngajatByCredentials(String username, String password) {
        String query = "SELECT * FROM Angajat WHERE username = ? AND password = ?";
        return findFirst(query, username, password);
    }
}