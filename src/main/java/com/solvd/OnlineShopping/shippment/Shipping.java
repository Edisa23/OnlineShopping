package com.solvd.OnlineShopping.shippment;

import com.solvd.OnlineShopping.exception.InvalidShippingOptionException;

import java.util.logging.Logger;

public abstract class Shipping implements ShippingOption {
    private static final Logger logger = Logger.getLogger(Shipping.class.getName());

    protected String optionName;
    protected double baseFee;

    public Shipping(String optionName, double baseFee) {
        this.optionName = optionName;
        this.baseFee = baseFee;
    }

    @Override
    public double calculateShippingFee() {

        return baseFee;
    }

    @Override
    public void displayOptionDetails() {
        logger.info("Shipping Option: " + optionName);
        logger.info("Base Fee: $" + baseFee);
    }

    protected void validateDeliveryTime(String deliveryTime) throws InvalidShippingOptionException {
        if (deliveryTime == null || deliveryTime.trim().isEmpty()) {
            throw new InvalidShippingOptionException("Invalid delivery time.");
        }
    }
}