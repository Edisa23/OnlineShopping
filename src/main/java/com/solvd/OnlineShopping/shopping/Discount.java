package com.solvd.OnlineShopping.shopping;

public class Discount<T extends Product> {
    private DiscountCalculator<T> discountCalculator;

    public Discount(DiscountCalculator<T> discountCalculator) {
        this.discountCalculator = discountCalculator;
    }

    public double calculateDiscount(T product) {
        return discountCalculator.calculateDiscount(product);
    }
}