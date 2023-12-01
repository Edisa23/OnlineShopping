package com.solvd;

import com.solvd.OnlineShopping.account.*;
import com.solvd.OnlineShopping.payment.Bill;
import com.solvd.OnlineShopping.payment.CreditCard;
import com.solvd.OnlineShopping.payment.PayPal;
import com.solvd.OnlineShopping.payment.Payment;
import com.solvd.OnlineShopping.shippment.ShippingOption;
import com.solvd.OnlineShopping.shippment.StandardShipping;
import com.solvd.OnlineShopping.shopping.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        Cart<Product> shoppingCart = new Cart<>();
        Cart<Product> cart = new Cart<>(new Discount<>(product -> product.getPrice() * 0.5));

        ProductDatabase productDatabase = new ProductDatabase();
        try {
            productDatabase.loadProductsFromFile("src/main/resources/products.txt");
        } catch (FileNotFoundException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }


        CustomerDatabase customerDatabase = new CustomerDatabase();

        ShippingOption shippingOption = new StandardShipping("Standard Shipping", 5.99, "3-5 days");

        Payment payment = new CreditCard();

        Scanner scanner = new Scanner(System.in);

        Account currentAccount = null;

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In as a Registered Customer");
            System.out.println("3. Continue as a Guest");
            System.out.println("4. Exit");

            int authChoice = scanner.nextInt();

            switch (authChoice) {
                case 1:
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.next();
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.next();

                    currentAccount = customerDatabase.createAccount(newUsername, newPassword, AccountType.NEW);
                    System.out.println("Account created successfully. Welcome, " + newUsername + "!");
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    String username = scanner.next();
                    System.out.print("Enter password: ");
                    String password = scanner.next();

                    currentAccount = customerDatabase.authenticateUser(username, password);

                    if (currentAccount == null) {
                        System.out.println("Invalid username or password. Please try again.");
                    } else {
                        System.out.println("Welcome, " + username + "!");
                    }
                    break;
                case 3:
                    currentAccount = new GuestCustomer(InfoFinal.DEFAULT_USERNAME, InfoFinal.DEFAULT_PASSWORD);
                    System.out.println("Continuing as a guest.");
                    break;
                case 4:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }

            if (currentAccount != null) {

                while (true) {
                    System.out.println("\nChoose an option:");
                    System.out.println("1. Add product to cart");
                    System.out.println("2. Remove product from cart");
                    System.out.println("3. Display cart contents");
                    System.out.println("4. Set shipping option");
                    System.out.println("5. Make payment");
                    System.out.println("6. Logout");

                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            System.out.print("Enter product ID: ");
                            int productId = scanner.nextInt();
                            System.out.print("Enter quantity: ");
                            int quantity = scanner.nextInt();


                            List<Product> products = productDatabase.getProductsForDepartment(ProductDatabase.Department.DEPARTMENT1);
                            Product selectedProduct = products.stream()
                                    .filter(product -> product.getProductId() == productId)
                                    .findFirst()
                                    .orElse(null);

                            if (selectedProduct != null) {
                                shoppingCart.addProduct(selectedProduct, quantity);
                                System.out.println(quantity + " " + selectedProduct.getProductName() + "(s) added to the cart.");
                            } else {
                                System.out.println("Product not found.");
                            }
                            break;
                        case 2:
                            System.out.print("Enter product ID to remove: ");
                            int removeProductId = scanner.nextInt();
                            shoppingCart.removeProduct(removeProductId);
                            System.out.println("Product removed from the cart.");
                            break;
                        case 3:
                            System.out.println("Cart contents:");
                            for (CartItem<Product> item : shoppingCart.getCartItems()) {
                                System.out.println(item.getProduct().getProductName() + " - Quantity: " + item.getQuantity());
                            }
                            System.out.println("Total: $" + shoppingCart.calculateTotal());
                            break;
                        case 4:
                            System.out.println("Available shipping options:");
                            shippingOption.displayOptionDetails();
                            shoppingCart.setShippingOption(shippingOption);
                            System.out.println("Shipping option set: " + shippingOption.getClass().getSimpleName());
                            break;
                        case 5:
                            System.out.println("Available payment options:");
                            payment.registerInformation();
                            boolean paymentSuccess = payment.makePayment();
                            if (paymentSuccess) {
                                System.out.println("Payment successful!");
                                shoppingCart.setPayment(payment);
                                shoppingCart.calculateTotal();

                                Bill bill = new PayPal();
                                bill.generateBill();
                            } else {
                                System.out.println("Payment failed. Please try again.");
                            }
                            break;
                        case 6:

                            System.out.println("Logged out successfully.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option.");
                            break;
                    }
                }
            }
        }
    }}
