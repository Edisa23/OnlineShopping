package com.solvd.OnlineShopping.shopping;

public class Product {

    private int productId;
    private String productName;
    private double price;
    private Department department;

    public Product(int productId, String productName, double price, Department department) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.department = department;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public Department getDepartment() {
        return department;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", productName='" + productName + '\'' + ", price=" + price + ", department=" + department + '}';
    }
}