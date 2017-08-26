package xyz.jmatt.models;

/*
 * 1:1 Model for an entry in the Users database table
 */
public class UserModel {
    private String username;
    private String password;
    private String userId;

    public UserModel() {}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }
}
