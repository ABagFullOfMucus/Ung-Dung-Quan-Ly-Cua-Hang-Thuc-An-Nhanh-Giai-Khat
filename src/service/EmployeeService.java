package service;

import model.employee.Employee;
import persistence.Repository;
import service.base.CrudService;

public class EmployeeService extends CrudService<Employee> {

    public EmployeeService(Repository<Employee> repository) {
        super(repository);
    }

    @Override
    protected String newId() {
        return nextId("NV");
    }
}