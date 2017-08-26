package xyz.jmatt.auth;

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
    public String getSaltedHashedPassword(char[] originalPassword) {
        //TODO
        originalPassword = UUID.randomUUID().toString().toCharArray(); //overwrite the original password in memory
        return "moose";
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
