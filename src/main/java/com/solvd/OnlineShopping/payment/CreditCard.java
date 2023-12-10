package com.solvd.OnlineShopping.payment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Logger;


public class CreditCard implements Payment {
    private static final Logger logger = Logger.getLogger(CreditCard.class.getName());
    private String cardNumber;
    private String cardHolderName;
    private String cardCvv;


    public CreditCard() {

    }

    public int getCardCvv() {
        throw new UnsupportedOperationException("CVV retrieval not allowed.");
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public void setCardCvv(int cardCvv) {
        this.cardCvv = hashCardCvv(cardCvv);
    }

    public void setCardNumber(String cardNumber) {
        if (!validateCreditCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Invalid credit card number");
        }
        this.cardNumber = cardNumber;
    }

    private String hashCardCvv(int cardCvv) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(String.valueOf(cardCvv).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing CVV", e);
        }
    }

    private boolean validateCreditCardNumber(String cardNumber) {

        return cardNumber.matches("\\d{16}");
    }

    @Override
    public boolean processPayment(double total) {

        return false;
    }

    public CreditCard(String cardNumber, String cardHolderName, int cardCvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        setCardCvv(cardCvv);
    }

    @Override
    public boolean makePayment() {
        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Processing credit card payment");
            logger.info("Enter CVV:");

            int enteredCvv = getValidatedCvv(scanner);
            String enteredCvvHash = hashCardCvv(enteredCvv);

            boolean paymentResult = compareHashedCvv(enteredCvvHash);

            if (paymentResult) {
                logger.info("Payment successful! Thank you for your purchase.");
            } else {
                logger.warning("Payment failed. Please try again.");
            }

            return paymentResult;
        }
    }

    private int getValidatedCvv(Scanner scanner) {
        int cvv = 0;
        while (true) {
            try {
                logger.info("Enter CVV:");
                cvv = scanner.nextInt();


                if (isValidCvvLength(cvv)) {
                    break;
                } else {
                    logger.warning("Invalid CVV length. Please enter a 3-digit CVV.");
                }
            } catch (java.util.InputMismatchException e) {
                logger.warning("Invalid CVV format. Please enter a numeric value.");
                scanner.nextLine();
            }
        }
        return cvv;
    }

    private boolean isValidCvvLength(int cvv) {
        return String.valueOf(cvv).length() == 3;
    }

    private boolean compareHashedCvv(String enteredCvvHash) {

        return enteredCvvHash.equals(cardCvv);
    }

    @Override
    public void registerInformation() {
        Scanner scanner = new Scanner(System.in);
        logger.info("Enter Card Number:");
        this.cardNumber = scanner.next();

        logger.info("Enter Card Holder Name:");
        this.cardHolderName = scanner.nextLine();
    }


    @Override
    public String toString() {
        return "Credit Card - Payment Details";
    }


}
