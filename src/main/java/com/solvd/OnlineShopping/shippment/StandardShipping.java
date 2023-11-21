package com.solvd.OnlineShopping.shippment;


import com.solvd.OnlineShopping.exception.InvalidShippingOptionException;
import com.solvd.OnlineShopping.payment.CreditCard;

import java.util.logging.Logger;

public class StandardShipping extends Shipping {
    private static final Logger logger = Logger.getLogger(CreditCard.class.getName());
    private static final double WEIGHT_COST_FACTOR = 0.1;
    private String deliveryTime;
    protected String optionName;
    protected double baseFee;

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public double getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(double baseFee) {
        this.baseFee = baseFee;
    }

    public StandardShipping(String optionName, double baseFee, String deliveryTime) {
        super(optionName, baseFee);
        this.deliveryTime = deliveryTime;

    }


    public void setDeliveryTime(String deliveryTime) {
        validateDeliveryTime(deliveryTime);
        this.deliveryTime = deliveryTime;
    }

    @Override
    public double calculateShippingCost(double weight) {
        return baseFee + (weight * WEIGHT_COST_FACTOR);
    }

    @Override
    public void displayOptionDetails() {
        super.displayOptionDetails();
        logger.info("Estimated Delivery Time: " + deliveryTime);
    }


    private void validateDeliveryTime(String deliveryTime) throws InvalidShippingOptionException {

        if (deliveryTime == null || deliveryTime.trim().isEmpty()) {
            throw new InvalidShippingOptionException("Invalid delivery time.");
        }
    }
}