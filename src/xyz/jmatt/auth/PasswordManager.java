package xyz.jmatt.auth;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
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

        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
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
     * Converts the given byte array into a hexadecimal string
     * @param bytes the array of bytes to convert
     * @return a hexadecimal String
     */
    private String toHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

    /**
     * Compares the given password to the hashed & salted one stored in the database
     * @return whether or not the password matches
     */
    public boolean validatePassword() {
        //TODO
        return false;
    }
}
