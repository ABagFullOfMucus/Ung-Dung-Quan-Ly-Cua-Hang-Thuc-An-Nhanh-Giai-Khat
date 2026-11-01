package persistence.converter;

import model.employee.Employee;
import model.employee.FullTimeEmployee;
import model.employee.PartTimeEmployee;
import model.employee.Role;
import persistence.converter.base.BaseTextConverter;

public class EmployeeConverter extends BaseTextConverter<Employee> {
    private static final String FULL_TIME = "FULLTIME";
    private static final String PART_TIME = "PARTTIME";

    /**
     * (non-Javadoc)
     * định dạng của nó là String
     * id|name|phoneNumber|address|Role|monthlySalary.(FullTime)
     * id|name|phoneNumber|address|Role|hourlyRate|hoursWorked.(PartTime)
     * 
     * @see persistence.TextConverter#toLine(java.lang.Object)
     */
    @Override
    public String toLine(Employee entity) {
        if (entity instanceof FullTimeEmployee) {
            FullTimeEmployee e = (FullTimeEmployee) entity;
            return join(e.getId(), e.getName(), e.getPhoneNumber(), e.getAddress(),
                    e.getRole(), FULL_TIME, e.getMonthlySalary());
        }
        if (entity instanceof PartTimeEmployee) {
            PartTimeEmployee e = (PartTimeEmployee) entity;
            return join(e.getId(), e.getName(), e.getPhoneNumber(), e.getAddress(),
                    e.getRole(), PART_TIME, e.getHourlyRate(), e.getHoursWorked());
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

            if (type.equals(FULL_TIME) && parts.length == 7) {
                double monthlySalary = Double.parseDouble(parts[6]);
                return new FullTimeEmployee(id, name, phoneNumber, address, role, monthlySalary);
            } else if (type.equals(PART_TIME) && parts.length == 8) {
                double hourlyRate = Double.parseDouble(parts[6]);
                double hoursWorked = Double.parseDouble(parts[7]);
                return new PartTimeEmployee(id, name, phoneNumber, address, role, hourlyRate, hoursWorked);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
