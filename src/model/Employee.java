package model;

public class Employee extends Person {
	private String role;
	
	private double salary;
	
	
	// constructor
	
	public Employee() {
		super();
	}
	
	public Employee(String id, String name, String phoneNumber, String role, double salary) {
		super(id, name, phoneNumber);
		this.role = role;
		this.salary = salary;
	}
	
	@Override
	public void displayInfo() {
		System.out.println("Tên nhân viên: " + getName() + " (ID: " + getID() + ")");
		System.out.println("Số điện thoại: " + getPhoneNumber());
		System.out.println("Chức vụ: " + getRole());
		System.out.println("Lương: " + getSalary());
	}
	
	@Override
	public String toFileString() {
		// Định dạng: EMPLOYEE,<id>,<name>,<phoneNumber>,<role>,<salary>
		return "EMPLOYEE," + getID() + "," + getName() + "," + getPhoneNumber() + "," + getRole() + "," + getSalary();
	}
	
	@Override
	public void fromFileString(String line) {
		String[] st = line.split(",");
		
		setID(st[1]);
		setName(st[2]);
		setPhoneNumber(st[3]);
		this.role = st[4];
		this.salary = Double.parseDouble(st[5]);
	}
	
	
	// setter
	public void setRole(String role) {
		this.role = role;
	}
	
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	// getter
	
	public String getRole() {
		return this.role;
	}
	
	public double getSalary() {
		return this.salary;
	}
	
}
