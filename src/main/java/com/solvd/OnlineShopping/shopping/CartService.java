package com.solvd.OnlineShopping.shopping;

import com.solvd.OnlineShopping.payment.Payment;

public interface CartService<T extends Product> {
    void addProductToCart(int productId, int quantity, ProductDatabase productDatabase, Cart<T> shoppingCart);

    void performCartAction(int action, Cart<T> shoppingCart);

    void setShippingOption(int shippingOptionChoice, Cart<T> shoppingCart);

    void setPaymentMethod(Payment payment, Cart<T> shoppingCart);
}