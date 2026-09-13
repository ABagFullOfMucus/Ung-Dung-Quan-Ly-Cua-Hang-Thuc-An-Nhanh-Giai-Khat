public class OrderList {
	private Order[] orders;
	
	private int count;
	
	// constructor
	public OrderList() {
		this.orders = new Order[1];
		this.count = 0;
	}
	
	private void resize() {
		int l = orders.length * 2;
		Order[] tmp = new Order[l];
		
		for (int i = 0; i < count; i++) {
			tmp[i] = orders[i];
		}
		
		orders = tmp;
	}
	
	public void addOrder(Order order) {
		if (count >= orders.length) {
			resize();
		}
		
		orders[count] = order;
		count += 1;
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
		
		for (int i = f; i < count; i++) {
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
        for (int i = 0; i < count; i++) {
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
	
	// getters
	public int getCount() {
		return this.count;
	}
	
	public Order[] getOrder() {
		return this.orders;
	}
}
