package com.solvd;

import com.solvd.OnlineShopping.account.*;
import com.solvd.OnlineShopping.payment.Bill;
import com.solvd.OnlineShopping.payment.CreditCard;
import com.solvd.OnlineShopping.payment.PayPal;
import com.solvd.OnlineShopping.payment.Payment;
import com.solvd.OnlineShopping.shippment.ExpressShipping;
import com.solvd.OnlineShopping.shippment.ShippingOption;
import com.solvd.OnlineShopping.shippment.StandardShipping;
import com.solvd.OnlineShopping.shopping.*;
import java.util.logging.Logger;


import java.io.FileNotFoundException;

import java.util.List;
import java.util.Scanner;


public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {


        Cart<Product> shoppingCart = new Cart<>();
        Cart<Product> cart = new Cart<>(new Discount<>(product -> product.getPrice() * 0.5));

        ProductDatabase productDatabase = new ProductDatabase();
        try {
            productDatabase.loadProductsFromFile("src/main/resources/products.txt");
        } catch (FileNotFoundException e) {
            System.err.println("Error loading products: " + e.getMessage());
            return;
        }

        CustomerDatabase customerDatabase = new CustomerDatabase();

        ShippingOption shippingOption = new StandardShipping("Standard Shipping", 5.99, "3-5 days");
        Payment payment = new CreditCard();
        Scanner scanner = new Scanner(System.in);

        Account currentAccount = authenticateUser(scanner, customerDatabase);

        while (true) {

            displayMainMenu();
            int authChoice = scanner.nextInt();

            switch (authChoice) {
                case 1:
                    currentAccount = signUp(scanner, customerDatabase);
                    break;
                case 2:
                    currentAccount = signIn(scanner, customerDatabase);
                    break;
                case 3:
                    currentAccount = new GuestCustomer(InfoFinal.DEFAULT_USERNAME, InfoFinal.DEFAULT_PASSWORD);
                    logger.info("Continuing as a guest.");
                    break;
                case 4:
                    logger.info("Exiting the program.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    logger.warning("Invalid choice. Please enter a valid option.");
                    break;
            }

            if (currentAccount != null) {
                handleUserActions(scanner, shoppingCart, productDatabase, shippingOption, payment);
            }
        }
    }

    private static Account authenticateUser(Scanner scanner, CustomerDatabase customerDatabase) {
        logger.info("Enter username:");
        String username = scanner.next();
        logger.info("Enter password:");
        String password = scanner.next();

        Account authenticatedAccount = customerDatabase.authenticateUser(username, password);

        if (authenticatedAccount != null) {
            logger.info("Authentication successful. Welcome, " + username + "!");
        } else {
            logger.warning("Authentication failed. Please check your credentials.");
        }

        return authenticatedAccount;
    }


    private static Account signUp(Scanner scanner, CustomerDatabase customerDatabase) {
        logger.info("Enter a username:");
        String username = scanner.next();


        if (customerDatabase.getAccount(username) != null) {
            logger.info("Username already exists. Please choose a different one.");
            return null;
        }

        logger.info("Enter a password:");
        String password = scanner.next();

        AccountType accountType = AccountType.NEW;

        Account newAccount = customerDatabase.createAccount(username, password, accountType);


        customerDatabase.addAccount(newAccount);

        logger.info("Account created successfully. Welcome, " + username + "!");
        return newAccount;
    }

    private static Account signIn(Scanner scanner, CustomerDatabase customerDatabase) {
        logger.info("Enter username:");
        String username = scanner.next();

        Account existingAccount = customerDatabase.getAccount(username);
        if (existingAccount == null) {
            logger.warning("Account not found. Please check your username.");
            return null;
        }

        logger.info("Enter password:");
        String password = scanner.next();


        if (customerDatabase.authenticateUser(username, password) != null) {
            logger.info("Authentication successful. Welcome back, " + username + "!");
            return existingAccount;
        } else {
            logger.warning("Authentication failed. Please check your credentials.");
            return null;
        }
    }

    private static void displayMainMenu() {
        System.out.println("==== Main Menu ===");
        System.out.println("1. Sign Up");
        System.out.println("2. Sign In");
        System.out.println("3. Continue as Guest");
        System.out.println("4. Exit");
        System.out.println("************************************");
        logger.info("Enter your choice: ");
    }

    private static void handleUserActions(Scanner scanner, Cart<Product> shoppingCart, ProductDatabase productDatabase,
                                          ShippingOption shippingOption, Payment payment){
     while (true) {
        displayUserMenu();
        int userChoice = scanner.nextInt();

        switch (userChoice) {
            case 1:

                displayProductCatalog(productDatabase);
                int productIdToAdd = scanner.nextInt();
                int quantityToAdd = scanner.nextInt();
                addProductToCart(productIdToAdd, quantityToAdd, productDatabase, shoppingCart);
                break;
            case 2:

                displayShoppingCart(shoppingCart);
                int cartAction = scanner.nextInt();
                performCartAction(cartAction, shoppingCart);
                break;
            case 3:

                displayShippingOptions();
                int shippingOptionChoice = scanner.nextInt();
                setShippingOption(shippingOptionChoice, shippingOption, shoppingCart);
                break;
            case 4:

                displayPaymentMethods();
                int paymentMethodChoice = scanner.nextInt();
                setPaymentMethod(paymentMethodChoice, payment, shoppingCart);
                break;
            case 5:

                processCheckout(shoppingCart, shippingOption, payment);
                break;
            case 6:

                logger.info("Exiting the shopping system. Thank you!");
                System.exit(0);
                break;
            default:
                logger.warning("Invalid choice. Please enter a valid option.");
                break;
        }
    }}


    private static void displayUserMenu() {
        System.out.println("====== Menu ======");
        System.out.println("1. View Products and Add to Cart");
        System.out.println("2. View Cart and Modify");
        System.out.println("3. Set Shipping Option");
        System.out.println("4. Set Payment Method");
        System.out.println("5. Checkout");
        System.out.println("6. Exit");
        System.out.println("************************************");
        logger.info("Enter your choice: ");
    }

    private static void displayProductCatalog(ProductDatabase productDatabase) {
        System.out.println("====== Product Catalog ======");

        for (ProductDatabase.Department department : ProductDatabase.Department.values()) {
            List<Product> products = productDatabase.getProductsForDepartment(department);

            if (!products.isEmpty()) {
                logger.info("Department: " + department);

                products.forEach(product -> {
                    logger.info("Product ID: " + product.getProductId());
                    logger.info("Name: " + product.getProductName());
                    logger.info("Price: $" + product.getPrice());
                    System.out.println("-------------");
                });
            }

        }

        System.out.println("************************************");
    }

    private static void addProductToCart(int productId, int quantity, ProductDatabase productDatabase, Cart<Product> shoppingCart) {
        Product productToAdd = findProductById(productId, productDatabase);

        if (productToAdd != null) {
            shoppingCart.addProduct(productToAdd, quantity);
            logger.info(quantity + " " + productToAdd.getProductName() + "(s) added to the cart.");
        } else {
            logger.warning("Product with ID " + productId + " not found in the catalog.");
        }
    }

    private static Product findProductById(int productId, ProductDatabase productDatabase) {
        for (ProductDatabase.Department department : ProductDatabase.Department.values()) {
            List<Product> products = productDatabase.getProductsForDepartment(department);

            for (Product product : products) {
                if (product.getProductId() == productId) {
                    return product;
                }
            }
        }
        return null;
    }

    private static void displayShoppingCart(Cart<Product> shoppingCart) {
        List<CartItem<Product>> cartItems = shoppingCart.getCartItems();

        if (cartItems.isEmpty()) {
            logger.warning("Your shopping cart is empty.");
        } else {
            System.out.println("=== Shopping Cart ===");

            for (CartItem<Product> cartItem : cartItems) {
                Product product = cartItem.getProduct();
                int quantity = cartItem.getQuantity();

                logger.info("Product ID: " + product.getProductId());
                logger.info("Name: " + product.getProductName());
                logger.info("Price: $" + product.getPrice());
                logger.info("Quantity: " + quantity);
                logger.info("Total Price: $" + product.getPrice() * quantity);
                System.out.println("-------------");
            }

            logger.info("Total Cart Value: $" + shoppingCart.calculateTotal());
            System.out.println("************************************");
        }
    }

    private static void performCartAction(int action, Cart<Product> shoppingCart) {
        switch (action) {
            case 1:

                updateCartItemQuantity(shoppingCart);
                break;
            case 2:

                removeProductFromCart(shoppingCart);
                break;
            case 3:

                shoppingCart.getCartItems().clear();
                logger.info("Shopping cart cleared.");
                break;
            case 4:

                break;
            default:
                logger.warning("Invalid action. Please enter a valid option.");
                break;
        }
    }

    private static void updateCartItemQuantity(Cart<Product> shoppingCart) {
        Scanner scanner = new Scanner(System.in);

        logger.info("Enter the Product ID to update quantity:");
        int productId = scanner.nextInt();

        CartItem<Product> cartItem = findCartItemById(productId, shoppingCart);

        if (cartItem != null) {
            logger.info("Enter the new quantity:");
            int newQuantity = scanner.nextInt();
            cartItem.setQuantity(newQuantity);
            logger.info("Quantity updated successfully.");
        } else {
            logger.warning("Product with ID " + productId + " not found in the cart.");
        }
    }

    private static void removeProductFromCart(Cart<Product> shoppingCart) {
        Scanner scanner = new Scanner(System.in);

        logger.info("Enter the Product ID to remove:");
        int productId = scanner.nextInt();

        shoppingCart.removeProduct(productId);
        logger.info("Product removed from the cart.");
    }

    private static CartItem<Product> findCartItemById(int productId, Cart<Product> shoppingCart) {
        for (CartItem<Product> cartItem : shoppingCart.getCartItems()) {
            if (cartItem.getProduct().getProductId() == productId) {
                return cartItem;
            }
        }
        return null;
    }


    private static void displayShippingOptions() {
        System.out.println("====== Shipping Options ======");
        System.out.println("1. Standard Shipping");
        System.out.println("2. Express Shipping");
        System.out.println("3. Go back to main menu");
        System.out.println("************************************");
        logger.info("Enter your choice: ");
    }

    private static void setShippingOption(int choice, ShippingOption shippingOption, Cart<Product> shoppingCart) {
        switch (choice) {
            case 1:

                shippingOption = new StandardShipping("Standard Shipping", 5.99, "3-5 days");
                shoppingCart.setShippingOption(shippingOption);
                logger.info("Shipping option set to Standard Shipping.");
                break;
            case 2:

                shippingOption = new ExpressShipping("Express Shipping", 12.99, "1-2 days");
                shoppingCart.setShippingOption(shippingOption);
                logger.info("Shipping option set to Express Shipping.");
                break;
            case 3:

                break;
            default:
                logger.warning("Invalid choice. Please enter a valid option.");
                break;
        }
    }

    private static void displayPaymentMethods() {
        System.out.println("=== Payment Methods ===");
        System.out.println("1. Credit Card");
        System.out.println("2. PayPal");
        System.out.println("3. Go back to main menu");
        System.out.println("************************************");
        logger.info("Enter your choice: ");
    }

    private static void setPaymentMethod(int choice, Payment payment, Cart<Product> shoppingCart) {
        switch (choice) {
            case 1:

                payment = new CreditCard();
                shoppingCart.setPayment(payment);
                logger.info("Payment method set to Credit Card.");
                break;
            case 2:

                payment = new PayPal();
                shoppingCart.setPayment(payment);
                logger.info("Payment method set to PayPal.");
                break;
            case 3:

                break;
            default:
                logger.warning("Invalid choice. Please enter a valid option.");
                break;
        }
    }

    private static void processCheckout(Cart<Product> shoppingCart, ShippingOption shippingOption, Payment payment) {
        if (shoppingCart.getCartItems().isEmpty()) {
            logger.info("Your shopping cart is empty. Please add items before checking out.");
            return;
        }


        displayShoppingCart(shoppingCart);


        logger.info("Selected Shipping Option:");
        shippingOption.displayOptionDetails();


        double subtotal = shoppingCart.calculateTotal();
        double shippingFee = shippingOption.calculateShippingFee();
        double total = subtotal + shippingFee;


        logger.info("Subtotal: $" + subtotal);
        logger.info("Shipping Fee: $" + shippingFee);
        logger.info("Total: $" + total);


        boolean paymentSuccess = payment.makePayment();

        if (paymentSuccess) {

            Bill bill = (Bill) payment;
            bill.generateBill();
            logger.info("Transaction successful! Thank you for your purchase.");
            shoppingCart.getCartItems().clear();
        } else {
            logger.warning("Payment failed. Please try again or choose a different payment method.");
        }
    }}