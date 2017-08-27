package xyz.jmatt.services;

import xyz.jmatt.auth.PasswordManager;
import xyz.jmatt.daos.DatabaseTransaction;
import xyz.jmatt.daos.UsersDao;
import xyz.jmatt.models.SimpleResult;
import xyz.jmatt.models.UserModel;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.UUID;

public class CreateAccountService {
    public CreateAccountService() {}

    /**
     * Attempts to create a new user account with the given username and password
     * @param username the username for the new account
     * @param originalPassword the password for the new account
     * @return result of the account creation
     */
    public SimpleResult createAccount(String username, char[] originalPassword) {
        DatabaseTransaction transaction = null;
        SimpleResult result = null;

        try {
            transaction = new DatabaseTransaction();
            UsersDao usersDao = new UsersDao(transaction);

            //Make sure username was not already in the database
            if(usersDao.doesUsernameExist(username)) {
                result = new SimpleResult("ERROR: Username already exists", true);
            } else {
                //salt & hash the user's password for secure storage
                String securePassword = PasswordManager.getInstance().getSaltedHashedPassword(originalPassword);
                originalPassword = UUID.randomUUID().toString().toCharArray(); //overwrite the original password in memory
                //generate a new UserId
                String userId = UUID.randomUUID().toString().replaceAll("-", ""); //get rid of dashes for nicer looking string

                //gather all
                UserModel userModel = new UserModel();
                userModel.setUsername(username);
                userModel.setPassword(securePassword);
                userModel.setUserId(userId);

                usersDao.pushNewUser(userModel);

                transaction.commit();
                transaction.close();
                transaction = null;

                result = new SimpleResult("", false);
            }
        } catch (SQLException e) {
            result = new SimpleResult("ERROR: Internal database error", true);
            e.printStackTrace();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            result = new SimpleResult("ERROR: Internal server error", true);
            e.printStackTrace();
        } finally {
            if(transaction != null) {
                transaction.rollback();
                transaction.close();
            }
        }
        return result;
    }
}
