package com.solvd.OnlineShopping.shippment;

import com.solvd.OnlineShopping.shopping.Cart;

public interface ShippingOption {
    double calculateShippingFee();
    void displayOptionDetails();
    double calculateShippingCost(Cart<?> cart);
}