public class Receipt {
	private Order order;
	
	private Store store;
	
	// constructor
	
	public Receipt(Order order, Store store) {
		this.order= order;
		this.store = store;
	}
	
	public void printReceipt() {
        System.out.println("==========================================");
        System.out.println("       " + (store != null ? store.getStoreName() : "Cửa hàng ABC"));
        System.out.println("Địa chỉ: " + (store != null ? store.getAddress() : "929 Trần Hưng Đạo, Chợ Quán, Hồ Chí Minh, Vietnam"));
        System.out.println("==========================================");
        System.out.println("Mã order: " + (order != null ? order.getOrderID() : "1234567890"));
        
        Employee cashier = order != null ? order.getCashier() : null;
        System.out.println("Thu ngân:  " + (cashier != null ? cashier.getName() : "Nguyễn Văn A"));
        
        Customer customer = order != null ? order.getCustomer() : null;
        System.out.println("Khách hàng: " + (customer != null ? customer.getName() : "Lê Văn B"));
        System.out.println("------------------------------------------");
        System.out.println(String.format("%-20s %-5s %-10s", "Sản phẩm", "Số lượng", "Tổng tiền"));
        System.out.println("------------------------------------------");
        
        if (order != null && order.getItems() != null) {
            OrderItem[] items = order.getItems();
            int count = order.getCount();
            for (int i = 0; i < count; i++) {
                OrderItem item = items[i];
                if (item != null && item.getProduct() != null) {
                    System.out.println(String.format("%-20s %-5d %-10.2f", 
                        item.getProduct().getProductName(), 
                        item.getQuantity(), 
                        item.getSubtotal()));
                }
            }
        }
	}
	
	
	
	// setters
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public void setStore(Store store) {
		this.store = store;
	}
	
	// getters
	public Order getOrder() {
		return this.order;
	}
	
	public Store getStore() {
		return this.store;
	}
	
}
