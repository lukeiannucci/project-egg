package xyz.jmatt.services;

import xyz.jmatt.auth.PasswordManager;
import xyz.jmatt.daos.DatabaseTransaction;
import xyz.jmatt.daos.UsersDao;
import xyz.jmatt.models.SimpleResult;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class LoginService {
    public LoginService() {}

    /**
     * Attempts to log in a user with the given credentials
     * @param username the given username
     * @param password the given password
     * @return result of the login attempt
     */
    public SimpleResult login(String username, char[] password) {
        DatabaseTransaction transaction = null;
        SimpleResult result = null;

        try {
            transaction = new DatabaseTransaction();
            UsersDao usersDao = new UsersDao(transaction);

            if(!usersDao.doesUsernameExist(username)) {
                result = new SimpleResult("ERROR: Username or password was incorrect", true);
            } else {
                String storedPassword = usersDao.getStoredPassword(username);

                if(PasswordManager.getInstance().validatePassword(password, storedPassword)) {
                    //TODO log in
                    result = new SimpleResult("", false);
                } else {
                    result = new SimpleResult("ERROR: Username or password was incorrect", true);
                }
            }
        } catch (SQLException e) {
            result = new SimpleResult("ERROR: Internal database error", true);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            result = new SimpleResult("ERROR: Internal server error", true);
        } finally {
            if(transaction != null) {
                transaction.rollback();
                transaction.close();
            }
        }
        return result;
    }
}
