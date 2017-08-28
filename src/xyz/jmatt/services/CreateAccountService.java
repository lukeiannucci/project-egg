package xyz.jmatt.services;

import org.h2.jdbcx.JdbcDataSource;
import xyz.jmatt.Strings;
import xyz.jmatt.auth.PasswordManager;
import xyz.jmatt.daos.DatabaseTransaction;
import xyz.jmatt.daos.UsersDao;
import xyz.jmatt.models.ClientSingleton;
import xyz.jmatt.models.SimpleResult;
import xyz.jmatt.models.UserModel;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
                result = new SimpleResult(Strings.ERROR_USERNAME, true); //username was already taken
            } else {
                //salt & hash the user's password for secure storage
                String securePassword = PasswordManager.getInstance().getSaltedHashedPassword(originalPassword);
                //generate a new UserId
                String userId = UUID.randomUUID().toString().replaceAll("-", ""); //get rid of dashes for nicer looking string

                //gather all data for model
                UserModel userModel = new UserModel();
                userModel.setUsername(username);
                userModel.setPassword(securePassword);
                userModel.setUserId(userId);
                //push new user to Users table
                usersDao.pushNewUser(userModel);

                //set up a new database for the new user - each user get's their own db that will be encrypted with a key generated from their salted & hashed password
                byte[] dbSalt = securePassword.split(":")[1].getBytes(); //get the salt for the database encryption key - note: this is not the same salt for the login password authentication,
                                                                                //this way the hash stored in the database for the user's password and encryption key are different but the user only needs
                                                                                //to remember one password
                ClientSingleton.getINSTANCE().generateDbKey(originalPassword, dbSalt); //use the database salt and the user-provided password to generate the encryption key for their database
                createNewUserDatabase(userId); //create a new database file encrypted with their key

                originalPassword = UUID.randomUUID().toString().toCharArray(); //overwrite the original password in memory

                //if everything else worked, commit then database changes
                transaction.commit();
                transaction.close();
                transaction = null;

                result = new SimpleResult("", false);
            }
        } catch (SQLException e) {
            result = new SimpleResult(Strings.ERROR_DATABASE, true);
            e.printStackTrace();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            result = new SimpleResult(Strings.ERROR_SERVER, true);
            e.printStackTrace();
        } catch (IOException e) {
            result = new SimpleResult(Strings.ERROR_NEW_DATABASE, true);
            e.printStackTrace();
        } finally {
            if(transaction != null) {
                transaction.rollback();
                transaction.close();
            }
        }
        return result;
    }

    /**
     * Creates a new encrypted database to hold all of the user's personal data
     * @param userId the userId to create the database for; file will be named according to this id
     * @throws IOException thrown if the file creation fails
     */
    private void createNewUserDatabase(String userId) throws IOException, SQLException {
//        File file = new File("db/" + userId + ".sqlite");
//        if(!file.createNewFile()) {
//            throw new IOException();
//        } else {
//            System.out.println("1");
//            Connection connection = DriverManager.getConnection("jdbc:sqlite:db/" + userId + ".sqlite", "admin", "test");
//            PreparedStatement prep = connection.prepareStatement("CREATE TABLE Test (" +
//                    "test VARCHAR PRIMARY KEY  NOT NULL  UNIQUE"    +
//                    ");");
//            prep.executeUpdate();
//            System.out.println("2");
//            testEdit(userId);
//            encrypt(userId);
//            testEdit(userId);
//        }
        boolean startedFinalTest = false;
        try {
            System.out.println("TESTING DATABASE STUFF");

            System.out.print("creating new database file ... ");
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:./db/" + userId + ";CIPHER=AES");
            ds.setPassword(ClientSingleton.getINSTANCE().getDbKey() + " ");
            Connection connection = ds.getConnection();
            System.out.println("success");

            System.out.print("verifying file creation ... ");
            File file = new File("db/" + userId + ".mv.db");
            if(file.exists()) {
                System.out.println("success");
            } else {
                System.out.println("failed");
            }

            System.out.print("attempting table creation ... ");
            PreparedStatement prep = connection.prepareStatement("CREATE TABLE Test (test VARCHAR(255));");
            prep.executeUpdate();
            connection.commit();
            connection.close();
            System.out.println("success");

            System.out.print("attempting connection *with* password ... ");
            JdbcDataSource ds2 = new JdbcDataSource();
            ds2.setURL("jdbc:h2:./db/" + userId + ";CIPHER=AES");
            ds2.setPassword(ClientSingleton.getINSTANCE().getDbKey() + " ");
            Connection connection2 = ds2.getConnection();
            PreparedStatement prep2 = connection2.prepareStatement("SELECT * FROM Test;");
            prep2.executeQuery();
            connection2.commit();
            connection2.close();
            System.out.println("success");

            startedFinalTest = true;
            System.out.print("attempting connection *without* password ... ");
            JdbcDataSource ds3 = new JdbcDataSource();
            ds3.setURL("jdbc:h2:./db/" + userId + ";CIPHER=AES");
            Connection connection3 = ds3.getConnection();
            PreparedStatement prep3 = connection3.prepareStatement("SELECT * FROM Test;");
            prep3.executeQuery();
            connection3.commit();
            connection3.close();
            System.out.println("failed");
        } catch (Exception e) {
            if(e.getMessage().contains("Wrong password") && startedFinalTest) {
                System.out.println("success");
            } else {
                System.err.println("TEST FAILED...");
                e.printStackTrace();
            }
        }
    }
}
