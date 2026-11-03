package service;

import common.Result;
import model.employee.Account;
import model.employee.Employee;

public class AuthService {

    private final EmployeeService employeeService;

    public AuthService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public Result<Employee> login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return Result.fail("Vui lòng nhập tên đăng nhập và mật khẩu");
        }
        for (Employee emp : employeeService.getAll()) {
            Account acc = emp.getAccount();
            if (acc != null && emp.isActive() && username.equals(acc.getUsername())) {
                if (acc.authenticate(password)) {
                    return Result.ok(emp);
                }
                break;
            }
        }
        // Dùng chung một thông báo để không lộ tài khoản nào tồn tại
        return Result.fail("Sai tên đăng nhập hoặc mật khẩu");
    }
}