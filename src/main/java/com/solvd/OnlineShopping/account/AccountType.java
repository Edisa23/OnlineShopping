package com.solvd.OnlineShopping.account;

public enum AccountType {

    NEW("New Customer"),
    REGISTERED("Registered Customer"),
    GUEST("Guest");
    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}