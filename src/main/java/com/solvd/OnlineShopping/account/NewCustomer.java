package com.solvd.OnlineShopping.account;

public class NewCustomer extends Account {

    public NewCustomer(String username, String password) {
        super(username, password);

        this.setUsername((username.length() <= InfoFinal.MAX_USERNAME_LENGTH) ? username : InfoFinal.DEFAULT_USERNAME);
        this.setPassword((password.length() >= InfoFinal.MIN_PASSWORD_LENGTH && password.length() <= InfoFinal.MAX_PASSWORD_LENGTH) ?
                password : InfoFinal.DEFAULT_PASSWORD);
    }

    @Override
    public boolean authenticate(String enteredPassword) {
        return this.getPassword().equals(enteredPassword);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.NEW;
    }
}
