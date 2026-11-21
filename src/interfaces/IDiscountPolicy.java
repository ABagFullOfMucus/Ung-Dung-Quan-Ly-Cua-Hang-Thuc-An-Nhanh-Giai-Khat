package interfaces;

import model.customer.Customer;

public interface IDiscountPolicy {
    double apply(double subtotal, Customer customer);
}
