package com.solvd.OnlineShopping.payment;

public interface Payment {


    void processPayment(double total);

    boolean makePayment();

    void registerInformation();

}