package com.solvd.OnlineShopping.payment;

public abstract class Bill {

    public abstract void generateBill();

    public double calculateTotal(double subtotal) {

        return subtotal;
    }
}
