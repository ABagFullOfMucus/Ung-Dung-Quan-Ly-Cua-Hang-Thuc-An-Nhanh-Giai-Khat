package model.customer;

import interfaces.IDiscountPolicy;

public class NoDiscount implements IDiscountPolicy {
    @Override
    public double apply(double subtotal, Customer customer) {
        return subtotal;
    }
}
