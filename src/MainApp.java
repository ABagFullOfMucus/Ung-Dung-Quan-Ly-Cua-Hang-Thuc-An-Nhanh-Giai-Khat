import java.util.Scanner;

public class MainApp {
	private static ProductList productList = new ProductList();
	private static CustomerList customerList = new CustomerList();
	private static OrderList orderList = new OrderList();
	private static Store store = new Store("Của hàng ABC", "929 Trần Hưng Đạo, phường Chợ Quán, Thành phố Hồ Chí Minh, Việt Nam", "02839235821");
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		int choice;
		do {
			System.out.println("==============================");
			System.out.println("	ỨNG DỤNG QUẢN LÝ");
			System.out.println("==============================");
			System.out.println("1. Quản lý sản phẩm");
            System.out.println("2. Quản lý khách hàng");
            System.out.println("3. Quản lý order và hóa đơn");
            System.out.println("0. Thoát");
            System.out.print("Nhập lựa chọn: ");
            
            while (!sc.hasNextInt()) {
				System.out.println("Vui lòng nhập một lựa chọn hợp lệ!");
				sc.next();
			}
			
			choice = sc.nextInt();
			sc.nextLine();
			
			switch(choice) {
				case 1:
					productMenu();
					break;
				case 2:
					// pass
					break;
				case 3:
					// pass
					break;
				case 0:
					System.out.println("Đã thoát!");
					break;
				default:
					System.out.println("Lựa chọn khôn hợp lệ. Nhập lại!");
			}
		} while (choice != 0);
	}
	
	private static void productMenu() {
		System.out.println("\n--- Quản lý sản phẩm ---");
		System.out.println("1. Xem danh sách sản phẩm");
		System.out.println("2. Thêm sản phẩm");
		System.out.println("3. Tìm sản phẩm theo ID");
		System.out.println("4. Xóa sản phẩm");
		System.out.println("5. Xem sản phẩm có giá cao nhất");
		System.out.println("6. Thoát");
		System.out.print("Nhập lựa chọn: ");
		int choice = sc.nextInt();
		sc.nextLine();
		
		switch (choice) {
			case 1:
				productList.displayAllProducts();
				break;
			case 2:
				System.out.print("Nhập ID sản phẩm: ");
				String id = sc.nextLine();
				System.out.print("Nhập tên sản phẩm: ");
				String name = sc.nextLine();
				System.out.print("Nhập giá: ");
				double price = sc.nextDouble();
				sc.nextLine();
				System.out.print("Nhập số lượng hàng: ");
				int stock = sc.nextInt();
				sc.nextLine();
				
				System.out.print("Nhập loại sản phẩm (1. Thức ăn nhanh - FastFood | 2. Thức uống - Beverage: ");
				int typeChoice = sc.nextInt();
				sc.nextLine();
				
				if (typeChoice == 1) {
					System.out.print("Nhập lương calo: ");
					double calories = sc.nextDouble();
					sc.nextLine();
					System.out.println("Nhập hạn sử dụng (giờ): ");
					double shelfLifeHours = sc.nextDouble();
					sc.nextLine();
					
					productList.addProduct(new FastFood(id, name, price, stock, calories, shelfLifeHours));
				} else {
					productList.addProduct(new Beverage(id, name, price, stock));
				}
				
				break;
			case 3:
				System.out.print("Nhập ID sản phẩm cần tìm: ");
                String findId = sc.nextLine();
                
                Product p = productList.findProduct(findId);
                
				if (p != null) {
					p.displayInfo();
				} else {
					System.out.println("Không tìm thấy sản phẩm!");
				}
				
				break;
			case 4:
				System.out.print("Nhập ID sản phẩm cần xóa: ");
                String removeId = sc.nextLine();
                if (productList.removeProduct(removeId)) {
					System.out.println(String.format("Đã thành công xóa sản phẩm có ID: %s!", removeId));
				} else {
					System.out.println("Không tìm thấy sản phẩm cần xóa!");
				}
				
				break;
			case 5:
				Product expensiveProduct = productList.findMostExpensiveProduct();
				
				if (expensiveProduct != null) {
					System.out.println("Sản phẩm có giá thành cao nhất: ");
					expensiveProduct.displayInfo();
				}
				
				break; 
			case 6:
				System.out.println("Đã thoát menu quản lý sản phẩm!");
				return;
			default:
				System.out.println("Lựa chọn không hợp lệ. Nhập lại!");
		}	
	}
	
	public static void customerMenu() {
		System.out.println("\n--- Quản lý khách hàng ---");
		System.out.println("1. Xem danh sách khách hàng");
		System.out.println("2. Thêm khách hàng");
		System.out.println("3. Tìm khách hàng bằng ID");
		System.out.println("4. Xóa khách hàng");
		System.out.println("5. Thoát");
		System.out.print("Nhập lựa chọn: ");
		
		int choice = sc.nextInt();
		sc.nextLine();
		
		switch (choice) {
			case 1:
				customerList.displayAllCustomers();
				break;
			case 2:
				// pass
				break;
			case 3:
				System.out.print("Nhập ID khách hàng cần tìm: ");
				String findId = sc.nextLine();
				
				Customer c = customerList.findCustomer(findId);
				
				if (c != null) {
					c.displayInfo();
				} else {
					
				}
				
				break;
		}
	}
}
