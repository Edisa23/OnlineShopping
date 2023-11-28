package com.solvd.OnlineShopping.payment;

import com.solvd.OnlineShopping.exception.InvalidPasswordException;
import com.solvd.OnlineShopping.exception.InvalidUsernameException;
import java.util.Scanner;
import java.util.logging.Logger;
public class PayPal extends Bill implements Payment {
    private static final Logger logger = Logger.getLogger(PayPal.class.getName());

    @Override
    public void processPayment(double total) {
        logger.info("Processing payment through PayPal");

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

    private void validateCredentials(String username, String password)
            throws InvalidUsernameException, InvalidPasswordException {
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
    public void registerInformation() {

    }

    @Override
    public void generateBill() {
        logger.info("Generating PayPal Bill");

        logger.info("Thank you for shopping with PayPal!");
    }

    @Override
    public String toString() {
        return "PayPal - Billing Details";
    }
}
