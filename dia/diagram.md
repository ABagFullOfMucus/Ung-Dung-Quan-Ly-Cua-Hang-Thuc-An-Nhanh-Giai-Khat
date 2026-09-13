Product implements IStorable {
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
	
	private void resize()
	public void addProduct(Product p)
	public Product findProduct(String id)
	public boolean removeProduct(String id)
	public void findMostExpensiveProduct()
	public void displayAllProducts()
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

CustomerList {
	private Customer[] customer
	private int count
	
	private void resize()
	public void addCustomer(Customer c)
	public Customer findCustomer(String id)
	public boolean removeCustomer(String id)
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
	public boolean removeEmployee(String id)
	public void findHighestPaidEmployee()
	public void searchByRole(String role)
	private void resize()
}

IStorable {
	String toFileString()
	void fromFileString(String line)
}

Order implements IStorable {
	private String orderID
	private Employee cashier
	private Customer customer
	private OrderItem[] items
	private int count
	
	public void addItem(OrderItem item)
	private void resize()
	public double calculateTotal()
	public boolean removeItem(String productID)
	public OrderItem findItem(String productID)
	public void displayOrder()
}

OrderItem implements IStorable {
	private Product product
	private int quantity
	
	public double getSubtotal()
	public void displayItem()
}

OrderList {
	private Order[] orders
	private int count
	
	public void calculateTotalRevenue()
	public void findHighestOrderValue()
	private void resize()
	public void addOrder(Order order)
	public Order findOrder(String orderID)
	public boolean removeOrder(String orderID)
	public void displayAllOrders()
}

Store implements IStorable {
	private String storeName
	private String address
	private String phoneNumber
}

Receipt {
	private Order order
	private Store store
	public void printReceipt()
}



























































































@ Back up - ABagFullOfMucus
