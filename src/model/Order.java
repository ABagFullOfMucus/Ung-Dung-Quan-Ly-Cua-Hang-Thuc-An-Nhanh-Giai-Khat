package model;

import interfaces.IPayable;
import interfaces.IStorable;
import service.CustomerManager;
import service.EmployeeManager;
import service.MenuManager;

public class Order implements IStorable, IPayable {
	private String orderID;
	
	private Employee cashier;
	
	private Customer customer;
	
	private OrderItem[] items;
	
	private int count;
	
	
	// constructor
	public Order() {
		this.items = new OrderItem[1];
		this.count = 0;
	}
	
	public Order(String orderID, Employee cashier, Customer customer) {
		this.orderID = orderID;
		this.cashier = cashier;
		this.customer = customer;
		this.items = new OrderItem[1];
		this.count = 0;
	}
	
	private void resize() {
		int l = items.length * 2;
		OrderItem[] tmp = new OrderItem[l];
		
		for (int i = 0; i < count; i++) {
			tmp[i] = items[i];
		}
		
		items = tmp;
	}
	
	public void addItem(OrderItem item) {
		if (count >= items.length) {
			resize();
		}
		
		items[count] = item;
		count += 1;
	}
	
	@Override
	public double calculateTotal() {
		double tong = 0;
		
		for (int i = 0; i < count; i++) {
			tong += items[i].getSubtotal();
		}
		
		return tong;
	}
	
	public boolean removeItem(String itemID) {
		int f = -1;
		for (int i = 0; i < count; i++) {
			if (items[i].getItem().getItemID().equals(itemID)) {
				f = i;
				break;
			}
		}
		
		if (f == -1) return false;
		
		for (int i = f; i < count - 1; i++) {
			items[i] = items[i + 1];
		}
		
		items[count - 1] = null;
		count -= 1;
		return true;
	}
	
	public OrderItem findItem(String itemID) {
		for (int i = 0; i < count; i++) {
			if (items[i].getItem().getItemID().equals(itemID)) {
				return items[i];
			}
		}
		return null;
	}
	
	public void displayOrder() {
		if (count == 0) {
			System.out.println("Danh sách trống!");
			return;
		}
		
		System.out.println("===== Order " + orderID + " =====");
		System.out.println("Thu ngân: " + (cashier != null ? cashier.getName() : "N/A")
			+ " | Khách hàng: " + (customer != null ? customer.getName() : "Khách vãng lai"));
		
		for (int i = 0; i < count; i++) {
			items[i].displayItem();
		}
		
		System.out.println("Tổng tiền: " + calculateTotal() + "VND");
	}
	
	@Override
	public String toFileString() {
		// Định dạng: ORDER,<orderID>,<cashierID|NULL>,<customerID|NULL>,<itemID:quantity>,...
		StringBuilder sb = new StringBuilder("ORDER,");
		sb.append(orderID).append(",").append(cashier != null ? cashier.getID() : "NULL").append(",").append(customer != null ? customer.getID() : "NULL");
		
		for (int i = 0; i < count; i++) {
			sb.append(",").append(items[i].toFileString());
		}
		
		return sb.toString();
	}
	
	@Override
	public void fromFileString(String line) {
		// Đọc thô: chỉ nạp mã order và số lượng, chưa ánh xạ tham chiếu
		String[] st = line.split(",");
		this.orderID = st[1];
		this.cashier = null;
		this.customer = null;
		this.items = new OrderItem[1];
		this.count = 0;
		
		for (int i = 4; i < st.length; i++) {
			OrderItem item = new OrderItem(null, 0);
			item.fromFileString(st[i]);
			addItem(item);
		}
	}
	
	// Đọc file kèm danh sách để ánh xạ lại tham chiếu
	public void fromFileString(String line, MenuManager menuManager, EmployeeManager employeeManager, CustomerManager customerManager) {
		String[] st = line.split(",");
		
		this.orderID = st[1];
		this.cashier = "NULL".equals(st[2]) ? null : employeeManager.findEmployee(st[2]);
		this.customer = "NULL".equals(st[3]) ? null : customerManager.findCustomer(st[3]);
		
		this.items = new OrderItem[1];
		this.count = 0;
		
		for (int i = 4; i < st.length; i++) {
			String[] itemParts = st[i].split(":");
			String itemID = itemParts[0];
			int quantity = Integer.parseInt(itemParts[1]);
			
			MenuItem item = menuManager.findItem(itemID);
			
			OrderItem newItem = new OrderItem(item, quantity);
			addItem(newItem); 
		}
	}
	
	// setter
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	
	public void setCashier(Employee cashier) {
		this.cashier = cashier;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	// getter
	public String getOrderID() {
		return this.orderID;
	}
	
	public Employee getCashier() {
		return this.cashier;
	}
	
	public Customer getCustomer() {
		return this.customer;
	}
	
	public OrderItem[] getItems() {
		return this.items;
	}
	
	public int getCount() {
		return this.count;
	}
}
