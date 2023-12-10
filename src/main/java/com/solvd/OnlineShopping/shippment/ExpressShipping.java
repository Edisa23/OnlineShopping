package com.solvd.OnlineShopping.shippment;

import com.solvd.OnlineShopping.shopping.Cart;

import java.util.logging.Logger;


public class ExpressShipping extends Shipping {
    private static final Logger logger = Logger.getLogger(ExpressShipping.class.getName());
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
    public double calculateShippingCost(Cart<?> cart) {
        return baseFee;
    }


}

