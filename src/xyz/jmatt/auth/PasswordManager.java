package xyz.jmatt.auth;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

/**
 * Deals with salt & hash generation & password validation
 */
public class PasswordManager {
    private static final PasswordManager INSTANCE = new PasswordManager();

    public static PasswordManager getInstance() {
        return INSTANCE;
    }

    /**
     * Generate a secure password using the PBKDF2 algorithm implementation for SHA-512 & a random salt
     * @param originalPassword the original password supplied by the user in a char array
     * @return the salted & hashed password
     */
    public String getSaltedHashedPassword(char[] originalPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = getNewSalt(); //salt for the user's password
        byte[] dbSalt = getNewSalt(); //salt for the user's database encryption key

        byte[] hash = getHashForPasswordAndSalt(originalPassword, salt); //generate the password hash
        originalPassword = UUID.randomUUID().toString().toCharArray(); //overwrite the original password in memory

        //format = passwordSalt:databaseSalt:passwordHash
        return DatatypeConverter.printHexBinary(salt) + ":" + DatatypeConverter.printHexBinary(dbSalt) + ":" + DatatypeConverter.printHexBinary(hash);
    }

    /**
     * Generates a new random salt for the user's password
     * @return an array of 16 random bytes
     */
    private byte[] getNewSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt); //random 16 bytes for salt
        return salt;
    }

    /**
     * Compares the given password to the hashed & salted one stored in the database
     * @return whether or not the password matches
     */
    public boolean validatePassword(char[] password, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //format = passwordSalt:databaseSalt:passwordHash
        String[] storedSections= storedPassword.split(":");
        byte[] passwordSalt = DatatypeConverter.parseHexBinary(storedSections[0]);
        byte[] passwordHash = DatatypeConverter.parseHexBinary(storedSections[2]);

        byte[] testHash = getHashForPasswordAndSalt(password, passwordSalt); //generate a new hash using the user-provided password & the hash stored in the database

        //compare the newly generated hash to the one stored in the database
        int diff = passwordHash.length ^ testHash.length;
        for(int i = 0; i < passwordHash.length && i < testHash.length; i++) {
            diff |= passwordHash[i] ^ testHash[i];
        }

        password = UUID.randomUUID().toString().toCharArray(); //overwrite the original password in memory

        return diff == 0;
    }

    /**
     * Generates a hash  using the PBKDF2 algorithm implementation for SHA-512 & a random salt using the given password & salt
     * @param password the password provided by the user
     * @param salt the random salt
     * @return the resulting hash
     */
    public byte[] getHashForPasswordAndSalt(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 10000;
        int hashSizeBytes = 512;

        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, hashSizeBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        return keyFactory.generateSecret(keySpec).getEncoded();
    }
}
