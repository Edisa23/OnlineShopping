package com.solvd.OnlineShopping.account;


public class GuestCustomer extends Customer {

    protected final String username;
    protected final String password;

    public GuestCustomer(String username, String password) {
        super(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        this.username = username;
        this.password = password;
    }


    @Override
    public boolean authenticate(String password) {
        return this.getPassword().equals(password);
    }
}
