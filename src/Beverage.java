public class Beverage extends Product {
	private String size;
	
	private boolean isIced;
	
	// constructor
	public Beverage() {
		super();
	}
	
	public Beverage(String productId, String productName, double price, int stock) {
		super(productId, productName, price, stock);
	}
	
	public Beverage(String productId, String productName, double price, int stock, String size, boolean isIced) {
		super(productId, productName, price, stock);
		this.size = size;
		this.isIced = isIced;
		
	}
	
	@Override
	public void displayInfo() {
		System.out.println("Loại nước: " + getProductName() + " (ID: " + getProductID() + ")");
		System.out.println("Giá tiền (1 sản phẩm): " + getPrice() + "VND | Hàng trong kho: " + getStock());
		System.out.println("Size (S/M/L): " + getSize() + " | Có đá (Có/Không): " + isIced());
	}
	
	@Override 
	public String toFileString() {
		return "Beverage," + getProductID() + "," + getProductName() + "," + getPrice() + "," + getStock() + "," + getSize() + "," + isIced();
	}
	
	@Override
	public void fromFileString(String line) {
		String[] st = line.split(",");
		
		setProductID(st[0]);
		setProductName(st[1]);
		setPrice(Double.parseDouble(st[2]));
		setStock(Integer.parseInt(st[3]));
		this.size = st[4];
		this.isIced = Boolean.parseBoolean(st[5]);
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
