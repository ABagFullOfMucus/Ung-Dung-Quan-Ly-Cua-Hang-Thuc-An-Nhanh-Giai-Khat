package interfaces;

import model.Customer;

public interface DiscountPolicy {
    double apply(double subtotal, Customer customer);
}
