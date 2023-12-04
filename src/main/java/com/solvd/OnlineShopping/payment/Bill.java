package com.solvd.OnlineShopping.payment;

public abstract class Bill implements Payment {

    public abstract void generateBill();

    public double calculateTotal(double subtotal) {

        return subtotal;
    }
}
