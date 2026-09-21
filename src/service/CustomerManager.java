package service;

import model.Customer;
import utils.FileHandler;

public class CustomerManager {
	private Customer[] customers;
	
	private int count;
	
	// constructor
	public CustomerManager() {
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
	
	public boolean addCustomer(Customer c) {
		if (findCustomer(c.getID()) != null) {
			return false;
		}
		
		if (count >= customers.length) resize();
		
		customers[count] = c;
		count += 1;
		return true;
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
	
	public void displayAllCustomers() {
		if (count == 0) {
			System.out.println("Danh sách trống!");
			return;
		}
		
		System.out.println("Danh sách khách hàng: ");
		for (int i = 0; i < count; i++) {
			customers[i].displayInfo();
		}
	}
	
	// Lưu / đọc file
	public void saveToFile(String fileName) {
		FileHandler.saveToFile(fileName, customers, count);
	}
	
	public void loadFromFile(String fileName) {
		String[] lines = FileHandler.readLines(fileName);
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			
			if (!line.startsWith("CUSTOMER")) continue;
			
			Customer c = new Customer();
			c.fromFileString(line);
			addCustomer(c);
		}
	}
	
	// getters 
	public Customer[] getCustomers() {
		return this.customers;
	}
	
	public int getCount() {
		return this.count;
	}
}
