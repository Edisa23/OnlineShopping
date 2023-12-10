package com.solvd.OnlineShopping.payment;

public interface Payment {


    boolean processPayment(double total);

    boolean makePayment();

    void registerInformation();


}