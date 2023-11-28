package com.solvd;

import com.solvd.OnlineShopping.account.Customer;
import com.solvd.OnlineShopping.account.CustomerDatabase;
import com.solvd.OnlineShopping.account.RegisteredCustomer;
import com.solvd.OnlineShopping.payment.Bill;
import com.solvd.OnlineShopping.payment.CreditCard;
import com.solvd.OnlineShopping.payment.PayPal;
import com.solvd.OnlineShopping.payment.Payment;
import com.solvd.OnlineShopping.shippment.ExpressShipping;
import com.solvd.OnlineShopping.shippment.ShippingOption;
import com.solvd.OnlineShopping.shippment.StandardShipping;
import com.solvd.OnlineShopping.shopping.Cart;
import com.solvd.OnlineShopping.shopping.CartItem;
import com.solvd.OnlineShopping.shopping.Product;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;


public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static CustomerDatabase customerDatabase = new CustomerDatabase();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {


        logger.info("=== Welcome to Solvd Shop ===");


        logger.info("=== Sign Up ===");
        logger.info("Enter username: ");
        String signUpUsername = scanner.nextLine();
        logger.info("Enter password: ");
        String signUpPassword = scanner.nextLine();

        Customer newCustomer = new RegisteredCustomer(signUpUsername, signUpPassword);
        customerDatabase.registerCustomer(newCustomer);
        logger.info("Sign up successful!");


        logger.info("\n=== Sign In ===");
        logger.info("Enter username: ");
        String signInUsername = scanner.nextLine();
        logger.info("Enter password: ");
        String signInPassword = scanner.nextLine();

        Customer authenticatedCustomer = customerDatabase.findCustomer(signInUsername);
        if (authenticatedCustomer != null && authenticatedCustomer.authenticate(signInPassword)) {
            logger.info("Sign in successful! Welcome, " + authenticatedCustomer.getUsername() + "!");
        } else {
            logger.warning("Invalid username or password. Sign in failed.");
            System.exit(0);
        }


        Product product1 = new Product(1, "Product A", 99.99);
        Product product2 = new Product(2, "Product B", 37.55);
        Product product3 = new Product(3, "Product C", 15.25);

        Cart<Product> cart = new Cart<>();


        while (true) {
            logger.info("\nMenu:");
            logger.info("1. Show Products");
            logger.info("2. Add to Cart");
            logger.info("3. Remove from Cart");
            logger.info("4. View Cart");
            logger.info("5. Order");
            logger.info("6. Exit");

            logger.info("Enter your choice: ");

            boolean exit = false;
            try {
                while (!exit) {


                    logger.info("6. Exit");

                    logger.info("Enter your choice: ");
                    int choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            logger.info("Available Products:");
                            logger.info("ID\tName\t\tPrice");
                            logger.info(product1.toString());
                            logger.info(product2.toString());
                            logger.info(product3.toString());
                            break;
                        case 2:
                            logger.info("Enter product ID to add to cart: ");
                            int productId = scanner.nextInt();
                            logger.info("Enter quantity: ");
                            int quantity = scanner.nextInt();
                            if (productId == product1.getProductId()) {
                                cart.addProduct(product1, quantity);
                            } else if (productId == product2.getProductId()) {
                                cart.addProduct(product2, quantity);
                            } else if (productId == product3.getProductId()) {
                                cart.addProduct(product3, quantity);
                            } else {
                                logger.warning("Product not found.");
                            }
                            break;
                        case 3:
                            logger.info("Enter product ID to remove from cart: ");
                            int removeId = scanner.nextInt();
                            cart.removeProduct(removeId);
                            break;
                        case 4:
                            logger.info("Cart Items:");
                            logger.info("ID\tName\t\tPrice\tQuantity");
                            for (CartItem<Product> item : cart.getCartItems()) {
                                logger.info(item.getProduct().getProductId() + "\t" +
                                        item.getProduct().getProductName() + "\t$" +
                                        item.getProduct().getPrice() + "\t" + item.getQuantity());
                            }
                            logger.info("Total: $" + cart.calculateTotal());
                            break;
                        case 5:
                            orderProcess(cart);
                            break;


                        case 6:
                            exit = true;

                            break;
                        default:
                            logger.warning("Invalid choice. Try again.");

                    }
                }
            } finally {
                logger.info("Thank you for using Solvd Shop!");
                scanner.close();
            }
        }
    }

    private static void orderProcess(Cart<Product> cart) {
        logger.info("Order Placed! Please choose shipping:");
        logger.info("1. Standard Shipping");
        logger.info("2. Express Shipping");
        int shippingChoice = scanner.nextInt();
        ShippingOption selectedShippingOption;

        if (shippingChoice == 1) {
            selectedShippingOption = new StandardShipping("Standard", 5.0, "Next Day");
        } else if (shippingChoice == 2) {
            selectedShippingOption = new ExpressShipping("Express", 10.0, "After 2-3 Days");
        } else {
            logger.log(Level.WARNING, "Invalid choice. Defaulting to Standard Shipping.");
            selectedShippingOption = new StandardShipping("Standard", 5.0, "Fast");
        }

        logger.info("Please choose payment method:");
        logger.info("1. Credit Card");
        logger.info("2. PayPal");
        int paymentChoice = scanner.nextInt();
        Payment paymentMethod;

        if (paymentChoice == 1) {
            paymentMethod = new CreditCard();
            paymentMethod.registerInformation();
        } else if (paymentChoice == 2) {
            paymentMethod = new PayPal();
            paymentMethod.registerInformation();
        } else {
            logger.warning("Invalid choice.");
            return;
        }

        boolean paymentSuccessful = paymentMethod.makePayment();
        if (paymentSuccessful) {
            double total = cart.calculateTotal();
            logger.info("Payment successfully completed!");
            generateBill(selectedShippingOption, total);
        } else {
            logger.warning("Payment failed. Please try again.");
        }
    }

    private static void generateBill(ShippingOption shippingOption, double total) {
        Bill bill = new PayPal();
        bill.generateBill();
        logger.info("Shipping Fee: $" + shippingOption.calculateShippingFee());
        logger.info("Total Bill: $" + bill.calculateTotal(total + shippingOption.calculateShippingFee()));
    }
}