package xyz.jmatt.daos;

import xyz.jmatt.models.Category;
import xyz.jmatt.models.TransactionModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public void intializeTable() throws SQLException {
        PreparedStatement prep = connection.prepareStatement(
                "CREATE TABLE Transactions (Name VARCHAR(255), Category VARCHAR(255), Amount DECIMAL(20,2), Date BIGINT);");
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
                "INSERT INTO Transactions (Name,Category,Amount,Date) VALUES (?,?,?,?);");
        prep.setString(1, model.getName());
        prep.setString(2, model.getCategory());
        prep.setBigDecimal(3, model.getAmount());
        prep.setLong(4, model.getDate());
        prep.executeUpdate();
        prep.close();
    }
}
