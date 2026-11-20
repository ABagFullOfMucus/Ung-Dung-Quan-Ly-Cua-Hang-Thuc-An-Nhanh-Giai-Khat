package service.impl;

import common.Result;
import model.employee.Account;
import model.employee.Employee;
import service.interfaces.IAuthService;
import service.interfaces.IEmployeeService;
import utils.Validator;

public class AuthServiceImpl implements IAuthService {
    private static final String INVALID_CREDENTIALS = "Sai tên đăng nhập hoặc mật khẩu";

    private final IEmployeeService employeeService;

    public AuthServiceImpl(IEmployeeService employeeService) {
        if (employeeService == null) {
            throw new IllegalArgumentException("IEmployeeService không được null");
        }
        this.employeeService = employeeService;
    }

    @Override
    public Result<Employee> login(String username, String password) {
        if (!Validator.isNotBlank(username) || password == null || password.isEmpty()) {
            return Result.fail("Vui lòng nhập tên đăng nhập và mật khẩu");
        }

        Result<Employee> found = employeeService.findByUsername(username.trim());
        if (!found.isSuccess()) {
            return Result.fail(INVALID_CREDENTIALS);
        }

        Employee emp = found.getData();
        if (emp == null || !emp.isActive()) {
            return Result.fail(INVALID_CREDENTIALS);
        }

        Account acc = emp.getAccount();
        if (acc == null || !acc.authenticate(password)) {
            return Result.fail(INVALID_CREDENTIALS);
        }
        return Result.ok(emp);
    }
}