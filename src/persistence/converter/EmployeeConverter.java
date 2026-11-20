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
     * id|name|phoneNumber|address|Role|FULLTIME|monthlySalary|username|passwordHash|active
     * id|name|phoneNumber|address|Role|PARTTIME|hourlyRate|hoursWorked|username|passwordHash|active
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
                    e.getRole(), FULL_TIME, e.getMonthlySalary(), username, passwordHash, e.isActive());
        }
        if (entity instanceof PartTimeEmployee) {
            PartTimeEmployee e = (PartTimeEmployee) entity;
            return join(e.getId(), e.getName(), e.getPhoneNumber(), e.getAddress(),
                    e.getRole(), PART_TIME, e.getHourlyRate(), e.getHoursWorked(), username, passwordHash,
                    e.isActive());
        }
        return "";
    }

    @Override
    public Employee fromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = split(line);
        if (parts == null || parts.length < 10) {
            return null;
        }

        try {
            String id = parts[0];
            if (id.isEmpty()) {
                return null;
            }
            String name = parts[1];
            String phoneNumber = parts[2];
            String address = parts[3];
            Role role = Role.valueOf(parts[4].toUpperCase());
            String type = parts[5].toUpperCase();

            Employee employee;
            String username;
            String passwordHash;
            String activeStr;

            if (type.equals(FULL_TIME) && parts.length == 10) {
                double monthlySalary = Double.parseDouble(parts[6]);
                if (!Double.isFinite(monthlySalary) || monthlySalary < 0) {
                    return null;
                }
                employee = new FullTimeEmployee(id, name, phoneNumber, address, role, monthlySalary);
                username = parts[7];
                passwordHash = parts[8];
                activeStr = parts[9];
            } else if (type.equals(PART_TIME) && parts.length == 11) {
                double hourlyRate = Double.parseDouble(parts[6]);
                double hoursWorked = Double.parseDouble(parts[7]);
                if (!Double.isFinite(hourlyRate) || hourlyRate <= 0
                        || !Double.isFinite(hoursWorked) || hoursWorked < 0) {
                    return null;
                }
                employee = new PartTimeEmployee(id, name, phoneNumber, address, role, hourlyRate, hoursWorked);
                username = parts[8];
                passwordHash = parts[9];
                activeStr = parts[10];
            } else {
                return null;
            }

            if (!"true".equalsIgnoreCase(activeStr) && !"false".equalsIgnoreCase(activeStr)) {
                return null;
            }
            boolean active = Boolean.parseBoolean(activeStr);

            if (!username.isEmpty() || !passwordHash.isEmpty()) {
                Account acc = new Account();
                acc.setUsername(username);
                acc.setPasswordHash(passwordHash);
                employee.setAccount(acc);
            }
            if (!active) {
                employee.deactivate();
            }
            return employee;
        } catch (Exception e) {
            return null;
        }
    }
}