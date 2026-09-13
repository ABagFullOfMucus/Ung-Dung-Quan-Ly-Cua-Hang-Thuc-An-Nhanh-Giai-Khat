public abstract class Product implements IStorable {
	private String productID;
	private String productName;
	
	private double price;
	
	private int stock;
	
	// constructor 
	
	public Product() {
		
	}
	
	public Product(String productID, String productName, double price, int stock) {
		this.productID = productID;
		this.productName = productName;
		this.price = price;
		this.stock = stock;
	}
	
	
	public abstract void displayInfo();
	
	// setter 
	
	public void setProductID(String productID) {
		this.productID = productID;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	
	// getter
	
	public String getProductID() {
		return this.productID;
	}
	
	public String getProductName() {
		return this.productName;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public int getStock() {
		return this.stock;
	}
}
