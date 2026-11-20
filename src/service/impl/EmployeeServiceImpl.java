package service.impl;

import common.Result;
import model.employee.Account;
import model.employee.Employee;
import model.employee.FullTimeEmployee;
import model.employee.PartTimeEmployee;
import model.employee.Role;
import persistence.Repository;
import service.base.CrudService;
import service.interfaces.IEmployeeService;
import utils.Validator;

public class EmployeeServiceImpl extends CrudService<Employee> implements IEmployeeService {

    public EmployeeServiceImpl(Repository<Employee> repository) {
        super(repository);
    }

    @Override
    protected String idPrefix() {
        return "NV";
    }

    @Override
    protected Employee[] newArray(int size) {
        return new Employee[size];
    }

    @Override
    protected String validate(Employee employee) {
        if (!Validator.isNotBlank(employee.getName())) {
            return "Tên nhân viên không được để trống";
        }
        if (!Validator.isSafeText(employee.getName())) {
            return "Tên nhân viên chứa ký tự không hợp lệ (| hoặc xuống dòng)";
        }
        if (!Validator.isValidPhone(employee.getPhoneNumber())) {
            return "Số điện thoại nhân viên không hợp lệ (10 số bắt đầu bằng 0 hoặc +84)";
        }
        if (!Validator.isSafeText(employee.getAddress())) {
            return "Địa chỉ nhân viên chứa ký tự không hợp lệ (| hoặc xuống dòng)";
        }
        if (employee.getRole() == null) {
            return "Vai trò nhân viên không được để trống";
        }

        String payError = validatePay(employee);
        if (payError != null) {
            return payError;
        }

        Account acc = employee.getAccount();
        if (acc != null && acc.getUsername() != null && !acc.getUsername().isEmpty()) {
            String usernameError = validateUsername(acc.getUsername(), employee);
            if (usernameError != null) {
                return usernameError;
            }
            if (!Validator.isNotBlank(acc.getPasswordHash())) {
                return "Tài khoản nhân viên phải có mật khẩu hợp lệ";
            }
        }
        return null;
    }

    private String validatePay(Employee employee) {
        if (employee instanceof FullTimeEmployee) {
            FullTimeEmployee fullTime = (FullTimeEmployee) employee;
            double salary = fullTime.getMonthlySalary();
            if (!Double.isFinite(salary) || salary < 0) {
                return "Lương tháng của nhân viên toàn thời gian phải lớn hơn hoặc bằng 0";
            }
            return null;
        }
        if (employee instanceof PartTimeEmployee) {
            PartTimeEmployee partTime = (PartTimeEmployee) employee;
            double rate = partTime.getHourlyRate();
            double hours = partTime.getHoursWorked();
            if (!Double.isFinite(rate) || rate <= 0) {
                return "Đơn giá theo giờ phải lớn hơn 0";
            }
            if (!Double.isFinite(hours) || hours < 0) {
                return "Số giờ làm việc không được âm";
            }
            return null;
        }
        return "Loại nhân viên không được hỗ trợ";
    }

    private String validateUsername(String username, Employee excludeEmployee) {
        if (!Validator.isValidUsername(username)) {
            return "Tên đăng nhập phải dài 3-30 ký tự, chỉ gồm chữ, số, '.', '_', '-' và không có khoảng trắng";
        }

        Employee[] all = getAll();
        for (int i = 0; i < all.length; i++) {
            Employee existing = all[i];
            if (existing != null
                    && !isSameId(existing, excludeEmployee)
                    && existing.getAccount() != null
                    && existing.getAccount().getUsername() != null
                    && existing.getAccount().getUsername().equalsIgnoreCase(username)) {
                return "Tên đăng nhập đã tồn tại";
            }
        }
        return null;
    }

