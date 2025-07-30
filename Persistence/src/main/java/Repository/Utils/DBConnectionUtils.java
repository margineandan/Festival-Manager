package Repository.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Utility class for managing database connections.
 * This class handles the creation and retrieval of a database connection.
 */
public class DBConnectionUtils {
    private Connection connection = null;
    private final Properties properties;
    private static final Logger logger = LogManager.getLogger(DBConnectionUtils.class);

    /**
     * Constructs a DBConnectionUtils instance with the given properties.
     *
     * @param properties The database connection properties.
     */
    public DBConnectionUtils(Properties properties) {
        this.properties = properties;
    }

    /**
     * Retrieves the active database connection.
     * If no connection exists or the existing one is closed, a new connection is created.
     *
     * @return The active database connection.
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = createConnection();

                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }
            }
            logger.info("Database connection established.");
        } catch (SQLException exception) {
            logger.error(exception);
        }
        return connection;
    }

    /**
     * Creates a new database connection using the properties provided.
     *
     * @return A new Connection instance.
     */
    private Connection createConnection() {
        String DB_URL = properties.getProperty("jdbc.url");
        String DB_USER = properties.getProperty("jdbc.user");
        String DB_PASS = properties.getProperty("jdbc.password");

        logger.info("Connecting to database...");
        logger.info("Database URL: {}, User: {}, Password: {}", DB_URL, DB_USER, DB_PASS);

        Connection newConnection = null;
        try {
            if (DB_USER != null && DB_PASS != null) {
                newConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            } else {
                newConnection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException exception) {
            logger.error(exception);
        }
        logger.info("Database connection created.");
        return newConnection;
    }
}
