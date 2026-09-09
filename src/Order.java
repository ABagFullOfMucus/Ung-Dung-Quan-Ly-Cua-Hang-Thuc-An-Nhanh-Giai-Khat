public class Order {
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
	
	// methods
	
	
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
}
