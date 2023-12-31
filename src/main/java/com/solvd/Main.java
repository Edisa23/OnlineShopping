package com.solvd;

import com.solvd.OnlineShopping.account.*;

import com.solvd.OnlineShopping.payment.CreditCard;
import com.solvd.OnlineShopping.payment.PayPal;
import com.solvd.OnlineShopping.payment.Payment;
import com.solvd.OnlineShopping.shippment.ExpressShipping;
import com.solvd.OnlineShopping.shippment.ShippingOption;
import com.solvd.OnlineShopping.shippment.StandardShipping;
import com.solvd.OnlineShopping.shopping.*;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.InputMismatchException;
import java.util.logging.Logger;


import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int SIGN_UP = 1;
    private static final int SIGN_IN = 2;
    private static final int CONTINUE_AS_GUEST = 3;
    private static final int EXIT = 4;
    private static final int VIEW_PRODUCTS = 1;
    private static final int VIEW_CART = 2;
    private static final int SET_SHIPPING_OPTION = 3;
    private static final int SET_PAYMENT_METHOD = 4;
    private static final int CHECKOUT = 5;
    private static final int EXIT_USER_MENU = 6;

    public static void main(String[] args) {


        Cart<Product> shoppingCart = new Cart<>();
        Cart<Product> cart = new Cart<>(new Discount<>(product -> product.getPrice() * 0.1));

        ProductDatabase productDatabase = new ProductDatabase();
        try {
            productDatabase.loadProductsFromFile("src/main/resources/products.txt");
        } catch (FileNotFoundException e) {
            logger.warning("Error loading products: " + e.getMessage());
            return;
        }

        CustomerDatabase customerDatabase = new CustomerDatabase();
        ShippingOption shippingOption = new ShippingOption() {

            @Override
            public double calculateShippingFee() {
                return 0;
            }

            @Override
            public void displayOptionDetails() {

            }

            @Override
            public double calculateShippingCost(Cart<?> cart) {
                return 0;
            }
        };

        Scanner scanner = new Scanner(System.in);

        Account currentAccount = null;

        while (true) {
            displayMainMenu();
            try {
                int authChoice = scanner.nextInt();

                switch (authChoice) {
                    case SIGN_UP:
                        currentAccount = signUp(scanner, customerDatabase);
                        break;
                    case SIGN_IN:
                        currentAccount = signIn(scanner, customerDatabase);
                        break;
                    case CONTINUE_AS_GUEST:
                        currentAccount = new GuestCustomer(InfoFinal.DEFAULT_USERNAME, InfoFinal.DEFAULT_PASSWORD);
                        logger.info(AccountType.GUEST.getWelcomeMessage());
                        break;
                    case EXIT:
                        logger.info("Exiting the program.");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        logger.warning("Invalid choice. Please enter a valid option.");
                        break;
                }

                if (currentAccount != null) {
                    handleUserActions(scanner, shoppingCart, productDatabase, shippingOption, cart);
                }
            } catch (InputMismatchException e) {
                logger.warning("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            } catch (Exception e) {
                logger.warning("An unexpected error occurred: " + e.getMessage());
                break;
            }
        }
    }


    private static Account signUp(Scanner scanner, CustomerDatabase customerDatabase) {


        logger.info("Enter a username:");
        String username = scanner.next();

        if (customerDatabase.getAccount(username) != null) {
            logger.info("Username already exists. Please Sign In.");
            return null;
        }
        try {
            AccountType accountType = AccountType.NEW;

            logger.info("Enter a password:");
            String password = scanner.next();

            Account newAccount = new NewCustomer(username, password);

            customerDatabase.addAccount(newAccount);
            logger.info("Account created successfully. Welcome, " + username + "!");
            logger.info(AccountType.NEW.getWelcomeMessage());
            return newAccount;
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid input: " + e.getMessage());
            logger.info("Please enter valid credentials.");
            return null;
        }
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
            logger.info("Authentication successful. " + username + "!");
            logger.info(AccountType.REGISTERED.getWelcomeMessage());
            return existingAccount;
        } else {
            logger.warning("Authentication failed. Please check your credentials.");
            return null;
        }
    }

    private static void displayMainMenu() {
        System.out.println("==== Welcome to Solvd Shop ===");
        System.out.println("1. Sign Up");
        System.out.println("2. Sign In");
        System.out.println("3. Continue as Guest");
        System.out.println("4. Exit");
        System.out.println("************************************");
        logger.info("Enter your choice: ");
    }

    private static void handleUserActions(Scanner scanner, Cart<Product> shoppingCart, ProductDatabase productDatabase, ShippingOption shippingOption, Cart<Product> cart) {
        int userChoice;

        do {
            displayUserMenu();
            try {
                userChoice = scanner.nextInt();
            } catch (InputMismatchException e) {
                logger.warning("Invalid input. Please enter a valid number.");
                scanner.nextLine();
                userChoice = 0;
                ;
            }
            switch (userChoice) {
                case VIEW_PRODUCTS:
                    displayProductCatalog(productDatabase);
                    System.out.print("Enter the Product ID to add to cart: ");
                    int productIdToAdd = scanner.nextInt();
                    System.out.print("Enter the quantity to add to cart: ");
                    int quantityToAdd = scanner.nextInt();
                    addProductToCart(productIdToAdd, quantityToAdd, productDatabase, shoppingCart, cart);
                    break;
                case VIEW_CART:
                    displayShoppingCart(shoppingCart);
                    System.out.print("Enter your cart action (1: Update Quantity, 2: Remove Product, 3: Clear Cart): ");
                    int cartAction = scanner.nextInt();
                    performCartAction(cartAction, shoppingCart);
                    break;
                case SET_SHIPPING_OPTION:
                    displayShippingOptions();
                    logger.info("Enter your choice: ");
                    int shippingOptionChoice = scanner.nextInt();
                    setShippingOption(shippingOptionChoice, shoppingCart);
                    break;
                case SET_PAYMENT_METHOD:
                    displayPaymentMethods();
                    logger.info("Enter your choice: ");
                    int paymentMethodChoice = scanner.nextInt();
                    setPaymentMethod(paymentMethodChoice, shoppingCart);
                    break;
                case CHECKOUT:
                    processCheckout(shoppingCart, shippingOption);
                    break;
                case EXIT_USER_MENU:
                    logger.info("Exiting the shopping system. Thank you!");
                    break;
                default:
                    logger.warning("Invalid choice. Please enter a valid option.");
            }
        } while (userChoice != EXIT_USER_MENU);
    }

    private static void setShippingOption(int choice, Cart<Product> shoppingCart) {

        try {
            ShippingOption shippingOption = null;

            switch (choice) {
                case 1:
                    shippingOption = new StandardShipping("Standard Shipping", 5.99, "3-5 days");
                    break;
                case 2:
                    shippingOption = new ExpressShipping("Express Shipping", 12.99, "1-2 days");
                    break;
                case 3:
                    break;
                default:
                    logger.warning("Invalid choice. Please enter a valid option.");
                    logger.info("Please choose a valid shipping option (1, 2, or 3).");
                    return;
            }

            if (shippingOption != null) {
                shoppingCart.setShippingOption(shippingOption);
                logger.info("Shipping option set successfully.");
            }
        } catch (Exception e) {
            logger.warning("Error setting shipping option: " + e.getMessage());
        }
    }

    private static ShippingOption createShippingOption(String className, String optionName, double baseFee, String deliveryTime) throws Exception {
        try {
            Class<?> shippingClass = Class.forName(className);
            Constructor<?> shippingConstructor = shippingClass.getConstructor(String.class, double.class, String.class);
            Object shippingObject = shippingConstructor.newInstance(optionName, baseFee, deliveryTime);

            Method setDeliveryTimeMethod = shippingObject.getClass().getMethod("setDeliveryTime", String.class);
            setDeliveryTimeMethod.invoke(shippingObject, "Fast Delivery");

            return (ShippingOption) shippingObject;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            logger.warning("Error creating shipping option: " + e.getMessage());
            return null;
        }
    }

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

        productDatabase.getProductsForDepartmentStream().forEach(product -> {
            logger.info("Product ID: " + product.getProductId());
            logger.info("Name: " + product.getProductName());
            logger.info("Price: $" + product.getPrice());
            System.out.println("-------------");


        });

        System.out.println("************************************");
    }

    private static void addProductToCart(int productId, int quantity, ProductDatabase productDatabase, Cart<Product> shoppingCart, Cart<Product> cart) {
        Product productToAdd = findProductById(productId, productDatabase);

        if (productToAdd != null) {

            double discountAmount = cart.getDiscount().calculateDiscount(productToAdd);
            productToAdd.setPrice(productToAdd.getPrice() - discountAmount);

            shoppingCart.addProduct(productToAdd, quantity);
            logger.info(quantity + " " + productToAdd.getProductName() + "(s) added to the cart with a discount of $" + discountAmount);
        } else {
            logger.warning("Product with ID " + productId + " not found in the catalog.");
        }
    }

    private static Product findProductById(int productId, ProductDatabase productDatabase) {
        return productDatabase.getProductsForDepartmentStream().filter(product -> product.getProductId() == productId).findFirst().orElse(null);
    }

    private static void displayShoppingCart(Cart<Product> shoppingCart) {
        List<CartItem<Product>> cartItems = shoppingCart.getCartItems();

        if (cartItems.isEmpty()) {
            logger.warning("Your shopping cart is empty.");
        } else {
            System.out.println("=== Shopping Cart ===");

            cartItems.forEach(cartItem -> {
                Product product = cartItem.getProduct();
                int quantity = cartItem.getQuantity();

                logger.info("Product ID: " + product.getProductId());
                logger.info("Name: " + product.getProductName());
                logger.info("Price: $" + product.getPrice());
                logger.info("Quantity: " + quantity);
                logger.info("Total Price: $" + product.getPrice() * quantity);
                System.out.println("-------------");
            });

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

    private static void displayPaymentMethods() {
        System.out.println("=== Payment Methods ===");
        System.out.println("1. Credit Card");
        System.out.println("2. PayPal");
        System.out.println("3. Go back to main menu");
        System.out.println("************************************");
        logger.info("Enter your choice: ");
    }

    private static void setPaymentMethod(int choice, Cart<Product> shoppingCart) {
        Scanner scanner = new Scanner(System.in);
        Payment selectedPayment = createPayment(choice, scanner);

        if (selectedPayment != null) {
            shoppingCart.setPayment(selectedPayment);
            logger.info("Payment method set successfully.");
        }
    }

    private static Payment createPayment(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                return createCreditCardPayment(scanner);
            case 2:
                return createPayPalPayment(scanner);
            default:
                logger.warning("Invalid choice. Please enter a valid option.");
                return null;
        }
    }

    private static CreditCard createCreditCardPayment(Scanner scanner) {
        CreditCard creditCard = new CreditCard();

        logger.info("Enter Credit Card Number:");
        String cardNumber = scanner.next();
        creditCard.setCardNumber(cardNumber);

        logger.info("Enter Cardholder Name:");
        String cardholderName = scanner.next();
        creditCard.setCardHolderName(cardholderName);

        logger.info("Enter CVV:");
        int cvv = scanner.nextInt();
        creditCard.setCardCvv(cvv);

        return creditCard;
    }


    private static PayPal createPayPalPayment(Scanner scanner) {
        PayPal payPal = null;

        try {
            logger.info("Enter PayPal Email:");
            String email = scanner.next();

            logger.info("Enter PayPal Password:");
            String password = scanner.next();

            payPal = new PayPal(email, password);
        } catch (Exception e) {
            logger.warning("Invalid input for PayPal. Please try again.");
        }

        return payPal;
    }

    private static void processCheckout(Cart<Product> shoppingCart, ShippingOption shippingOption) {
        if (shoppingCart.getCartItems().isEmpty()) {
            logger.info("Your shopping cart is empty. Please add items before checking out.");
            return;
        }

        displayShoppingCart(shoppingCart);

        logger.info("Transaction successful! Thank you for your purchase.");
        shoppingCart.getCartItems().clear();
        System.exit(0);
    }

}