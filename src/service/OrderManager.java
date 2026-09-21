package service;

import model.Customer;
import model.Employee;
import model.Order;
import model.OrderItem;
import utils.FileHandler;

public class OrderManager {
	private Order[] orders;
	
	private int count;
	
	// Thông tin cửa hàng (gộp từ lớp Store cũ)
	private String storeName;
	
	private String storeAddress;
	
	private String storePhoneNumber;
	
	// constructor
	public OrderManager() {
		this.orders = new Order[1];
		this.count = 0;
		this.storeName = "Cửa hàng ABC";
		this.storeAddress = "929 Trần Hưng Đạo, phường Chợ Quán, Thành phố Hồ Chí Minh, Việt Nam";
		this.storePhoneNumber = "02839235821";
	}
	
	private void resize() {
		int l = orders.length * 2;
		Order[] tmp = new Order[l];
		
		for (int i = 0; i < count; i++) {
			tmp[i] = orders[i];
		}
		
		orders = tmp;
	}
	
	public boolean addOrder(Order order) {
		if (findOrder(order.getOrderID()) != null) {
			return false;
		}
		
		if (count >= orders.length) {
			resize();
		}
		
		orders[count] = order;
		count += 1;
		return true;
	}
	
	public Order findOrder(String orderID) {
        for (int i = 0; i < count; i++) {
            if (orders[i].getOrderID().equals(orderID)) {
                return orders[i];
            }
        }
        return null;
    }
	
	public boolean removeOrder(String orderID) {
		int f = -1;
		
		for (int i = 0; i < count; i++) {
			if (orders[i].getOrderID().equals(orderID)) {
				f = i;
				break;
			}
		}
		
		if (f == -1) return false;
		
		for (int i = f; i < count - 1; i++) {
			orders[i] = orders[i + 1];
		}
		
		orders[count - 1] = null;
		count -= 1;
		return true; 
	}
	
	public void displayAllOrders() {
        if (count == 0) {
            System.out.println("Không có order nào!");
            return;
        }
        
        for (int i = 0; i < count; i++) {
            orders[i].displayOrder();
        }
    }
    
    public void calculateTotalRevenue() {
		if (count == 0) {
            System.out.println("Không có order nào!");
            return;
        }
        
        double tong = 0;
        for (int i = 0; i < count; i++) {
			tong += orders[i].calculateTotal();
		}
		
		System.out.println("Tổng giá trị có danh sách order là: " + tong);
	}
	
	public void findHighestOrderValue() {
		if (count == 0) {
            System.out.println("Không có order nào!");
            return;
        }
        
        double flag = orders[0].calculateTotal();
        for (int i = 1; i < count; i++) {
			if (flag < orders[i].calculateTotal()) {
				flag = orders[i].calculateTotal();
			}
		}
		
		System.out.println("Danh sách những order có giá trị lớn nhất là: ");
		for (int i = 0; i < count; i++) {
			if (flag == orders[i].calculateTotal()) {
				orders[i].displayOrder();
			}
		}
	}
	
	// In hóa đơn 
	public void printReceipt(Order order) {
		if (order == null) {
			System.out.println("Không tìm thấy order!");
			return;
		}
		
		System.out.println("==========================================");
		System.out.println("       " + storeName);
		System.out.println("Địa chỉ: " + storeAddress);
		System.out.println("==========================================");
		System.out.println("Mã order: " + order.getOrderID());
		
		Employee cashier = order.getCashier();
		System.out.println("Thu ngân:  " + (cashier != null ? cashier.getName() : "N/A"));
		
		Customer customer = order.getCustomer();
		System.out.println("Khách hàng: " + (customer != null ? customer.getName() : "Khách vãng lai"));
		System.out.println("------------------------------------------");
		System.out.println(String.format("%-20s %-8s %-12s", "Sản phẩm", "S.Lượng", "Thành tiền"));
		System.out.println("------------------------------------------");
		
		if (order.getItems() != null) {
			OrderItem[] items = order.getItems();
			int itemCount = order.getCount();
			for (int i = 0; i < itemCount; i++) {
				OrderItem item = items[i];
				if (item != null && item.getItem() != null) {
					System.out.println(String.format("%-20s %-8d %-12.2f", 
						item.getItem().getItemName(), 
						item.getQuantity(), 
						item.getSubtotal()));
				}
			}
		}
		
		System.out.println("------------------------------------------");
		System.out.println(String.format("%-32s %-12.2f %s", "TỔNG TIỀN", order.calculateTotal(), "VNĐ"));
		System.out.println("Cảm ơn quý khách. Hẹn gặp lại!");
	}
	
	// Lưu / đọc file
	public void saveToFile(String fileName) {
		FileHandler.saveToFile(fileName, orders, count);
	}
	
	public void loadFromFile(String fileName, MenuManager menuManager, EmployeeManager employeeManager, CustomerManager customerManager) {
		String[] lines = FileHandler.readLines(fileName);
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			
			if (!line.startsWith("ORDER")) continue;
			
			Order order = new Order();
			order.fromFileString(line, menuManager, employeeManager, customerManager);
			addOrder(order);
		}
	}
	
	// setter thông tin cửa hàng
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	
	public void setStorePhoneNumber(String storePhoneNumber) {
		this.storePhoneNumber = storePhoneNumber;
	}
	
	// getters
	public int getCount() {
		return this.count;
	}
	
	public Order[] getOrders() {
		return this.orders;
	}
	
	public String getStoreName() {
		return this.storeName;
	}
	
	public String getStoreAddress() {
		return this.storeAddress;
	}
	
	public String getStorePhoneNumber() {
		return this.storePhoneNumber;
	}
}
