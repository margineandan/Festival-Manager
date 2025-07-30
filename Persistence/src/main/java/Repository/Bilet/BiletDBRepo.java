package Repository.Bilet;

import Domain.Bilet;
import Domain.Spectacol;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;
import Repository.Spectacol.SpectacolDBHibernateRepo;
import Repository.Spectacol.SpectacolDBRepo;
import Repository.Utils.AbstractDBRepoUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class BiletDBRepo extends AbstractDBRepoUtils<Integer, Bilet> implements IBiletDBRepo {
    private final SpectacolDBRepo spectacolDBRepo;
    // private final SpectacolDBHibernateRepo spectacolDBRepo;
    private static final Logger logger = LogManager.getLogger(BiletDBRepo.class);
    private final Validator<Bilet> validator;

    public BiletDBRepo(Properties properties, SpectacolDBRepo spectacolDBRepo, Validator<Bilet> validator) {
        super(properties);
        this.spectacolDBRepo = spectacolDBRepo;
        this.validator = validator;
        logger.info("BiletDBRepo initialized with properties: {}", properties);
    }

    @Override
    public Bilet getEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String numeCumparator = resultSet.getString("numeCumparator");
        int nrLocuri = resultSet.getInt("nrLocuri");
        int spectacolID = resultSet.getInt("spectacol_id");
        Spectacol spectacol = spectacolDBRepo.findOne(spectacolID).get();

        Bilet bilet = new Bilet(id, numeCumparator, nrLocuri, spectacol);
        bilet.setId(id);
        return bilet;
    }


    /**
     * @return an {@code Iterable<E>}
     */
    @Override
    public Iterable<Bilet> findAll() {
        String query = "SELECT * FROM Bilet";
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
    public Optional<Bilet> findOne(Integer id) {
        String query = "SELECT * FROM Bilet WHERE id = ?";
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
    public void save(Bilet entity) {
        validator.validate(entity);
        logger.info("Validated {} successfully.", entity);

        String query = "INSERT INTO Bilet (numeCumparator, nrLocuri, spectacol_id) VALUES (?, ?, ?)";
        executeQuery(query, entity.getNumeCumparator(), entity.getNrLocuri(), entity.getSpectacol().getId());

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
        String query = "DELETE FROM Bilet WHERE id = ?";
        executeQuery(query, id);

        logger.info("Deleted Bilet with ID = {} successfully.", id);
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
    public void update(Bilet entity) {}

    @Override
    public Iterable<Bilet> findBiletBySpectacol(Spectacol spectacol) {
        String query = "SELECT * FROM Bilet WHERE spectacol_id = ?";
        return findAllEntity(query, spectacol.getId());
    }
}