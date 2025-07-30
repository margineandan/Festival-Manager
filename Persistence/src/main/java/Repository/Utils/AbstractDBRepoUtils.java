package Repository.Utils;

import Domain.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.StreamSupport;

/**
 * Abstract utility class for database repository operations.
 * Provides generic methods for executing queries and retrieving entities.
 *
 * @param <ID> The type of the entity identifier.
 * @param <E>  The type of the entity, extending Entity<ID>.
 */
public abstract class AbstractDBRepoUtils<ID, E extends Entity<ID>> {
    private final DBConnectionUtils connectionUtils;
    private static final Logger logger = LogManager.getLogger(AbstractDBRepoUtils.class);

    /**
     * Constructs an AbstractDBRepoUtils instance with the given properties.
     *
     * @param properties The database connection properties.
     */
    public AbstractDBRepoUtils(Properties properties) {
        this.connectionUtils = new DBConnectionUtils(properties);
    }

    /**
     * Converts a ResultSet row into an entity object.
     *
     * @param resultSet The ResultSet containing the entity data.
     * @return The entity object.
     * @throws SQLException If a database access error occurs.
     */
    protected abstract E getEntity(ResultSet resultSet) throws SQLException;

    /**
     * Executes a SELECT query and retrieves all matching entities.
     *
     * @param SQLQuery The SQL query to execute.
     * @param varargs  The query parameters.
     * @return An Iterable collection of retrieved entities.
     */
    protected Iterable<E> findAllEntity(String SQLQuery, Object... varargs) {
        Connection connection = connectionUtils.getConnection();
        List<E> entities = new ArrayList<>();
        logger.trace("Query: {}", SQLQuery);

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery)) {
            for (int i = 0; i < varargs.length; i++) {
                preparedStatement.setObject(i + 1, varargs[i]);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(getEntity(resultSet));
                }
            }
            logger.trace("Query returned {} rows.", entities.size());
        } catch (SQLException exception) {
            logger.error(exception);
        }

        return entities;
    }

    /**
     * Executes a SELECT query and retrieves the first matching entity.
     *
     * @param SQLQuery The SQL query to execute.
     * @param varargs  The query parameters.
     * @return The first matching entity or null if no entity is found.
     */
    protected E findFirst(String SQLQuery, Object... varargs) {
        return StreamSupport.stream(findAllEntity(SQLQuery, varargs).spliterator(), false)
                .findFirst().orElse(null);
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE query.
     *
     * @param SQLQuery The SQL query to execute.
     * @param varargs  The query parameters.
     */
    protected void executeQuery(String SQLQuery, Object... varargs) {
        Connection connection = connectionUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery)) {
            for (int i = 0; i < varargs.length; i++) {
                preparedStatement.setObject(i + 1, varargs[i]);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            logger.trace("Query executed on {} rows.", rowsAffected);
        } catch (SQLException exception) {
            logger.error(exception);
        }
    }
}

