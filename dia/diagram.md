Product {
	private String productID
	private String productName
	private double price
	private int stock
	
	public abstract void displayInfo()
}

Beverage extends Product {
	private String size
	private boolean isIced
}

FastFood extends Product {
	private double calories
	private double shelfLifeHours
}

ProductList {
	private Product[] products
	private int count
	
	public void addProduct(Product p)
	public Product findProduct(String id)
	public boolean removeProduct(String id)
}

Person {
	protected String id
	protected String name
	protected String phoneNumber
	
	public abstract void displayInfo()
}

Customer extends Person {
	private String memberType
	private int loyaltyPoints
	
	public void addPoints(int points)
}

Employee extends Person {
	private String role
	private double salary
}

EmployeeList {
	private Employee[] employees
	private int count
	
	public void addEmployee(Employee e)
	public Employee findEmployee(String id)
	public void removeEmployee(String id)
}

IStorable {
	String toFileString()
	void fromFileString(String line)
}

Order {
	private String orderID
	private Employee cashier
	private Customer customer
	private OrderItem[] items
	private int count
}

OrderItem {
	private Product product
	private int quantity
	
	public double getSubtotal()
}

OrderList {
	private Order[] orders
	private int count
}

Store {
	private String storeName
	private String address
	private String phoneNumber
}

Receipt {
	private Order order
	private Store store
	public void printReceipt()
}


