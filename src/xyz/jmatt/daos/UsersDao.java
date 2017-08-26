package xyz.jmatt.daos;

import java.sql.Connection;

public class UsersDao {
    private Connection connection;

    public UsersDao(DatabaseTransaction transaction) {
        this.connection = transaction.getConnection();
    }

    public boolean doesUsernameExist(String username) {
        return false;
    }
}
