package com.solvd.OnlineShopping.shopping;

public class CartItem<T extends Product> {
    private T product;
    private int quantity;

    public void setProduct(T product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public T getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public CartItem(T product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}