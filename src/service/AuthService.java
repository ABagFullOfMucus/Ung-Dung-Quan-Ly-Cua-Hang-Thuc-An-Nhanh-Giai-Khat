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

        Employee[] employees = employeeService.getAll();
        if (employees == null) {
            return Result.fail("Sai tên đăng nhập hoặc mật khẩu");
        }

        for (Employee emp : employees) {
            if (emp == null || !emp.isActive()) {
                continue;
            }
            Account acc = emp.getAccount();
            if (acc == null || acc.getUsername() == null) {
                continue;
            }
            if (username.equals(acc.getUsername())) {
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
