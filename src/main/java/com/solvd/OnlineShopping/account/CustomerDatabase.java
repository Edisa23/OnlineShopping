package com.solvd.OnlineShopping.account;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDatabase {
    private static final Logger logger = Logger.getLogger(CustomerDatabase.class.getName());
    private Map<String, Account> accounts;

    public CustomerDatabase() {
        this.accounts = new HashMap<>();
        loadRegisteredCustomers();
    }

    public void addAccount(Account account) {

        if (account != null) {
            accounts.put(account.getUsername(), account);
        } else {

            logger.warning("Please enter a correct username or password");
        }
    }

    public Account createAccount(String username, String password, AccountType accountType) {

        if (!isValidUsername(username) || !isValidPassword(password)) {
            return null;
        }
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

    private boolean isValidUsername(String username) {

        int usernameLength = username.length();
        return usernameLength >= InfoFinal.MIN_USERNAME_LENGTH && usernameLength <= InfoFinal.MAX_USERNAME_LENGTH;


    }

    private boolean isValidPassword(String password) {

        int passwordLength = password.length();
        return passwordLength >= InfoFinal.MIN_PASSWORD_LENGTH && passwordLength <= InfoFinal.MAX_PASSWORD_LENGTH;
    }

    private void loadRegisteredCustomers() {
        Path filePath = Paths.get("src/main/resources/user_credentials.txt");

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
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
            Logger.getLogger(CustomerDatabase.class.getName()).log(Level.SEVERE, "Error loading registered customers", e);
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