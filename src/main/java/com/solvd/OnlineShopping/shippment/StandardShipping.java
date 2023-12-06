package com.solvd.OnlineShopping.shippment;

import java.util.logging.Logger;

public class StandardShipping extends Shipping {
    private static final Logger logger = Logger.getLogger(StandardShipping.class.getName());
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

    public void setDeliveryTime(String deliveryTime) {
        validateDeliveryTime(deliveryTime);
        this.deliveryTime = deliveryTime;
    }

    public StandardShipping(String optionName, double baseFee, String deliveryTime) {
        super(optionName, baseFee);
        validateDeliveryTime(deliveryTime);
        this.deliveryTime = deliveryTime;

    }


    @Override
    public void displayOptionDetails() {
        super.displayOptionDetails();
        logger.info("Estimated Delivery Time: " + deliveryTime);
    }

}