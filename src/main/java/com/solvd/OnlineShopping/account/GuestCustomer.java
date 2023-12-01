package com.solvd.OnlineShopping.account;

public class GuestCustomer extends Account {
    public GuestCustomer(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean authenticate(String password) {
        return true;
    }


    @Override
    public AccountType getAccountType() {
        return AccountType.GUEST;
    }
}