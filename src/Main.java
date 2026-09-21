import java.util.Scanner;

import model.Admin;
import model.Customer;
import model.Drink;
import model.Employee;
import model.FastFood;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import service.CustomerManager;
import service.EmployeeManager;
import service.MenuManager;
import service.OrderManager;
import utils.InputValidator;

public class Main {
	private static MenuManager menuManager = new MenuManager();
	private static CustomerManager customerManager = new CustomerManager();
	private static EmployeeManager employeeManager = new EmployeeManager();
	private static OrderManager orderManager = new OrderManager();
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("===== ĐĂNG NHẬP QUẢN TRỊ VIÊN =====");
		Admin admin = new Admin("AD1", "Quản trị viên", "N/A", "admin", "admin");
		
		boolean loggedIn = false;
		for (int attempt = 0; attempt < 3 && !loggedIn; attempt++) {
			String username = InputValidator.getNonEmptyString(sc, "Tên đăng nhập: ");
			String password = InputValidator.getNonEmptyString(sc, "Mật khẩu: ");
			
			if (admin.login(username, password)) {
				loggedIn = true;
			} else {
				System.out.println("Sai thông tin đăng nhập! Còn " + (2 - attempt) + " lần thử.");
			}
		}
		
		if (!loggedIn) {
			System.out.println("Đã hết lượt đăng nhập. Chương trình kết thúc!");
			sc.close();
			return;
		}
		
		int choice;
		do {
			System.out.println("==============================");
			System.out.println("\tỨNG DỤNG QUẢN LÝ");
			System.out.println("==============================");
			System.out.println("1. Quản lý sản phẩm");
			System.out.println("2. Quản lý khách hàng");
			System.out.println("3. Quản lý nhân viên");
			System.out.println("4. Quản lý order và hóa đơn");
			System.out.println("5. Lưu dữ liệu xuống file");
			System.out.println("6. Đọc dữ liệu từ file");
			System.out.println("0. Thoát");
			System.out.print("Nhập lựa chọn: ");
			
			choice = InputValidator.getInt(sc, "");
			
			switch (choice) {
				case 1:
					productMenu();
					break;
				case 2:
					customerMenu();
					break;
				case 3:
					employeeMenu();
					break;
				case 4:
					orderMenu();
					break;
				case 5:
					saveAllData();
					break;
				case 6:
					loadAllData();
					break;
				case 0:
					System.out.println("Đã thoát!");
					break;
				default:
					System.out.println("Lựa chọn không hợp lệ. Nhập lại!");
			}
		} while (choice != 0);
		
