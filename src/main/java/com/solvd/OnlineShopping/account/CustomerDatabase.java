package com.solvd.OnlineShopping.account;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class CustomerDatabase {

    private Map<String, Account> accounts;

    public CustomerDatabase() {
        this.accounts = new HashMap<>();
        loadRegisteredCustomers();
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

    private void loadRegisteredCustomers() {
        Path filePath = Paths.get("src/main/resources/user_credentials.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 3) {
                    String username = credentials[0];
                    String password = credentials[1];
                    String accountTypeString = credentials[2];

                    AccountType accountType = AccountType.valueOf(accountTypeString.toUpperCase());
                    Account account = createAccount(username, password, accountType);
                    accounts.put(username, account);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Account getAccount(String username) {
        return accounts.get(username);
    }

    public Account authenticateUser(String username, String enteredPassword) {
        Account account = getAccount(username);
        if (account != null && account.authenticate(enteredPassword)) {
            return account;
        }
        return null;
    }
}