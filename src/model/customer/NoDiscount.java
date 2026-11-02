package model.customer;

import interfaces.DiscountPolicy;

public class NoDiscount implements DiscountPolicy {
    @Override
    public double apply(double subtotal, Customer customer) {
        return subtotal;
    }
}
