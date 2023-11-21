package com.solvd.OnlineShopping.account;

public final class RegisteredCustomer extends Customer {
    public RegisteredCustomer(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}