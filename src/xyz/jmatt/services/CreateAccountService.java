package xyz.jmatt.services;

import xyz.jmatt.models.SimpleResult;

public class CreateAccountService {
    public CreateAccountService() {}

    public SimpleResult createAccount(String username, char[] password) {
        return new SimpleResult("beans", true);
    }
}
