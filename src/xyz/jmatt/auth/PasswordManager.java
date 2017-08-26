package xyz.jmatt.auth;

public class PasswordManager {
    private static final PasswordManager INSTANCE = new PasswordManager();

    public static PasswordManager getInstance() {
        return INSTANCE;
    }
}
