package com.solvd.OnlineShopping.shopping;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ProductDatabase {

    private static final Logger logger = Logger.getLogger(ProductDatabase.class.getName());
    private Map<Department, List<Product>> departmentProducts;

    public List<Product> getProductsForDepartment(Department department) {
        return departmentProducts.getOrDefault(department, List.of());
    }

    public Stream<Product> getProductsForDepartmentStream() {
        return departmentProducts.values().stream().flatMap(List::stream);
    }

    public ProductDatabase() {
        this.departmentProducts = new EnumMap<>(Department.class);
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
                    logger.warning("Invalid line in product file: " + line);
                }
            }
        }
    }

    public void addProduct(Product product) {
        Department department = product.getDepartment();
        departmentProducts.putIfAbsent(department, new ArrayList<>());
        departmentProducts.get(department).add(product);
    }

}