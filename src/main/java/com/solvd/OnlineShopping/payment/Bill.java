package com.solvd.OnlineShopping.payment;

import com.solvd.OnlineShopping.shippment.ShippingOption;
import com.solvd.OnlineShopping.shopping.Cart;
import com.solvd.OnlineShopping.shopping.Product;

public abstract class Bill implements Payment {

    public abstract void generateBill(Cart<Product> shoppingCart, ShippingOption shippingOption, double subtotal, double shippingFee, double total);

    @Override
    public void processPayment(double total) {

        System.out.println("Processing payment for the bill with a total of $" + total);
    }

    @Override
    public boolean makePayment() {

        System.out.println("Payment successful for the bill");
        return true;
    }

    @Override
    public void registerInformation() {

        System.out.println("Registering information for the payment of the bill");
    }
}
