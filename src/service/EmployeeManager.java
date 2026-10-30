package service;

import model.Employee;
import model.Role;

public class EmployeeManager {
    private Employee[] employees;
    private int count;

    public EmployeeManager() {
        employees = new Employee[10];
        count = 0;
    }

    private void resize() {
        Employee[] newEmployees = new Employee[employees.length * 2];
        for (int i = 0; i < count; i++) {
            newEmployees[i] = employees[i];
        }
        employees = newEmployees;
    }

    public boolean addEmployee(Employee e) {
        if (e == null || findEmployee(e.getId()) != null) {
            return false;
        }

        if (count >= employees.length) {
            resize();
        }

        employees[count] = e;
        count++;
        return true;
    }

    public Employee findEmployee(String id) {
        if (id == null) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            if (employees[i].getId().equalsIgnoreCase(id)) {
                return employees[i];
            }
        }
        return null;
    }

    public boolean removeEmployee(String id) {
        for (int i = 0; i < count; i++) {
            if (employees[i].getId().equalsIgnoreCase(id)) {
                for (int j = i; j < count - 1; j++) {
                    employees[j] = employees[j + 1];
                }
                employees[count - 1] = null;
                count--;
                return true;
            }
        }
        return false;
    }

    public void displayAllEmployees() {
        if (count == 0) {
            System.out.println("Danh sách nhân viên trống.");
            return;
        }

        System.out.println("Danh sách nhân viên:");
        for (int i = 0; i < count; i++) {
            Employee e = employees[i];
            System.out.println(e.getId() + " | " + e.getName() + " | "
                    + e.getPhoneNumber() + " | " + e.getRole()
                    + " | lương=" + e.calculatePay());
        }
    }

    public Employee findHighestPaidEmployee() {
        Employee highest = null;
        for (int i = 0; i < count; i++) {
            if (highest == null || employees[i].calculatePay() > highest.calculatePay()) {
                highest = employees[i];
            }
        }
        return highest;
    }

    public void searchByRole(Role role) {
        if (role == null) {
            return;
        }

        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (employees[i].getRole() == role) {
                Employee e = employees[i];
                System.out.println(e.getId() + " | " + e.getName() + " | "
                        + e.getPhoneNumber() + " | " + e.getRole());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Không tìm thấy nhân viên có chức vụ: " + role);
        }
    }

    public Employee[] getEmployees() {
        Employee[] result = new Employee[count];
        for (int i = 0; i < count; i++) {
            result[i] = employees[i];
        }
        return result;
    }

    public int getCount() {
        return count;
    }
}
