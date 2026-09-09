public class Receipt {
	private Order order;
	
	private Store store;
	
	// constructor
	
	public Receipt(Order order, Store store) {
		this.order= order;
		this.store = store;
	}
	
	public void printReceipt() {
		// pass
	}
}
