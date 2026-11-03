package persistence.converter;

import model.employee.Account;
import model.employee.Employee;
import model.employee.FullTimeEmployee;
import model.employee.PartTimeEmployee;
import model.employee.Role;
import persistence.converter.base.BaseTextConverter;

public class EmployeeConverter extends BaseTextConverter<Employee> {
    private static final String FULL_TIME = "FULLTIME";
    private static final String PART_TIME = "PARTTIME";

    /**
     * id|name|phoneNumber|address|Role|FULLTIME|monthlySalary|username|passwordHash
     * id|name|phoneNumber|address|Role|PARTTIME|hourlyRate|hoursWorked|username|passwordHash
     */
    @Override
    public String toLine(Employee entity) {
        if (entity == null) {
            return "";
        }
        Account acc = entity.getAccount();
        String username = acc == null || acc.getUsername() == null ? "" : acc.getUsername();
        String passwordHash = acc == null || acc.getPasswordHash() == null ? "" : acc.getPasswordHash();

        if (entity instanceof FullTimeEmployee) {
            FullTimeEmployee e = (FullTimeEmployee) entity;
            return join(e.getId(), e.getName(), e.getPhoneNumber(), e.getAddress(),
                    e.getRole(), FULL_TIME, e.getMonthlySalary(), username, passwordHash);
        }
        if (entity instanceof PartTimeEmployee) {
            PartTimeEmployee e = (PartTimeEmployee) entity;
            return join(e.getId(), e.getName(), e.getPhoneNumber(), e.getAddress(),
                    e.getRole(), PART_TIME, e.getHourlyRate(), e.getHoursWorked(), username, passwordHash);
        }
        return "";
    }

    @Override
    public Employee fromLine(String line) {
        String[] parts = split(line);
        if (parts == null || parts.length < 6) {
            return null;
        }

        try {
            String id = parts[0];
            String name = parts[1];
            String phoneNumber = parts[2];
            String address = parts[3];
            Role role = Role.valueOf(parts[4].toUpperCase());
            String type = parts[5].toUpperCase();

            Employee employee = null;
            if (type.equals(FULL_TIME) && parts.length >= 7) {
                double monthlySalary = Double.parseDouble(parts[6]);
                employee = new FullTimeEmployee(id, name, phoneNumber, address, role, monthlySalary);
            } else if (type.equals(PART_TIME) && parts.length >= 8) {
                double hourlyRate = Double.parseDouble(parts[6]);
                double hoursWorked = Double.parseDouble(parts[7]);
                employee = new PartTimeEmployee(id, name, phoneNumber, address, role, hourlyRate, hoursWorked);
            }

            if (employee == null) {
                return null;
            }

            String username = parts.length > 7 && type.equals(FULL_TIME) ? parts[7]
                    : parts.length > 8 && type.equals(PART_TIME) ? parts[8] : "";
            String passwordHash = parts.length > 8 && type.equals(FULL_TIME) ? parts[8]
                    : parts.length > 9 && type.equals(PART_TIME) ? parts[9] : "";

            if (!username.isEmpty() || !passwordHash.isEmpty()) {
                Account acc = new Account();
                acc.setUsername(username);
                acc.setPasswordHash(passwordHash);
                employee.setAccount(acc);
            }
            return employee;
        } catch (Exception e) {
            return null;
        }
    }
}
