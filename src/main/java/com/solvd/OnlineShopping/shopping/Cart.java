package com.solvd.OnlineShopping.shopping;

import com.solvd.OnlineShopping.exception.InvalidProductException;
import com.solvd.OnlineShopping.payment.CreditCard;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Cart<T extends Product> {
    private static final Logger logger = Logger.getLogger(CreditCard.class.getName());
    private List<CartItem<T>> cartItems;

    public Cart() {

        cartItems = new ArrayList<>();
    }

    public void addProduct(T product, int quantity) {
        try {
            validateProduct(product);
            cartItems.add((CartItem<T>) new CartItem<Product>(product, quantity));
            logger.info(quantity + " " + product.getProductName() + "(s) added to the cart.");
        } catch (InvalidProductException e) {
            logger.warning("Product not added to the cart. " + e.getMessage());
        }
    }

    private void validateProduct(Product product) throws InvalidProductException {
        if (product == null || product.getProductId() <= 0 || product.getProductName().trim().isEmpty() || product.getPrice() < 0) {
            throw new InvalidProductException("Invalid product details.");
        }
    }


    public void removeProduct(int productId) {
        cartItems.removeIf(item -> item.getProduct().getProductId() == productId);
        logger.info("Product removed from the cart.");
    }

    public List<CartItem<T>> getCartItems() {
        return (List<CartItem<T>>) cartItems;
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem<T> item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }
}