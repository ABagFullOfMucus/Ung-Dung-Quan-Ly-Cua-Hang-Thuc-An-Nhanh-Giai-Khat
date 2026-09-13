public class OrderItem implements IStorable {
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
	
	public void displayItem() {
        System.out.println(product.getProductName() + " x " + quantity + " = VNĐ" + getSubtotal());
    }
	
	@Override
    public String toFileString() {
        return product.getProductID() + ":" + quantity;
    }
    
    @Override
    public void fromFileString(String line) {
		String[] st = line.split(",");
		
		String productID = st[0];
		this.quantity = Integer.parseInt(st[1]);
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
