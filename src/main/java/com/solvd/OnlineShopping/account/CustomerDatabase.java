package com.solvd.OnlineShopping.account;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class CustomerDatabase {

    private Map<String, Account> accounts;

    public CustomerDatabase() {
        this.accounts = new HashMap<>();
        loadRegisterdCostumer();
    }

    public void addAccount(Account account) {
        accounts.put(account.getUsername(), account);
    }

    public Account createAccount(String username, String password, AccountType accountType) {
        switch (accountType) {
            case GUEST:
                return new GuestCustomer(username, password);
            case NEW:
                return new NewCustomer(username, password);
            case REGISTERED:
                return new RegisteredCustomer(username, password);
            default:
                throw new IllegalArgumentException("Invalid account type");
        }
    }

    private void loadRegisterdCostumer() {
        try (BufferedReader br = new BufferedReader(new FileReader("/src/main/resourcesuser_credentials.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                String username = credentials[0];
                String password = credentials[1];
                String accountTypeString = credentials[2];

                AccountType accountType = AccountType.valueOf(accountTypeString.toUpperCase());
                Account account = createAccount(username, password, accountType);
                accounts.put(username, account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Account getAccount(String username) {
        return accounts.get(username);
    }


    public Account authenticateUser(String username, String password) {

        return null;
    }

}
