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
            List<Product> currentProducts = new ArrayList<>();
            Department currentDepartment = null;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (isDepartment(line)) {
                    currentDepartment = findDepartment(line);
                } else {
                    String[] parts = line.split(",");
                    if (parts.length == 3 && currentDepartment != null) {
                        try {
                            int productId = Integer.parseInt(parts[0]);
                            String productName = parts[1];
                            double price = Double.parseDouble(parts[2]);

                            currentProducts.add(new Product(productId, productName, price, currentDepartment));
                        } catch (NumberFormatException e) {
                            handleNumberFormatException(line, e);
                        }
                    } else {
                        System.err.println("Invalid line format: " + line);
                    }
                }
            }

            if (!currentProducts.isEmpty() && currentDepartment != null) {
                departmentProducts.putIfAbsent(currentDepartment, new ArrayList<>());
                departmentProducts.get(currentDepartment).addAll(currentProducts);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isDepartment(String line) {

        return line.toUpperCase().contains("DEPARTMENT");
    }

    private Department findDepartment(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 4) {
            String departmentString = parts[3].trim();
            return Department.valueOf(departmentString.toUpperCase());
        } else {
            throw new IllegalArgumentException("Invalid line format: " + line);
    }}

    private void handleNumberFormatException(String line, NumberFormatException e) {
        System.err.println("Error parsing line: " + line + ". Invalid number format.");
        e.printStackTrace();
    }

    public List<Product> getProductsForDepartment(Department department) {
        return departmentProducts.getOrDefault(department, new ArrayList<>());
    }
}