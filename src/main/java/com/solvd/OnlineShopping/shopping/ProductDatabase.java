package com.solvd.OnlineShopping.shopping;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ProductDatabase {

    public enum Department {
        DEPARTMENT1("Department 1", "Description for Department 1"),
        DEPARTMENT2("Department 2", "Description for Department 2"),
        DEPARTMENT3("Department 3", "Description for Department 3"),
        DEPARTMENT4("Department 4", "Description for Department 4"),
        DEPARTMENT5("Department 5", "Description for Department 5");

        private final String displayName;
        private final String description;

        Department(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }
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
                    currentDepartment = Department.valueOf(line.toUpperCase());
                    departmentProducts.put(currentDepartment, new ArrayList<>(currentProducts));
                    currentProducts.clear();
                } else {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        try {
                            int productId = Integer.parseInt(parts[0]);
                            String productName = parts[1];
                            double price = Double.parseDouble(parts[2]);
                            Department department = Department.valueOf(parts[3].toUpperCase());

                            currentProducts.add(new Product(productId, productName, price, department));
                        } catch (IllegalArgumentException e) {

                            System.err.println("Error parsing line: " + line);
                        }
                    } else {

                        System.err.println("Invalid line format: " + line);
                    }
                }
            }

            if (!currentProducts.isEmpty() && currentDepartment != null) {
                departmentProducts.put(currentDepartment, new ArrayList<>(currentProducts));
            }
        }
    }

    private boolean isDepartment(String line) {
        return Arrays.stream(Department.values())
                .anyMatch(department -> line.equalsIgnoreCase(department.name()));
    }

    public List<Product> getProductsForDepartment(Department department) {
        return departmentProducts.getOrDefault(department, new ArrayList<>());
    }
}