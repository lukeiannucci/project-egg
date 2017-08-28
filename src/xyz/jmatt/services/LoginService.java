package xyz.jmatt.services;

import xyz.jmatt.Strings;
import xyz.jmatt.auth.PasswordManager;
import xyz.jmatt.daos.DatabaseTransaction;
import xyz.jmatt.daos.UsersDao;
import xyz.jmatt.models.ClientSingleton;
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

            //check to see if the username exists before we bother checking passwords
            if(!usersDao.doesUsernameExist(username)) {
                result = new SimpleResult(Strings.ERROR_LOGIN, true);
            } else { //username was found, check password
                String storedPassword = usersDao.getStoredPassword(username); //grab the password from the database

                //check the user-given password against the salted hash stored in the database
                if(PasswordManager.getInstance().validatePassword(password, storedPassword)) {
                    //get the database key salt from the storedPassword
                    byte[] dbSalt = storedPassword.split(":")[1].getBytes(); //format = "passwordSalt:databaseSalt:passwordHash"
                    //re-generate the user's database encryption key for this session using their provided password & database salt
                    ClientSingleton.getINSTANCE().generateDbKey(password, dbSalt);

                    //TODO log in
                    result = new SimpleResult("", false);
                } else { //password was incorrect
                    result = new SimpleResult(Strings.ERROR_LOGIN, true);
                }
            }
        } catch (SQLException e) {
            result = new SimpleResult(Strings.ERROR_DATABASE, true);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            result = new SimpleResult(Strings.ERROR_SERVER, true);
        } finally {
            if(transaction != null) {
                transaction.rollback();
                transaction.close();
            }
        }
        return result;
    }
}
