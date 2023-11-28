package com.solvd.OnlineShopping.shippment;

import java.util.logging.Logger;

import com.solvd.OnlineShopping.payment.CreditCard;

public class ExpressShipping extends Shipping {
    private static final Logger logger = Logger.getLogger(CreditCard.class.getName());
    private String deliveryTime;

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {

        this.deliveryTime = deliveryTime;
    }

    public ExpressShipping(String optionName, double baseFee, String deliveryTime) {
        super(optionName, baseFee);
        validateDeliveryTime(deliveryTime);
        this.deliveryTime = deliveryTime;

    }

    @Override
    public void displayOptionDetails() {
        super.displayOptionDetails();
        logger.info("Estimated Delivery Time: " + deliveryTime);
    }

    @Override
    public double calculateShippingCost(double weight) {

        return baseFee * weight * 1.2;
    }

}

