package interfaces;

import model.customer.Customer;

public interface DiscountPolicy {
    double apply(double subtotal, Customer customer);
}
