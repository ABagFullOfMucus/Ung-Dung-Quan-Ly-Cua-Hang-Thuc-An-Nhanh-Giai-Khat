package service.interfaces;

import common.Result;
import model.customer.Customer;

public interface ICustomerService extends ICrudService<Customer> {
    Result<Customer> earnPoints(String customerId, double amountSpent);
}