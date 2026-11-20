package service.interfaces;

import common.Result;
import model.employee.Employee;

public interface IEmployeeService extends ICrudService<Employee> {
    Result<Employee> findByUsername(String username);

    Result<Employee> createAccount(String empId, String username, String rawPassword);

    Result<Employee> changePassword(String empId, String oldPassword, String newPassword);
}