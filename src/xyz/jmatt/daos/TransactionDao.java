package xyz.jmatt.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionDao {
    private Connection connection;

    public TransactionDao(PersonalDatabaseTransaction transaction) {
        this.connection = transaction.getConnection();
    }

    public void intializeTable() throws SQLException {
        PreparedStatement prep = connection.prepareStatement(
                "CREATE TABLE Transactions (" +
                        "Name VARCHAR(255)," +
                        "Category VARCHAR(255)," +
                        "Amount DECIMAL(20,2)," +
                        "Date BIGINT" +
                    ");");
        prep.execute();
        prep.close();
    }
}
