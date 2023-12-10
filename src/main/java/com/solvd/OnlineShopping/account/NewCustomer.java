package com.solvd.OnlineShopping.account;

public class NewCustomer extends Account {

    public NewCustomer(String username, String password) {
        super(validateUsername(username), validatePassword(password));


    }

    private static String validateUsername(String username) {

        if (username.matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
            int usernameLength = username.length();
            if (usernameLength >= InfoFinal.MIN_USERNAME_LENGTH && usernameLength <= InfoFinal.MAX_USERNAME_LENGTH) {
                return username;
            } else {
                throw new IllegalArgumentException("Username length doesn't meet requirements");
            }
        } else {
            throw new IllegalArgumentException("Username should start with a letter and can contain letters and numbers");
        }
    }

    private static String validatePassword(String password) {
        if (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$")) {
            int length = password.length();
            if (length >= InfoFinal.MIN_PASSWORD_LENGTH && length <= InfoFinal.MAX_PASSWORD_LENGTH) {
                return password;
            } else {
                throw new IllegalArgumentException("Password length doesn't meet requirements");
            }
        } else {
            throw new IllegalArgumentException("Password should be a combination of uppercase letters, lowercase letters, numbers, and symbols");
        }
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
