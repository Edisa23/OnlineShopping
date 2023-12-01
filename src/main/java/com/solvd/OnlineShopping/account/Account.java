package com.solvd.OnlineShopping.account;

public abstract class Account {
    String username;
    String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public abstract boolean authenticate(String password);

    public abstract AccountType getAccountType();
}
