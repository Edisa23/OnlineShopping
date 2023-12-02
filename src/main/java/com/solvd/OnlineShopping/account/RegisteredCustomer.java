package com.solvd.OnlineShopping.account;

public class RegisteredCustomer extends Account {
    public RegisteredCustomer(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean authenticate(String enteredPassword) {
        return getPassword().equals(enteredPassword);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.REGISTERED;
    }
}