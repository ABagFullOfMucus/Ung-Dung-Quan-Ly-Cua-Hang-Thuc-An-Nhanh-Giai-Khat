package model.customer;

import interfaces.DiscountPolicy;

public class MemberDiscount implements DiscountPolicy {
    @Override
    public double apply(double subtotal, Customer customer) {
        if (customer == null || customer.getTier() == null) {
            return subtotal;
        }

        switch (customer.getTier()) {
            case SILVER:
                return subtotal * 0.95;
            case GOLD:
                return subtotal * 0.90;
            case NONE:
            default:
                return subtotal;
        }
    }
}
