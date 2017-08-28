package xyz.jmatt.models;

import xyz.jmatt.auth.PasswordManager;

import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

/**
 * Holds information about the current user's session
 */
public class ClientSingleton {
    private static final ClientSingleton INSTANCE = new ClientSingleton();

    private String dbKey; //the encryption key for the current user's database, recalculated upon login using the user's credentials

    public static ClientSingleton getINSTANCE() {
        return INSTANCE;
    }

    public String getDbKey() {
        return dbKey;
    }

    /**
     * Generates the encryption key for the database by hashing the password provided by the user at login and the salt stored in the database
     * @param userPassword the password provided by the user at login
     * @param dbSalt the database password salt from the database
     */
    public void generateDbKey(char[] userPassword, byte[] dbSalt) throws NoSuchAlgorithmException, InvalidKeySpecException{
        dbKey = DatatypeConverter.printHexBinary(PasswordManager.getInstance().getHashForPasswordAndSalt(userPassword, dbSalt));
        userPassword = UUID.randomUUID().toString().toCharArray(); //overwrite the original password in memory
    }
}
