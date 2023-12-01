package com.solvd.OnlineShopping.shopping;

@FunctionalInterface
public interface DiscountCalculator<T extends Product> {
    double calculateDiscount(T product);
}
