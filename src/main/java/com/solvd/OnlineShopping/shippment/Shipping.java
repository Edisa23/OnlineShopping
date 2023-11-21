package com.solvd.OnlineShopping.shippment;
import com.solvd.OnlineShopping.payment.CreditCard;

import java.util.logging.Logger;
public abstract class Shipping implements ShippingOption {
    private static final Logger logger = Logger.getLogger(CreditCard.class.getName());
    private static final double WEIGHT_COST_FACTOR = 0.1;
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

    public double calculateShippingCost(double weight) {
        return baseFee + (weight * WEIGHT_COST_FACTOR);
    }

    @Override
    public void displayOptionDetails() {
        logger.info("Shipping Option: " + optionName);
        logger.info("Base Fee: $" + baseFee);
    }


}