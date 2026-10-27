package service;

import java.util.Arrays;
import model.Customer;
import utils.FileHandler;

public class CustomerManager {
    private Customer[] customers;
    private int count;

    // Constructor
    public CustomerManager() {
        customers = new Customer[10];
        count = 0;
    }

    private void resize() {
        Customer[] newCustomers = new Customer[customers.length * 2];
        for (int i = 0; i < count; i++) {
            newCustomers[i] = customers[i];
        }
        customers = newCustomers;
    }

    public boolean addCustomer(Customer c) {
        if (c == null || findCustomer(c.getID()) != null) {
            return false;
        }

        if (count >= customers.length) {
            resize();
        }

        customers[count] = c;
        count++;

        return true;
    }

    public Customer findCustomer(String id) {
        for (int i = 0; i < count; i++) {
            if (customers[i].getID().equalsIgnoreCase(id)) {
                return customers[i];
            }
        }
        return null;
    }

    public boolean removeCustomer(String id) {
        Customer c = findCustomer(id);

        if (c == null || !c.isStatus()) {
            return false;
        }

        c.setStatus(false);
        return true;
    }

    // Hiển thị tất cả khách hàng
	public void displayAllCustomers() {
		if (count == 0) {
			System.out.println("Danh sách trống!");
			return;
		}
		
        boolean hasActive = false;
		System.out.println("Danh sách khách hàng: ");
		for (int i = 0; i < count; i++) {
			customers[i].displayInfo();
            hasActive = false;
		}
        if (!hasActive) {
            System.out.println("Không có khách hàng nào đang hoạt động.");
        }
	}

    public void saveToFile(String fileName) {
        FileHandler.saveToFile(fileName, customers, count);
    }

    public void loadFromFile(String fileName) {
        String[] lines = FileHandler.readLines(fileName);

        if (lines == null) {
            return;
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null && !lines[i].trim().isEmpty()) {
                Customer c = new Customer();
                c.fromFileString(lines[i]);
                addCustomer(c);
            }
        }
    }

    public Customer[] getCustomers() {
        return Arrays.copyOf(customers, count);
    }

    public int getCount() {
        return count;
    }
}
