package com.solvd.OnlineShopping.account;

public class RegisteredCustomer extends Account {
    public RegisteredCustomer(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean authenticate(String password) {
        return getPassword().equals(password);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.REGISTERED;
    }

}