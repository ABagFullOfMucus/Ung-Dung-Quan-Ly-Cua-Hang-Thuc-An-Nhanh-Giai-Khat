package service;

import model.Admin;
import model.Employee;
import utils.FileHandler;

public class EmployeeManager {
	private Employee[] employees;
	
	private int count;
	
	// constructor
	public EmployeeManager() {
		this.employees = new Employee[1];
		this.count = 0;
	}
	
	private void resize() {
		int l = employees.length * 2;
		Employee[] tmp = new Employee[l];
		
		for (int i = 0; i < count; i++) {
			tmp[i] = employees[i];
		}
		
		employees = tmp;
	}
	
	public boolean addEmployee(Employee e) {
		if (findEmployee(e.getID()) != null) {
			return false;
		}
		
		if (count >= employees.length) {
			resize(); 
		}
		
		employees[count] = e;
		count += 1;
		return true;
	}
	
	public Employee findEmployee(String id) {
		for (int i = 0; i < count; i++) {
			if (employees[i].getID().equals(id))
				return employees[i];
		}
		return null;
	}
	
	public boolean removeEmployee(String id) {
		int f = -1;
		
		for (int i = 0; i < count; i++) {
			if (employees[i].getID().equals(id)) {
				f = i;
				break; 
			}
		}
		
		if (f == -1) return false;
		
		for (int i = f; i < count - 1; i++) 
			employees[i] = employees[i + 1];
		
		
		employees[count - 1] = null;
		count -= 1;
		
		return true;
	}
	
	public void displayAllEmployees() {
		if (count == 0) {
			System.out.println("Danh sách trống!");
			return;
		}
		
		System.out.println("Danh sách nhân viên: ");
		for (int i = 0; i < count; i++) {
			employees[i].displayInfo();
		}
	}
	
	public void findHighestPaidEmployee() {
		if (count == 0) {
			System.out.println("Danh sách trống!");
			return;
		}
		
		double luongFlag = employees[0].getSalary();
		for (int i = 1; i < count; i++) {
			if (employees[i].getSalary() > luongFlag) 
				luongFlag = employees[i].getSalary();
		}
		
		System.out.println("===== Danh sách nhân viên có lương cao nhất =====");
		for (int i = 0; i < count; i++) {
			if (luongFlag == employees[i].getSalary()) {
				employees[i].displayInfo();
			}
		}
	}
	
	public void searchByRole(String role) {
		if (count == 0) {
			System.out.println("Danh sách trống!");
			return;
		}
		
		System.out.println("===== Danh sách nhân viên có chức vụ cần tìm =====");
		for (int i = 0; i < count; i++) {
			if (employees[i].getRole().equals(role)) {
				employees[i].displayInfo();
			}
		}
	}
	
	// Lưu / đọc file
	public void saveToFile(String fileName) {
		FileHandler.saveToFile(fileName, employees, count);
	}
	
	public void loadFromFile(String fileName) {
		String[] lines = FileHandler.readLines(fileName);
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			
			if (line.startsWith("ADMIN")) {
				Admin a = new Admin();
				a.fromFileString(line);
				addEmployee(a);
			} else if (line.startsWith("EMPLOYEE")) {
				Employee e = new Employee();
				e.fromFileString(line);
				addEmployee(e);
			}
		}
	}
	
	// getters
	public int getCount() {
		return this.count;
	}
	
	public Employee[] getEmployees() {
		return this.employees;
	}
}