    private int countActiveAdmins() {
        Employee[] all = getAll();
        int count = 0;
        for (int i = 0; i < all.length; i++) {
            if (all[i] != null && all[i].isActive() && all[i].getRole() == Role.ADMIN) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected String checkDeactivatable(Employee employee) {
        if (employee.getRole() == Role.ADMIN && countActiveAdmins() <= 1) {
            return "Không thể vô hiệu hóa quản trị viên (ADMIN) đang hoạt động cuối cùng";
        }
        return null;
    }

    @Override
    public Result<Employee> update(Employee entity) {
        if (entity != null && Validator.isNotBlank(entity.getId())) {
            Employee existing = getRepository().findById(entity.getId().trim());
            if (existing != null
                    && existing.isActive()
                    && existing.getRole() == Role.ADMIN
                    && entity.getRole() != Role.ADMIN
                    && countActiveAdmins() <= 1) {
                return Result.fail("Không thể hạ quyền quản trị viên (ADMIN) đang hoạt động cuối cùng");
            }
        }
        return super.update(entity);
    }

    @Override
    public Result<Employee> findByUsername(String username) {
        if (!Validator.isNotBlank(username)) {
            return Result.fail("Tên đăng nhập không hợp lệ");
        }
        String target = username.trim();
        Employee[] all = getAll();
        for (int i = 0; i < all.length; i++) {
            Employee emp = all[i];
            if (emp != null
                    && emp.getAccount() != null
                    && emp.getAccount().getUsername() != null
                    && target.equalsIgnoreCase(emp.getAccount().getUsername())) {
                return Result.ok(emp);
            }
        }
        return Result.fail("Không tìm thấy nhân viên với tên đăng nhập " + target);
    }

    @Override
    public Result<Employee> createAccount(String empId, String username, String rawPassword) {
        if (!Validator.isNotBlank(empId)) {
            return Result.fail("Mã nhân viên không hợp lệ");
        }
        String cleanEmpId = empId.trim();
        Employee emp = getRepository().findById(cleanEmpId);
        if (emp == null) {
            return Result.fail("Không tìm thấy nhân viên có mã " + cleanEmpId);
        }
        if (!emp.isActive()) {
            return Result.fail("Nhân viên đã bị vô hiệu hóa, không thể tạo tài khoản");
        }
        if (emp.getAccount() != null && Validator.isNotBlank(emp.getAccount().getUsername())) {
            return Result.fail("Nhân viên này đã có tài khoản");
        }

        String usernameError = validateUsername(username, emp);
        if (usernameError != null) {
            return Result.fail(usernameError);
        }
        if (!Validator.isNotBlank(rawPassword) || rawPassword.length() < 6) {
            return Result.fail("Mật khẩu phải có ít nhất 6 ký tự và không chỉ gồm khoảng trắng");
        }

        Account previousAccount = emp.getAccount();
        emp.setAccount(new Account(username, rawPassword));
        if (!getRepository().update(emp)) {
            emp.setAccount(previousAccount);
            return Result.fail("Không thể lưu tài khoản nhân viên");
        }
        return Result.ok(emp);
    }

    @Override
    public Result<Employee> changePassword(String empId, String oldPassword, String newPassword) {
        if (!Validator.isNotBlank(empId)) {
            return Result.fail("Mã nhân viên không hợp lệ");
        }
        String cleanEmpId = empId.trim();
        Employee emp = getRepository().findById(cleanEmpId);
        if (emp == null) {
            return Result.fail("Không tìm thấy nhân viên có mã " + cleanEmpId);
        }
        if (!emp.isActive()) {
            return Result.fail("Nhân viên đã bị vô hiệu hóa, không thể đổi mật khẩu");
        }
        Account acc = emp.getAccount();
        if (acc == null || !Validator.isNotBlank(acc.getUsername())) {
            return Result.fail("Nhân viên chưa có tài khoản");
        }
        if (oldPassword == null || oldPassword.isEmpty()) {
            return Result.fail("Vui lòng nhập mật khẩu cũ");
        }
        if (!Validator.isNotBlank(newPassword) || newPassword.length() < 6) {
            return Result.fail("Mật khẩu mới phải có ít nhất 6 ký tự và không chỉ gồm khoảng trắng");
        }

        String previousHash = acc.getPasswordHash();
        if (!acc.changePassword(oldPassword, newPassword)) {
            return Result.fail("Mật khẩu cũ không chính xác");
        }
        if (!getRepository().update(emp)) {
            acc.setPasswordHash(previousHash);
            return Result.fail("Không thể lưu mật khẩu mới");
        }
        return Result.ok(emp);
    }
}