		sc.close();
	}
	
	private static void productMenu() {
		int choice;
		do {
			System.out.println("\n--- Quản lý sản phẩm ---");
			System.out.println("1. Xem danh sách sản phẩm");
			System.out.println("2. Thêm sản phẩm");
			System.out.println("3. Tìm sản phẩm theo ID");
			System.out.println("4. Xóa sản phẩm");
			System.out.println("5. Xem sản phẩm có giá cao nhất");
			System.out.println("6. Áp dụng giảm giá cho sản phẩm");
			System.out.println("7. Thoát");
			System.out.print("Nhập lựa chọn: ");
			choice = InputValidator.getInt(sc, "");
			
			switch (choice) {
				case 1:
					menuManager.displayAllItems();
					break;
				case 2:
					addProductFromInput();
					break;
				case 3: {
					String findId = InputValidator.getNonEmptyString(sc, "Nhập ID sản phẩm cần tìm: ");
					
					MenuItem p = menuManager.findItem(findId);
					
					if (p != null) {
						p.displayInfo();
					} else {
						System.out.println("Không tìm thấy sản phẩm!");
					}
					break;
				}
				case 4: {
					String removeId = InputValidator.getNonEmptyString(sc, "Nhập ID sản phẩm cần xóa: ");
					
					if (menuManager.removeItem(removeId)) {
						System.out.println(String.format("Đã thành công xóa sản phẩm có ID: %s!", removeId));
					} else {
						System.out.println("Không tìm thấy sản phẩm cần xóa!");
					}
					break;
				}
				case 5: {
					MenuItem expensiveProduct = menuManager.findMostExpensiveItem();
					
					if (expensiveProduct != null) {
						System.out.println("Sản phẩm có giá thành cao nhất: ");
						expensiveProduct.displayInfo();
					} else {
						System.out.println("Danh sách trống!");
					}
					break;
				}
				case 6: {
					String discountId = InputValidator.getNonEmptyString(sc, "Nhập ID sản phẩm cần giảm giá: ");
					MenuItem discountItem = menuManager.findItem(discountId);
					
					if (discountItem == null) {
						System.out.println("Không tìm thấy sản phẩm!");
						break;
					}
					
					double rate = InputValidator.getDouble(sc, "Nhập % giảm giá (0 - 100): ");
					discountItem.setDiscountRate(rate / 100.0);
					System.out.println("Đã áp dụng giảm giá! Giá sau giảm: " + discountItem.getDiscountedPrice() + "VND");
					break;
				}
				case 7:
					System.out.println("Đã thoát menu quản lý sản phẩm!");
					break;
				default:
					System.out.println("Lựa chọn không hợp lệ. Nhập lại!");
			}
		} while (choice != 7);
	}
	
	private static void addProductFromInput() {
		String id = InputValidator.getNonEmptyString(sc, "Nhập ID sản phẩm: ");
		
		if (menuManager.findItem(id) != null) {
			System.out.println("Sản phẩm với ID này đã tồn tại!");
			return;
		}
		
		String name = InputValidator.getNonEmptyString(sc, "Nhập tên sản phẩm: ");
		double price = InputValidator.getDouble(sc, "Nhập giá: ");
		int stock = InputValidator.getInt(sc, "Nhập số lượng hàng: ");
		
		System.out.println("Nhập loại sản phẩm (1. Thức ăn nhanh - FastFood | 2. Thức uống - Drink): ");
		int typeChoice = InputValidator.getInt(sc, "");
		
		if (typeChoice == 1) {
			double calories = InputValidator.getDouble(sc, "Nhập lượng calo: ");
			double shelfLifeHours = InputValidator.getDouble(sc, "Nhập hạn sử dụng (giờ): ");
			
			menuManager.addItem(new FastFood(id, name, price, stock, calories, shelfLifeHours));
			System.out.println("Đã thêm thức ăn nhanh thành công!");
		} else {
			String size = InputValidator.getNonEmptyString(sc, "Nhập size (S/M/L): ");
			boolean isIced = InputValidator.getBoolean(sc, "Có đá không? (true/false): ");
			
			menuManager.addItem(new Drink(id, name, price, stock, size, isIced));
			System.out.println("Đã thêm thức uống thành công!");
		}
	}
	
	private static void customerMenu() {
		int choice;
		do {
			System.out.println("\n--- Quản lý khách hàng ---");
			System.out.println("1. Xem danh sách khách hàng");
			System.out.println("2. Thêm khách hàng");
			System.out.println("3. Tìm khách hàng bằng ID");
			System.out.println("4. Xóa khách hàng");
			System.out.println("5. Cộng điểm thành viên");
			System.out.println("6. Thoát");
			System.out.print("Nhập lựa chọn: ");
			choice = InputValidator.getInt(sc, "");
			
			switch (choice) {
				case 1:
					customerManager.displayAllCustomers();
					break;
				case 2: {
					String id = InputValidator.getNonEmptyString(sc, "Nhập ID khách hàng: ");
					
					if (customerManager.findCustomer(id) != null) {
						System.out.println("Khách hàng với ID này đã tồn tại!");
						break;
					}
					
					String name = InputValidator.getNonEmptyString(sc, "Nhập tên khách hàng: ");
					String phone = InputValidator.getNonEmptyString(sc, "Nhập số điện thoại: ");
					String memberType = InputValidator.getNonEmptyString(sc, "Nhập loại thành viên (Thường/Bạc/Vàng): ");
					int loyaltyPoints = InputValidator.getInt(sc, "Nhập điểm thành viên: ");
					
					customerManager.addCustomer(new Customer(id, name, phone, memberType, loyaltyPoints));
					System.out.println("Đã thêm khách hàng thành công!");
					break;
				}
				case 3: {
					String findId = InputValidator.getNonEmptyString(sc, "Nhập ID khách hàng cần tìm: ");
					Customer c = customerManager.findCustomer(findId);
					
					if (c != null) {
						c.displayInfo();
					} else {
						System.out.println("Không tìm thấy khách hàng!");
					}
					break;
				}
				case 4: {
					String removeId = InputValidator.getNonEmptyString(sc, "Nhập ID khách hàng cần xóa: ");
					
					if (customerManager.removeCustomer(removeId)) {
						System.out.println(String.format("Đã thành công xóa khách hàng có ID: %s!", removeId));
					} else {
						System.out.println("Không tìm thấy khách hàng cần xóa!");
					}
					break;
				}
				case 5: {
					String pointId = InputValidator.getNonEmptyString(sc, "Nhập ID khách hàng cần cộng điểm: ");
					Customer pointCustomer = customerManager.findCustomer(pointId);
					
					if (pointCustomer == null) {
						System.out.println("Không tìm thấy khách hàng!");
						break;
					}
					
					int points = InputValidator.getInt(sc, "Nhập số điểm cần cộng: ");
					pointCustomer.addPoints(points);
					System.out.println("Đã cộng điểm! Tổng điểm hiện tại: " + pointCustomer.getLoyaltyPoints());
					break;
				}
				case 6:
					System.out.println("Đã thoát menu quản lý khách hàng!");
					break;
				default:
					System.out.println("Lựa chọn không hợp lệ. Nhập lại!");
			}
		} while (choice != 6);
	}
	
	private static void employeeMenu() {
		int choice;
		do {
			System.out.println("\n--- Quản lý nhân viên ---");
			System.out.println("1. Xem danh sách nhân viên");
			System.out.println("2. Thêm nhân viên");
			System.out.println("3. Xóa nhân viên");
			System.out.println("4. Tìm nhân viên theo chức vụ");
			System.out.println("5. Xem nhân viên có lương cao nhất");
			System.out.println("6. Thoát");
			System.out.print("Nhập lựa chọn: ");
			choice = InputValidator.getInt(sc, "");
			
			switch (choice) {
				case 1:
					employeeManager.displayAllEmployees();
					break;
				case 2: {
					String id = InputValidator.getNonEmptyString(sc, "Nhập ID nhân viên: ");
					
					if (employeeManager.findEmployee(id) != null) {
						System.out.println("Nhân viên với ID này đã tồn tại!");
						break;
					}
					
					String name = InputValidator.getNonEmptyString(sc, "Nhập tên nhân viên: ");
					String phone = InputValidator.getNonEmptyString(sc, "Nhập số điện thoại: ");
					String role = InputValidator.getNonEmptyString(sc, "Nhập chức vụ: ");
					double salary = InputValidator.getDouble(sc, "Nhập lương: ");
					
					employeeManager.addEmployee(new Employee(id, name, phone, role, salary));
					System.out.println("Đã thêm nhân viên thành công!");
					break;
				}
				case 3: {
					String removeId = InputValidator.getNonEmptyString(sc, "Nhập ID nhân viên cần xóa: ");
					
					if (employeeManager.removeEmployee(removeId)) {
						System.out.println(String.format("Đa thành công xóa nhân viên có ID: %s!", removeId));
					} else {
						System.out.println("Không tìm thấy nhân viên cần xóa!");
					}
					break;
				}
				case 4: {
					String role = InputValidator.getNonEmptyString(sc, "Nhập chức vụ cần tìm: ");
					employeeManager.searchByRole(role);
					break;
				}
				case 5:
					employeeManager.findHighestPaidEmployee();
					break;
				case 6:
					System.out.println("Đã thoát menu quản lý nhân viên!");
					break;
				default:
					System.out.println("Lựa chọn không hợp lệ. Nhập lại!");
			}
		} while (choice != 6);
	}
	
	private static void orderMenu() {
		int choice;
		do {
			System.out.println("\n--- Quản lý order và hóa đơn ---");
			System.out.println("1. Xem danh sách order");
			System.out.println("2. Tạo order mới");
			System.out.println("3. Tìm order theo mã");
			System.out.println("4. Xóa order");
			System.out.println("5. Thêm sản phẩm vào order");
			System.out.println("6. Xóa sản phẩm khỏi order");
			System.out.println("7. In hóa đơn của order");
			System.out.println("8. Xem tổng doanh thu");
			System.out.println("9. Xem order có giá trị cao nhất");
			System.out.println("0. Thoát");
			System.out.print("Nhập lựa chọn: ");
			choice = InputValidator.getInt(sc, "");
			
			switch (choice) {
				case 1:
					orderManager.displayAllOrders();
					break;
				case 2:
					createOrderFromInput();
					break;
				case 3: {
					Order found = orderManager.findOrder(InputValidator.getNonEmptyString(sc, "Nhập mã order cần tìm: "));
					
					if (found != null) {
						found.displayOrder();
					} else {
						System.out.println("Không tìm thấy order!");
					}
					break;
				}
				case 4: {
					String removeId = InputValidator.getNonEmptyString(sc, "Nhập mã order cần xóa: ");
					
					if (orderManager.removeOrder(removeId)) {
						System.out.println(String.format("Đã thành công xóa order có mã: %s!", removeId));
					} else {
						System.out.println("Không tìm thấy order cần xóa!");
					}
					break;
				}
				case 5:
					addItemToOrderFromInput();
					break;
				case 6: {
					Order o = orderManager.findOrder(InputValidator.getNonEmptyString(sc, "Nhập mã order: "));
					
					if (o == null) {
						System.out.println("Không tìm thấy order!");
						break;
					}
					
					String itemRemoveId = InputValidator.getNonEmptyString(sc, "Nhập ID sản phẩm cần xóa khỏi order: ");
					
					if (o.removeItem(itemRemoveId)) {
						System.out.println("Đã xóa sản phẩm khỏi order!");
					} else {
						OrderItem badItem = o.findItem(itemRemoveId);
						System.out.println("Không tìm thấy sản phẩm trong order!");
					}
					break;
				}
				case 7: {
					Order receiptOrder = orderManager.findOrder(InputValidator.getNonEmptyString(sc, "Nhập mã order cần in hóa đơn: "));
					orderManager.printReceipt(receiptOrder);
					break;
				}
				case 8:
					orderManager.calculateTotalRevenue();
					break;
				case 9:
					orderManager.findHighestOrderValue();
					break;
				case 0:
					System.out.println("Đã thoát menu quản lý order!");
					break;
				default:
					System.out.println("Lựa chọn không hợp lệ. Nhập lại!");
			}
		} while (choice != 0);
	}
	
	private static void createOrderFromInput() {
		String orderID = InputValidator.getNonEmptyString(sc, "Nhập mã order: ");
		
		if (orderManager.findOrder(orderID) != null) {
			System.out.println("Order với mã này đã tồn tại!");
			return;
		}
		
		String cashierId = InputValidator.getNonEmptyString(sc, "Nhập ID nhân viên thu ngân: ");
		Employee cashier = employeeManager.findEmployee(cashierId);
		
		if (cashier == null) {
			System.out.println("Không tìm thấy nhân viên thu ngân!");
			return;
		}
		
		System.out.print("Nhập ID khách hàng (bỏ trống nếu khách vãng lai): ");
		String customerInput = sc.nextLine().trim();
		Customer customer;
		
		if (customerInput.isEmpty()) {
			customer = new Customer("GUEST", "Khách vãng lai", "N/A", "Thường", 0);
		} else {
			customer = customerManager.findCustomer(customerInput);
			
			if (customer == null) {
				System.out.println("Không tìm thấy khách hàng, order sẽ dùng khách vãng lai!");
				customer = new Customer("GUEST", "Khách vãng lai", "N/A", "Thường", 0);
			}
		}
		
		Order order = new Order(orderID, cashier, customer);
		orderManager.addOrder(order);
		System.out.println("Đã tạo order thành công! Hãy dùng chức năng 5 để thêm sản phẩm.");
	}
	
	private static void addItemToOrderFromInput() {
		Order order = orderManager.findOrder(InputValidator.getNonEmptyString(sc, "Nhập mã order: "));
		
		if (order == null) {
			System.out.println("Không tìm thấy order!");
			return;
		}
		
		MenuItem product = menuManager.findItem(InputValidator.getNonEmptyString(sc, "Nhập ID sản phẩm: "));
		
		if (product == null) {
			System.out.println("Không tìm thấy sản phẩm!");
			return;
		}
		
		int quantity = InputValidator.getInt(sc, "Nhập số lượng: ");
		
		if (quantity <= 0) {
			System.out.println("Số lượng phải lớn hơn 0!");
			return;
		}
		
		OrderItem existing = order.findItem(product.getItemID());
		if (existing != null) {
			existing.setQuantity(existing.getQuantity() + quantity);
		} else {
			order.addItem(new OrderItem(product, quantity));
		}
		
		System.out.println("Đã thêm sản phẩm vào order!");
	}
	
	private static void saveAllData() {
		menuManager.saveToFile("data/menu.txt");
		customerManager.saveToFile("data/customers.txt");
		employeeManager.saveToFile("data/employees.txt");
		orderManager.saveToFile("data/orders.txt");
		System.out.println("Đã lưu dữ liệu vào thư mục data/!");
	}
	
	private static void loadAllData() {
		menuManager.loadFromFile("data/menu.txt");
		customerManager.loadFromFile("data/customers.txt");
		employeeManager.loadFromFile("data/employees.txt");
		orderManager.loadFromFile("data/orders.txt", menuManager, employeeManager, customerManager);
		
		System.out.println("Đã tải dữ liệu: " + menuManager.getCount() + " sản phẩm, "
			+ customerManager.getCount() + " khách hàng, "
			+ employeeManager.getCount() + " nhân viên, "
			+ orderManager.getCount() + " order.");
	}
}
