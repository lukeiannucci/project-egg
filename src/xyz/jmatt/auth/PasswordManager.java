package xyz.jmatt.auth;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

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
        int iterations = 10000;
        byte[] salt = getNewSalt();

        PBEKeySpec keySpec = new PBEKeySpec(originalPassword, salt, iterations, 512);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = keyFactory.generateSecret(keySpec).getEncoded(); //compute hash

        originalPassword = UUID.randomUUID().toString().toCharArray(); //overwrite the original password in memory

        return iterations + ":" + DatatypeConverter.printHexBinary(salt) + ":" + DatatypeConverter.printHexBinary(hash);
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
    public boolean validatePassword(char[] password, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException{
        String[] storedSections= storedPassword.split(":");
        int iterations = Integer.parseInt(storedSections[0]);
        byte[] salt = DatatypeConverter.parseHexBinary(storedSections[1]);
        byte[] hash = DatatypeConverter.parseHexBinary(storedSections[2]);

        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, hash.length * 8);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] testHash = keyFactory.generateSecret(keySpec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }

        password = UUID.randomUUID().toString().toCharArray(); //overwrite the original password in memory

        return diff == 0;
    }
}
