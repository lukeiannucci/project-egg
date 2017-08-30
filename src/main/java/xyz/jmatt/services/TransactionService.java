package xyz.jmatt.services;

import xyz.jmatt.daos.PersonalDatabaseTransaction;
import xyz.jmatt.daos.TransactionDao;
import xyz.jmatt.models.TransactionModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Performs tasks related to Transactions
 */
public class TransactionService {
    public TransactionService() {}

    /**
     * Returns a list of all transactions in the database
     * @return a list of TransactionModels
     */
    public List<TransactionModel> getAllTransactions() {
        PersonalDatabaseTransaction transaction = null;
        List<TransactionModel> result = null;

        try {
            transaction = new PersonalDatabaseTransaction();
            TransactionDao transactionDao = new TransactionDao(transaction);

            result = transactionDao.getAllTransactions();

            transaction.commit();
            transaction.close();
            transaction = null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(transaction != null) {
                transaction.rollback();
                transaction.close();
                transaction = null;
            }
        }
        return result;
    }

    /**
     * Adds the given transaction to the database
     * @param model the TransactionModel to add
     * @return whether or not the model was added successfully
     */
    public boolean addTransaction(TransactionModel model) {
        PersonalDatabaseTransaction transaction = null;

        try {
            transaction = new PersonalDatabaseTransaction();
            TransactionDao transactionDao = new TransactionDao(transaction);

            transactionDao.pushTransaction(model);

            transaction.commit();
            transaction.close();
            transaction = null;

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(transaction != null) {
                transaction.rollback();
                transaction.close();
                transaction = null;
            }
        }
        return false;
    }
}
