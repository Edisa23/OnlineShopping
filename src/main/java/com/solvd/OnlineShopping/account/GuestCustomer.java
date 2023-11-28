package com.solvd.OnlineShopping.account;

public class GuestCustomer extends Customer {

    public GuestCustomer(String username, String password) {
        super(DEFAULT_USERNAME, DEFAULT_PASSWORD);

    }

    @Override
    public boolean authenticate(String password) {
        return this.getPassword().equals(password);
    }
}
