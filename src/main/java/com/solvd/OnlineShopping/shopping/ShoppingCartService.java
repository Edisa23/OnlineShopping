package com.solvd.OnlineShopping.shopping;

import com.solvd.OnlineShopping.payment.Payment;
import com.solvd.OnlineShopping.shippment.ShippingOption;

public interface ShoppingCartService<T extends Product> {
    void processCheckout(Cart<T> shoppingCart, Payment payment, ShippingOption shippingOption);
}