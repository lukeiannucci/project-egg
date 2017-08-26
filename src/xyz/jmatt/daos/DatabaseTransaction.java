package xyz.jmatt.daos;

import java.sql.Connection;

public class DatabaseTransaction {
    private Connection connection;
    //TODO

    public DatabaseTransaction() {}

    /**
     * Gets the active connection to the database for the transaction
     * @return the active connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Rolls back the current connection to the database
     */
    public void rollback() {
        //TODO
    }

    /**
     * Commits any pending changes to the database
     */
    public void commit() {
        //TODO
    }

    /**
     * Close the connection associated with the transaction
     */
    public void close() {
        //TODO
    }
}
