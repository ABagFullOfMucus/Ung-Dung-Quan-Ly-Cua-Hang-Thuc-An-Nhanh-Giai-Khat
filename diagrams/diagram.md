MenuItem implements IStorable, IDiscountable {
	- String itemID
	- String itemName
	- double price
	- int stock
	- double discountRate
	
	+ abstract void displayInfo()
	+ double getDiscountedPrice()
}

Drink extends MenuItem {
	- String size
	- boolean isIced
}

FastFood extends MenuItem {
	- double calories
	- double shelfLifeHours
}

MenuManager {
	- MenuItem[] items
	- int count
	
	+ boolean addItem(MenuItem item)
	+ MenuItem findItem(String id)
	+ boolean removeItem(String id)
	+ void displayAllItems()
	+ MenuItem findMostExpensiveItem()
	+ void saveToFile(String fileName)
	+ void loadFromFile(String fileName)
	- void resize()
}

Person implements IStorable {
	# String id
	# String name
	# String phoneNumber
	
	+ abstract void displayInfo()
}

Customer extends Person {
	- String memberType
	- int loyaltyPoints
	
	+ void addPoints(int points)
}

Employee extends Person {
	- String role
	- double salary
}

Admin extends Employee {
	- String username
	- String password
	
	+ boolean login(String username, String password)
}

CustomerManager {
	- Customer[] customers
	- int count
	
	+ boolean addCustomer(Customer c)
	+ Customer findCustomer(String id)
	+ boolean removeCustomer(String id)
	+ void displayAllCustomers()
	+ void saveToFile(String fileName)
	+ void loadFromFile(String fileName)
	- void resize()
}

EmployeeManager {
	- Employee[] employees
	- int count
	
	+ boolean addEmployee(Employee e)
	+ Employee findEmployee(String id)
	+ boolean removeEmployee(String id)
	+ void displayAllEmployees()
	+ void findHighestPaidEmployee()
	+ void searchByRole(String role)
	+ void saveToFile(String fileName)
	+ void loadFromFile(String fileName)
	- void resize()
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
	- String orderID
	- Employee cashier
	- Customer customer
	- OrderItem[] items
	- int count
	
	+ void addItem(OrderItem item)
	+ double calculateTotal()
	+ boolean removeItem(String itemID)
	+ OrderItem findItem(String itemID)
	+ void displayOrder()
}

OrderItem implements IStorable {
	- MenuItem item
	- int quantity
	
	+ double getSubtotal()
	+ void displayItem()
}

OrderManager {
	- Order[] orders
	- int count
	- String storeName
	- String storeAddress
	- String storePhoneNumber
	
	+ boolean addOrder(Order order)
	+ Order findOrder(String orderID)
	+ boolean removeOrder(String orderID)
	+ void displayAllOrders()
	+ void calculateTotalRevenue()
	+ void findHighestOrderValue()
	+ void printReceipt(Order order)
	+ void saveToFile(String fileName)
	+ void loadFromFile(String fileName, ...)
	- void resize()
}

FileHandler {
	+ static boolean saveToFile(String fileName, IStorable[] items, int count)
	+ static int countLines(String fileName)
	+ static String[] readLines(String fileName)
}

InputValidator {
	+ static int getInt(Scanner sc, String prompt)
	+ static double getDouble(Scanner sc, String prompt)
	+ static String getNonEmptyString(Scanner sc, String prompt)
	+ static boolean getBoolean(Scanner sc, String prompt)
}



@ Back up - ABagFullOfMucus
