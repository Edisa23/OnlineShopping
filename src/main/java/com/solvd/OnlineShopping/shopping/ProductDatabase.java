package com.solvd.OnlineShopping.shopping;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.*;

public class ProductDatabase {
    public enum Department {
        DEPARTMENT1, DEPARTMENT2, DEPARTMENT3, DEPARTMENT4, DEPARTMENT5
    }

    private Map<Department, List<Product>> departmentProducts;

    public ProductDatabase() {
        this.departmentProducts = new HashMap<>();
    }

    public void loadProductsFromFile(String filePath) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 4) {
                    int productId = Integer.parseInt(parts[0]);
                    String productName = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    String departmentName = parts[3];

                    Department department = Department.valueOf(departmentName);

                    Product product = new Product(productId, productName, price, department);
                    addProduct(product);
                } else {
                    System.err.println("Invalid line in product file: " + line);
                }
            }
        }
    }

    public void addProduct(Product product) {
        Department department = product.getDepartment();
        departmentProducts.putIfAbsent(department, new ArrayList<>());
        departmentProducts.get(department).add(product);
    }

    public List<Product> getProductsForDepartment(Department department) {
        return departmentProducts.getOrDefault(department, new ArrayList<>());
    }
}