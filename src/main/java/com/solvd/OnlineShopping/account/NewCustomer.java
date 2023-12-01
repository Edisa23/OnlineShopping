package com.solvd.OnlineShopping.account;

import static com.solvd.OnlineShopping.account.InfoFinal.*;

public class NewCustomer extends Account {

    public NewCustomer(String username, String password) {
        super(username,password);

        this.username = (username.length() <= MAX_USERNAME_LENGTH) ? username : DEFAULT_USERNAME;
        this.password = (password.length() >= MIN_PASSWORD_LENGTH && password.length() <= MAX_PASSWORD_LENGTH) ?
                password : DEFAULT_PASSWORD;
    }

    @Override
    public boolean authenticate(String password) {
        return this.password.equals(password);

    }
    @Override
    public AccountType getAccountType() {
        return AccountType.NEW;
}}