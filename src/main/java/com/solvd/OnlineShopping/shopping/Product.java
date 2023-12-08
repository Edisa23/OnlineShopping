package com.solvd.OnlineShopping.shopping;

public class Product {

    private int productId;
    private String productName;
    private double price;
    private ProductDatabase.Department department;

    public Product(int productId, String productName, double price, ProductDatabase.Department department) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.department = department;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDepartment(ProductDatabase.Department department) {
        this.department = department;
    }

    public ProductDatabase.Department getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", productName='" + productName + '\'' + ", price=" + price + ", department=" + department + '}';
    }
}