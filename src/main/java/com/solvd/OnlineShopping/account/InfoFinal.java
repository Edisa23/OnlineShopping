package com.solvd.OnlineShopping.account;

public abstract class InfoFinal {
    final static int MAX_USERNAME_LENGTH = 13;
    final static int MIN_PASSWORD_LENGTH = 8;
    final static int MAX_PASSWORD_LENGTH = 20;
    final static String DEFAULT_USERNAME = "Guest";
    final static String DEFAULT_PASSWORD = "password";

    public abstract boolean authenticate(String password);
}
