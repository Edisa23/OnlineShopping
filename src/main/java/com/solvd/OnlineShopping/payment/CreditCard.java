package com.solvd.OnlineShopping.payment;

import com.solvd.OnlineShopping.exception.InvalidCVVException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Logger;


public class CreditCard  implements Payment {
    private static final Logger logger = Logger.getLogger(CreditCard.class.getName());
    private String cardNumber;
    private String cardHolderName;
    private String cardCvvHash;


    public CreditCard() {

    }

    public int getCardCvv() {
        throw new UnsupportedOperationException("CVV retrieval not allowed.");
    }

    public void setCardCvv(int cardCvv) {
        this.cardCvvHash = hashCVV(cardCvv);
    }
    private String hashCVV(int cardCvv) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(String.valueOf(cardCvv).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing CVV", e);
        }
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        if (!validateCreditCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Invalid credit card number");
        }
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
    private boolean validateCreditCardNumber(String cardNumber) {

        return cardNumber.matches("\\d{16}");
    }
    @Override
    public void processPayment(double total) {

    }

    public CreditCard(String cardNumber, String cardHolderName, int cardCvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.setCardCvv(cardCvv);
    }

    @Override
    public boolean makePayment() {
        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Processing credit card payment");
            logger.info("Enter CVV:");
            int cvv = scanner.nextInt();

            setCardCvv(cvv);
            logger.info("Payment successful! Thank you for your purchase.");
            return true;

        }
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
