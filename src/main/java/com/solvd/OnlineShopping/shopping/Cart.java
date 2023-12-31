package com.solvd.OnlineShopping.shopping;

import com.solvd.OnlineShopping.exception.InvalidProductException;
import com.solvd.OnlineShopping.payment.Payment;
import com.solvd.OnlineShopping.shippment.ShippingOption;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Cart<T extends Product> {
    private static final Logger logger = Logger.getLogger(Cart.class.getName());
    private List<CartItem<T>> cartItems;
    private ShippingOption shippingOption;
    private Payment payment;
    private Discount<T> discount;

    public Cart() {
        cartItems = new ArrayList<>();
    }

    public void setDiscount(Discount<T> discount) {
        this.discount = discount;
    }


    public void setShippingOption(ShippingOption shippingOption) {
        this.shippingOption = shippingOption;
        logger.info("Shipping option set to: " + shippingOption.getClass().getSimpleName());
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        logger.info("Payment method set to: " + payment.getClass().getSimpleName());
    }

    public Discount<T> getDiscount() {
        return discount;
    }

    public List<CartItem<T>> getCartItems() {
        return cartItems;
    }

    public Cart(Discount<T> discount) {
        cartItems = new ArrayList<>();
        this.discount = discount;
    }


    public void addProduct(T product, int quantity) {
        try {
            validateProduct(product);
            cartItems.add(new CartItem<>(product, quantity));
            logger.info(quantity + " " + product.getProductName() + "(s) added to the cart.");
        } catch (InvalidProductException e) {
            logger.warning("Product not added to the cart. " + e.getMessage());
        }
    }

    private void validateProduct(T product) throws InvalidProductException {
        if (product == null || product.getProductId() <= 0 || product.getProductName().trim().isEmpty() || product.getPrice() < 0) {
            throw new InvalidProductException("Invalid product details.");
        }
    }

    public void removeProduct(int productId) {
        cartItems.removeIf(item -> item.getProduct().getProductId() == productId);
        logger.info("Product removed from the cart.");
    }


    public double calculateTotal() {
        double total = 0;
        for (CartItem<T> item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        if (shippingOption != null) {
            total += shippingOption.calculateShippingCost(this);
        }

        return total;
    }

    public void processPayment() {

        if (payment != null) {

            boolean paymentSuccess = payment.processPayment(calculateTotal());

            if (paymentSuccess) {
                logger.info("Payment processed successfully.");

            } else {
                logger.warning("Payment processing failed. Please try again or choose a different payment method.");

            }
        } else {
            logger.warning("No payment method set. Please set a payment method before processing the payment.");

        }
    }
}