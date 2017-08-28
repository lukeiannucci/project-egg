package xyz.jmatt.models;

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

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }
}
