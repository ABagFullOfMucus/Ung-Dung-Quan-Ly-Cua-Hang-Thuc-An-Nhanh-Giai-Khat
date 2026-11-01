package model;

import interfaces.DiscountPolicy;

public class MemberDiscount implements DiscountPolicy {
    @Override
    public double apply(double subtotal, Customer customer) {
        if (customer == null || customer.getTier() == null) {
            return subtotal;
        }

        double discountRate = 0.0;
        switch (customer.getTier()) {
            case SILVER:
                discountRate = 0.05;
                break;
            case GOLD:
                discountRate = 0.10;
                break;
            case NONE:
            default:
                discountRate = 0.0;
                break;
        }

        return subtotal * (1.0 - discountRate);
    }
}
