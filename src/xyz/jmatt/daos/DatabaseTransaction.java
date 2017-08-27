package xyz.jmatt.daos;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTransaction {
    private final String DB_URL = "jdbc:sqlite:" + "db" + File.separator + "main.sqlite";
    private Connection connection;

    public DatabaseTransaction() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
        } catch (Exception e) {
            System.err.println("ERROR: Could not load sqlite .jar");
            e.printStackTrace();
        }
        connection = DriverManager.getConnection(DB_URL);
        connection.setAutoCommit(false);
    }

    /**
     * Gets the active connection to the database for the transaction
     * @return the active connection
     */
    Connection getConnection() {
        return connection;
    }

    /**
     * Rolls back the current connection to the database
     */
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println("Could not rollback database transaction");
        }
    }

    /**
     * Commits any pending changes to the database
     */
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
