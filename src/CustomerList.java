public class CustomerList {
	private Customer[] customers;
	
	private int count;
	
	// constructor
	public CustomerList() {
		this.customers = new Customer[1];
		this.count = 0;
	}
	
	private void resize() {
		int l = customers.length * 2;
		Customer[] tmp = new Customer[l];
		
		for (int i = 0; i < count; i++) {
			tmp[i] = customers[i];
		}
		
		customers = tmp;
	}
	
	public void addCustomer(Customer c) {
		if (count >= customers.length) resize();
		
		customers[count] = c;
		count += 1;
	}
	
	public Customer findCustomer(String id) {
		for (int i = 0; i < count; i++) {
			if (customers[i].getID().equals(id)) {
				return customers[i];
			}
		}
		return null;
	}

	public boolean removeCustomer(String id) {
		int f = -1;
		for (int i = 0; i < count; i++) {
			if (customers[i].getID().equals(id)) {
				f = i;
				break;
			}
		}
		
		if (f == -1) {
			return false;
		}
		
		for (int i = f; i < count - 1; i++) {
			customers[i] = customers[i + 1];
		}
		
		customers[count - 1] = null;
		count--;
		return true;
	}
	
	// setters
	public void setCustomers(Customer[] customers) {
		this.customers = customers;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	// getters 
	public Customer[] getCustomers() {
		return this.customers;
	}
	
	public int getCount() {
		return this.count;
	}
}
