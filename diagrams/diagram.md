MenuItem implements IStorable, IDiscountable {
	private String itemID
	private String itemName
	private double price
	private int stock
	private double discountRate
	
	public abstract void displayInfo()
	public double getDiscountedPrice()
}

Drink extends MenuItem {
	private String size
	private boolean isIced
}

FastFood extends MenuItem {
	public double calories
	public double shelfLifeHours
}

MenuManager {
	private MenuItem[] items
	private int count
	
	public boolean addItem(MenuItem item)
	public MenuItem findItem(String id)
	public boolean removeItem(String id)
	public void displayAllItems()
	public MenuItem findMostExpensiveItem()
	public void saveToFile(String fileName)
	public void loadFromFile(String fileName)
	private void resize()
}

Person implements IStorable {
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

Admin extends Employee {
	private String username
	private String password
	
	public boolean login(String username, String password)
}

CustomerManager {
	private Customer[] customers
	private int count
	
	public boolean addCustomer(Customer c)
	public Customer findCustomer(String id)
	public boolean removeCustomer(String id)
	public void displayAllCustomers()
	public void saveToFile(String fileName)
	public void loadFromFile(String fileName)
	private void resize()
}

EmployeeManager {
	private Employee[] employees
	private int count
	
	public boolean addEmployee(Employee e)
	public Employee findEmployee(String id)
	public boolean removeEmployee(String id)
	public void displayAllEmployees()
	public void findHighestPaidEmployee()
	public void searchByRole(String role)
	public void saveToFile(String fileName)
	public void loadFromFile(String fileName)
	private void resize()
}

IStorable {
	String toFileString()
	void fromFileString(String line)
}

IDiscountable {
	double getDiscountRate()
	void setDiscountRate(double discountRate)
	double getDiscountedPrice()
}

IPayable {
	double calculateTotal()
}

Order implements IStorable, IPayable {
	private String orderID
	private Employee cashier
	private Customer customer
	private OrderItem[] items
	private int count
	
	public void addItem(OrderItem item)
	public double calculateTotal()
	public boolean removeItem(String itemID)
	public OrderItem findItem(String itemID)
	public void displayOrder()
}

OrderItem implements IStorable {
	private MenuItem item
	private int quantity
	
	public double getSubtotal()
	public void displayItem()
}

OrderManager {
	private Order[] orders
	private int count
	private String storeName
	private String storeAddress
	private String storePhoneNumber
	
	public boolean addOrder(Order order)
	public Order findOrder(String orderID)
	public boolean removeOrder(String orderID)
	public void displayAllOrders()
	public void calculateTotalRevenue()
	public void findHighestOrderValue()
	public void printReceipt(Order order)
	public void saveToFile(String fileName)
	public void loadFromFile(String fileName, ...)
	private void resize()
}

FileHandler {
	public static boolean saveToFile(String fileName, IStorable[] items, int count)
	public static int countLines(String fileName)
	public static String[] readLines(String fileName)
}

InputValidator {
	public static int getInt(Scanner sc, String prompt)
	public static double getDouble(Scanner sc, String prompt)
	public static String getNonEmptyString(Scanner sc, String prompt)
	public static boolean getBoolean(Scanner sc, String prompt)
}



@ Back up - ABagFullOfMucus
