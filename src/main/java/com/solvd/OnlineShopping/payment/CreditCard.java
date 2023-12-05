package com.solvd.OnlineShopping.payment;

import com.solvd.OnlineShopping.exception.InvalidCVVException;

import java.util.Scanner;
import java.util.logging.Logger;


public class CreditCard extends Bill implements Payment {
    private static final Logger logger = Logger.getLogger(CreditCard.class.getName());
    private String cardNumber;
    private String cardHolderName;
    private int cardCvv;


    public CreditCard() {

    }


    public int getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(int cardCvv) {
        this.cardCvv = cardCvv;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }


    @Override
    public void processPayment(double total) {

    }

    public CreditCard(String cardNumber, String cardHolderName, int cardCvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cardCvv = cardCvv;
    }

    @Override
    public boolean makePayment() {
        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Processing credit card payment");
            logger.info("Enter CVV:");
            int cvv = scanner.nextInt();

            validateCVV(cvv);
            setCardCvv(cvv);
            logger.info("Payment successful! Thank you for your purchase.");
            return true;
        } catch (InvalidCVVException e) {
            logger.warning("Payment failed. " + e.getMessage());
            return false;
        }
    }

    private void validateCVV(int cvv) throws InvalidCVVException {
        if (cvv <= 0) {
            throw new InvalidCVVException("Invalid CVV. Please check your information and try again.");
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
    public void generateBill() {
        logger.info("Generating Credit Card Bill");

        logger.info("Thank you for shopping with Credit Card!");
    }

    @Override
    public String toString() {
        return "Credit Card - Payment Details";
    }


}
