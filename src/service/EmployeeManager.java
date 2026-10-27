package service;

import java.util.Arrays;
import model.Admin;
import model.Employee;
import utils.FileHandler;

public class EmployeeManager {
    private Employee[] employees;
    private int count;

    // Constructor
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
        if (e == null || findEmployee(e.getID()) != null) {
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
        if (id == null) return null;        
        for (int i = 0; i < count; i++) {
            if (employees[i].getID().equalsIgnoreCase(id)) {
                return employees[i];
            }
        }
        return null;
    }

    public boolean removeEmployee(String id) {
        Employee e = findEmployee(id);
        if (e == null || !e.isStatus()) {
            return false;
        }
        e.setStatus(false);
        return true;
    }

    public void displayAllEmployees() {
        if (count == 0) {
            System.out.println("Danh sách nhân viên trống.");
            return;
        }

        boolean hasActive = false;
        for (int i = 0; i < count; i++) {
            if (employees[i].isStatus()) {
                employees[i].displayInfo();
                hasActive = true;
            }
        }

        if (!hasActive) {
            System.out.println("Không có nhân viên nào đang hoạt động.");
        }
    }

    public Employee findHighestPaidEmployee() {
        Employee highest = null;
        for (int i = 0; i < count; i++) {
            if (employees[i].isStatus()) {
                if (highest == null || employees[i].getSalary() > highest.getSalary()) {
                    highest = employees[i];
                }
            }
        }
        return highest;
    }

    public void searchByRole(String role) {
        if(role == null) return;
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (employees[i].isStatus() && employees[i].getRole().equalsIgnoreCase(role)) {
                employees[i].displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("Không tìm thấy nhân viên có chức vụ: " + role);
        }
    }

    public void saveToFile(String fileName) {
        FileHandler.saveToFile(fileName, employees, count);
    }

    public void loadFromFile(String fileName) {
        String[] lines = FileHandler.readLines(fileName);

        if (lines == null) {
            return;
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null && !lines[i].trim().isEmpty()) {
                Employee e;

                if (lines[i].startsWith("ADMIN")) {
                    e = new Admin();
                } else {
                    e = new Employee();
                }

                e.fromFileString(lines[i]);
                addEmployee(e);
            }
        }
    }

    public Employee[] getEmployees() {
        return Arrays.copyOf(employees, count);
    }

    public int getCount() {
        return count;
    }
}
