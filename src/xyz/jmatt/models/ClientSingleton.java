package xyz.jmatt.models;

public class ClientSingleton {
    private static final ClientSingleton INSTANCE = new ClientSingleton();
    private String dbKey;

    public static ClientSingleton getINSTANCE() {
        return INSTANCE;
    }

    public void setDbKey(String key) {
        dbKey = key;
    }

    public String getDbKey() {
        return dbKey;
    }
}
