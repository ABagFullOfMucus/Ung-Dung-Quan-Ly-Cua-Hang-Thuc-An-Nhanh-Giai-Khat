package model;

public class Admin extends Employee {
	private String username;
	
	private String password;
	
	// constructor
	
	public Admin() {
		super();
	}
	
	public Admin(String id, String name, String phoneNumber, String username, String password) {
		super(id, name, phoneNumber, "Quản trị viên", 0.0);
		this.username = username;
		this.password = password;
	}
	
	public Admin(String id, String name, String phoneNumber, String role, double salary, String username, String password) {
		super(id, name, phoneNumber, role, salary);
		this.username = username;
		this.password = password;
	}
	
	// Kiểm tra thông tin đăng nhập
	public boolean login(String username, String password) {
		return this.username.equals(username) && this.password.equals(password);
	}
	
	@Override
	public void displayInfo() {
		super.displayInfo();
		System.out.println("Tài khoản: " + getUsername());
	}
	
	@Override
	public String toFileString() {
		// Định dạng: ADMIN,<id>,<name>,<phoneNumber>,<role>,<salary>,<username>,<password>
		return "ADMIN," + getID() + "," + getName() + "," + getPhoneNumber() + "," + getRole() + "," + getSalary() + "," + getUsername() + "," + getPassword();
	}
	
	@Override
	public void fromFileString(String line) {
		String[] st = line.split(",");
		
		setID(st[1]);
		setName(st[2]);
		setPhoneNumber(st[3]);
		setRole(st[4]);
		setSalary(Double.parseDouble(st[5]));
		this.username = st[6];
		this.password = st[7];
	}
	
	// setter
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	// getter
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
}
