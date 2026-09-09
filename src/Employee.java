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
		// pass
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
