public class Beverage extends Product {
	private String size;
	
	private boolean isIced;
	
	// constructor
	public Beverage() {
		super();
	}
	
	public Beverage(String productId, String productName, double price, int stock, String size, boolean isIced) {
		super(productId, productName, price, stock);
		this.size = size;
		this.isIced = isIced;
		
	}
	
	@Override
	public void displayInfo() {
		// pass
	}
	
	@Override 
	public String toFileString() {
		// pass
		return "";
	}
	
	@Override
	public void fromFileString(String line) {
		// pass
	}
	
	
	// setter
	public void setSize(String size) {
		this.size = size;
	}
	
	public void setIsIced(boolean isIced) {
		this.isIced = isIced;
	}
	
	
	// getter
	
	public String getSize() {
		return this.size;
	}
	
	public boolean isIced() {
		return this.isIced;
	}
}
