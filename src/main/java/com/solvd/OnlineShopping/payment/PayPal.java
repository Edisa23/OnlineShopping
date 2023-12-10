package com.solvd.OnlineShopping.payment;

import com.solvd.OnlineShopping.exception.InvalidPasswordException;
import com.solvd.OnlineShopping.exception.InvalidUsernameException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PayPal implements Payment {
    private static final Logger logger = Logger.getLogger(PayPal.class.getName());
    private String email;
    private String passwordHash;

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        this.passwordHash = hashPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public PayPal(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    private String hashPassword(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    @Override
    public boolean processPayment(double total) {
        logger.info("Processing payment through PayPal");

        return false;
    }

    @Override
    public boolean makePayment() {
        Scanner scanner = new Scanner(System.in);
        logger.info("Enter your email for PayPal:");
        String email = scanner.nextLine();
        logger.info("Enter PayPal username:");
        String username = scanner.nextLine();
        logger.info("Enter PayPal password:");
        String password = scanner.nextLine();

        try {
            validateCredentials(username, password);
            logger.info("Payment successful! Thank you for your purchase.");
            return true;
        } catch (InvalidUsernameException | InvalidPasswordException e) {
            logger.warning("Payment failed. " + e.getMessage());
            return false;
        }
    }

    @Override
    public void registerInformation() {

    }

    private boolean validateEmail(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void validateCredentials(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        if (!username.equals("validUsername")) {
            throw new InvalidUsernameException("Invalid PayPal username.");
        }

        if (!password.equals("validPassword")) {
            throw new InvalidPasswordException("Invalid PayPal password.");
        }

        logger.info("Link your bank account or card with PayPal");
        logger.info("Bank account linked successfully!");
    }


    @Override
    public String toString() {
        return "PayPal - Billing Details";
    }
}
