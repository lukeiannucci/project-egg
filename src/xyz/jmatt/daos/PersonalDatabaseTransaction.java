package xyz.jmatt.daos;

import org.h2.jdbcx.JdbcDataSource;
import xyz.jmatt.models.ClientSingleton;

import java.sql.Connection;
import java.sql.SQLException;

public class PersonalDatabaseTransaction {
    private final String URL_PREFIX = "jdbc:h2:./db/";
    private final String URL_SUFFIX = ";CIPHER=AES";
    private Connection connection;

    public PersonalDatabaseTransaction() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(URL_PREFIX + ClientSingleton.getINSTANCE().getUserId() + URL_SUFFIX);
        dataSource.setPassword(ClientSingleton.getINSTANCE().getDbKey() + " ");
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
    }

    /**
     * Gets the active connection to the user's database for the transaction
     * @return the active connection
     */
    Connection getConnection() {
        return connection;
    }

    /**
     * Commits any pending changes to the user's database
     */
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println("Could not rollback database transaction");
        }
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Close the connection associated with the transaction
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Could not close connection to database");
        }
    }
}
