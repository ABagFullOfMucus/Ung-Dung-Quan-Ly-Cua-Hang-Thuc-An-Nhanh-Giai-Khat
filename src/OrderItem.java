public class OrderItem {
	private Product product;
	
	private int quantity;
	
	
	// constructor
	public OrderItem(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	

	public double getSubtotal() {
		return this.product.getPrice() * this.quantity;
	}
	
	
	// setter
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	// getter 
	public Product getProduct() {
		return this.product;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
}
