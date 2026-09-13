public class Order implements IStorable {
	private String orderID;
	
	private Employee cashier;
	
	private Customer customer;
	
	private OrderItem[] items;
	
	private int count;
	
	
	// constructor
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
	
	public double calculateTotal() {
		double tong = 0;
		
		for (int i = 0; i < count; i++) {
			tong += items[i].getSubtotal();
		}
		
		return tong;
	}
	
	public boolean removeItem(String productID) {
		int f = -1;
		for (int i = 0; i < count; i++) {
			if (items[i].getProduct().getProductID().equals(productID)) {
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
	
	public OrderItem findItem(String productID) {
		for (int i = 0; i < count; i++) {
			if (items[i].getProduct().getProductID().equals(productID)) {
				return items[i];
			}
		}
		return null;
	}
	
	public void displayOrder() {
		if (count == 0) {
			System.out.println("Danh sách trống!");
		}
		
		System.out.println("===== Danh sách Order =====");
		for (int i = 0; i < count; i++) {
			items[1].displayItem();
		}
	}
	
	@Override
	public String toFileString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ORDER,").append(orderID).append(",").append(cashier != null ? cashier.getID() : "NULL").append(",").append(customer != null ? customer.getID() : "NULL");
		
		for (int i = 0; i < count; i++) {
			sb.append(",").append(items[i].getProduct().getProductID()).append(":").append(items[i].getQuantity());
		}
		
		return sb.toString();
	}
	
	@Override
	public void fromFileString(String line) {
		String[] st = line.split(",");
		this.orderID = st[0];
		this.items = new OrderItem[1];
		this.count = 0;
		
		for (int i = 3; i < st.length; i++) {
			String[] itemParts = st[i].split(":");
			OrderItem item = new OrderItem(null, Integer.parseInt(itemParts[1]));
			addItem(item);
		}
	}
	
	public void fromFileString(String line, ProductList productList, EmployeeList employeeList, CustomerList customerList) {
		String[] st = line.split(",");
		
		this.orderID = st[0];
		this.cashier = employeeList.findEmployee(st[1]);
		this.customer = customerList.findCustomer(st[2]);
		
		this.items = new OrderItem[1];
		this.count = 0;
		
		for (int i = 3; i < st.length; i++) {
			String[] orderItemParts = st[i].split(":");
			String productID = orderItemParts[0]; 
			int quantity = Integer.parseInt(orderItemParts[1]);
			
			Product product = productList.findProduct(productID);
			
			OrderItem newItem = new OrderItem(product, quantity);
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
