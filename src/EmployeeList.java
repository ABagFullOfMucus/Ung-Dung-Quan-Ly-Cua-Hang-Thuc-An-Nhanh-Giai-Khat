public class EmployeeList {
	private Employee[] employees;
	
	private int count;
	
	// constructor
	public EmployeeList() {
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
	
	public void addEmployee(Employee e) {
		if (count >= employees.length) {
			resize(); 
		}
		
		employees[count] = e;
		count += 1;
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
	
	public void findHighestPaidEmployee() {
		if (count == 0) {
			System.out.println("Danh sách trống!");
			return;
		}
		
		double luongFlag = employees[0].getSalary();
		for (int i = 0; i < count; i++) {
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
			if (employees[i].getRole() == role) {
				employees[i].displayInfo();
			}
		}
	}
}

























