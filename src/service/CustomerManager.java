package service;

import model.Customer;

public class CustomerManager {
    private Customer[] customers;
    private int count;

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
        if (c == null || findCustomer(c.getId()) != null) {
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
        if (id == null) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            if (customers[i].getId().equalsIgnoreCase(id)) {
                return customers[i];
            }
        }
        return null;
    }

    public boolean removeCustomer(String id) {
        for (int i = 0; i < count; i++) {
            if (customers[i].getId().equalsIgnoreCase(id)) {
                for (int j = i; j < count - 1; j++) {
                    customers[j] = customers[j + 1];
                }
                customers[count - 1] = null;
                count--;
                return true;
            }
        }
        return false;
    }

    public void displayAllCustomers() {
        if (count == 0) {
            System.out.println("Danh sách khách hàng trống.");
            return;
        }

        System.out.println("Danh sách khách hàng:");
        for (int i = 0; i < count; i++) {
            Customer c = customers[i];
            System.out.println(c.getId() + " | " + c.getName() + " | "
                    + c.getPhoneNumber() + " | " + c.getTier()
                    + " | " + c.getLoyaltyPoints() + " điểm");
        }
    }

    public Customer[] getCustomers() {
        Customer[] result = new Customer[count];
        for (int i = 0; i < count; i++) {
            result[i] = customers[i];
        }
        return result;
    }

    public int getCount() {
        return count;
    }
}
