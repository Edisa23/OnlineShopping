package com.solvd.OnlineShopping.account;

public abstract class Customer extends InfoFinal {
    protected final String username;
    protected final String password;


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Customer(String username, String password) {
        this.username = (username.length() <= MAX_USERNAME_LENGTH) ? username : DEFAULT_USERNAME;
        this.password = (password.length() >= MIN_PASSWORD_LENGTH && password.length() <= MAX_PASSWORD_LENGTH) ?
                password : DEFAULT_PASSWORD;
    }

    @Override
    public boolean authenticate(String password) {


        return this.password.equals(password);

    }
}