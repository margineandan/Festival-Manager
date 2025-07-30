package Repository.Spectacol;

import Domain.Spectacol;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;
import Repository.Utils.AbstractDBRepoUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;

public class SpectacolDBRepo extends AbstractDBRepoUtils<Integer, Spectacol> implements ISpectacolDBRepo {
    private static final Logger logger = LogManager.getLogger(SpectacolDBRepo.class);
    private final Validator<Spectacol> validator;

    public SpectacolDBRepo(Properties properties, Validator<Spectacol> validator) {
        super(properties);
        this.validator = validator;
        logger.info("SpectacolDBRepo initialized with properties: {}", properties);
    }

    @Override
    public Spectacol getEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String numeArtist = resultSet.getString("numeArtist");
        LocalDateTime dataSpectacol = resultSet.getTimestamp("dataSpectacol").toLocalDateTime();
        String locSpectacol = resultSet.getString("locSpectacol");
        int nrLocuriDisponibile = resultSet.getInt("nrLocuriDisponibile");
        int nrLocuriOcupate = resultSet.getInt("nrLocuriOcupate");

        Spectacol spectacol = new Spectacol(id, numeArtist, dataSpectacol, locSpectacol, nrLocuriDisponibile, nrLocuriOcupate);
        spectacol.setId(id);
        return spectacol;
    }

    /**
     * @return an {@code Iterable<E>}
     */
    @Override
    public Iterable<Spectacol> findAll() {
        String query = "SELECT * FROM spectacol";
        return findAllEntity(query);
    }

    /**
     * @param ID -the id of the entity to be returned
     *                <p>
     *                id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    @Override
    public Optional<Spectacol> findOne(Integer ID) {
        String query = "SELECT * FROM Spectacol WHERE ID = ?";
        return Optional.ofNullable(findFirst(query, ID));
    }

    /**
     * @param entity entity must be not null
     * <p>
     * - the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    @Override
    public void save(Spectacol entity) {
        validator.validate(entity);
        logger.info("Validated {} successfully.", entity);

        String query = "INSERT INTO Spectacol (numeArtist, dataSpectacol, locSpectacol, nrLocuriDisponibile, nrLocuriOcupate) VALUES (?, ?, ?, ?, ?)";
        String formattedDate = entity.getDataSpectacol().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        executeQuery(query, entity.getNumeArtist(), formattedDate, entity.getLocSpectacol(), entity.getNrLocuriDisponibile(), entity.getNrLocuriOcupate());

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
        String query = "DELETE FROM Spectacol WHERE ID = ?";
        executeQuery(query, id);

        logger.info("Deleted Spectacol with ID = {} successfully.", id);
    }

    /**
     * @param spectacol entity must not be null* @return an {@code Optional}
     *               <p>
     *               - null if the entity was updated
     *               <p>
     *               - otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    @Override
    public void update(Spectacol spectacol) {
        validator.validate(spectacol);
        logger.info("Validated spectacol with ID = {} successfully.", spectacol.getId());

        String query = "UPDATE Spectacol SET numeArtist = ?, dataSpectacol = ?, locSpectacol = ?, nrLocuriDisponibile = ?, nrLocuriOcupate = ? WHERE ID = ?";
        String formattedDate = spectacol.getDataSpectacol().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        executeQuery(query, spectacol.getNumeArtist(), formattedDate, spectacol.getLocSpectacol(), spectacol.getNrLocuriDisponibile(), spectacol.getNrLocuriOcupate(), spectacol.getId());

        logger.info("Updated Spectacol with ID = {} successfully.", spectacol.getId());
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByDates(LocalDateTime from, LocalDateTime to) {
        String query = "SELECT * FROM Spectacol WHERE dataSpectacol >= ? AND dataSpectacol <= ?";
        return findAllEntity(query, from, to);
    }

    @Override
    public Iterable<Spectacol> findAllSpectacolByArtist(String numeArtist) {
        String query = "SELECT * FROM Spectacol WHERE numeArtist = ?";
        return findAllEntity(query, numeArtist);
    }

    @Override
    public Spectacol findSpectacolByDateAndArtist(LocalDateTime date, String numeArtist) {
        String query = "SELECT * FROM Spectacol WHERE numeArtist = ? AND dataSpectacol = ?";
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return findFirst(query, numeArtist, formattedDate);
    }
}
