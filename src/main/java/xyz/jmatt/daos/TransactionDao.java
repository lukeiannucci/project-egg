package xyz.jmatt.daos;

import xyz.jmatt.models.TransactionModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles interactions with the Transactions database table
 */
public class TransactionDao {
    private Connection connection;

    public TransactionDao(PersonalDatabaseTransaction transaction) {
        this.connection = transaction.getConnection();
    }

    /**
     * Creates the table and all the columns for the first time
     */
    public void initializeTable() throws SQLException {
        PreparedStatement prep = connection.prepareStatement(
                "CREATE TABLE Transactions (TransactionId VARCHAR(255) PRIMARY KEY, Name VARCHAR(255), Category VARCHAR(255), Amount DECIMAL(20,2), Date BIGINT);");
        prep.execute();
        prep.close();
    }

    /**
     * Gets a list of models for all transactions in the table
     * @return a list of all transactions put into TransactionModels
     */
    public List<TransactionModel> getAllTransactions() throws SQLException {
        PreparedStatement prep = connection.prepareStatement(
                "SELECT * FROM Transactions");
        ResultSet resultSet = prep.executeQuery();
        List<TransactionModel> transactions = new ArrayList<>();
        while(resultSet.next()) {
            TransactionModel model = new TransactionModel();
            model.setTransactionId(resultSet.getString("TransactionId"));
            model.setName(resultSet.getString("Name"));
            model.setCategory(resultSet.getString("Category"));
            model.setAmount(resultSet.getBigDecimal("Amount"));
            model.setDate(resultSet.getLong("Date"));
            transactions.add(model);
        }
        resultSet.close();
        prep.close();
        return transactions;
    }

    /**
     * Adds the given model to the Transactions database table
     * @param model the model to add
     */
    public void pushTransaction(TransactionModel model) throws SQLException {
        PreparedStatement prep = connection.prepareStatement(
                "INSERT INTO Transactions (TransactionId,Name,Category,Amount,Date) VALUES (?,?,?,?,?);");
        prep.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
        prep.setString(2, model.getName());
        prep.setString(3, model.getCategory());
        prep.setBigDecimal(4, model.getAmount());
        prep.setLong(5, model.getDate());

        prep.executeUpdate();
        prep.close();
    }
}
