package service.interfaces;

import common.Result;
import model.employee.Employee;

public interface IAuthService {
    Result<Employee> login(String username, String password);
}