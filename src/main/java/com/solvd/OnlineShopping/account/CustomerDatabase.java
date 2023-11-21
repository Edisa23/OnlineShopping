package com.solvd.OnlineShopping.account;

import java.util.HashMap;
import java.util.Map;

public class CustomerDatabase {
    private final Map<String, Customer> customers;

    public CustomerDatabase() {
        this.customers = new HashMap<>();
    }

    public void registerCustomer(Customer customer) {
        customers.put(customer.getUsername(), customer);
    }

    public Customer findCustomer(String username) {
        return customers.get(username);
    }
}

