package xyz.jmatt.models;

/**
 * Holds information about the current user's session
 */
public class ClientSingleton {
    private static final ClientSingleton INSTANCE = new ClientSingleton();

    private String dbKey; //the encryption key for the current user's database, recalculated upon login using the user's credentials
    private String userId; //the userId of the current user

    public static ClientSingleton getINSTANCE() {
        return INSTANCE;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
