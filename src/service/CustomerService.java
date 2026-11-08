package service;

import model.customer.Customer;
import persistence.Repository;
import service.base.CrudService;

public class CustomerService extends CrudService<Customer> {

    public CustomerService(Repository<Customer> repository) {
        super(repository);
    }
    
    @Override
    protected String newId() {
        return nextId("KH");
    }
}