package xyz.jmatt.services;

import xyz.jmatt.Strings;
import xyz.jmatt.auth.PasswordManager;
import xyz.jmatt.daos.MainDatabaseTransaction;
import xyz.jmatt.daos.UsersDao;
import xyz.jmatt.models.ClientSingleton;
import xyz.jmatt.models.SimpleResult;

import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Performs tasks related to logging in a user wit the provided credentials
 */
public class LoginService {
    public LoginService() {}

    /**
     * Attempts to log in a user with the given credentials
     * @param username the given username
     * @param password the given password
     * @return result of the login attempt
     */
    public SimpleResult login(String username, char[] password) {
        MainDatabaseTransaction transaction = null;
        SimpleResult result = null;

        try {
            transaction = new MainDatabaseTransaction();
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
                    String dbKey = DatatypeConverter.printHexBinary(PasswordManager.getInstance().getHashForPasswordAndSalt(password, dbSalt));
                    ClientSingleton.getINSTANCE().setDbKey(dbKey);

                    password = UUID.randomUUID().toString().toCharArray(); //overwrite the original password in memory

                    String userId = usersDao.getUserId(username); //get the userId for the given username from the database
                    ClientSingleton.getINSTANCE().setUserId(userId); //save for the session

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
