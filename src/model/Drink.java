package model;

public class Drink extends MenuItem {
	private String size;
	
	private boolean isIced;
	
	// constructor
	public Drink() {
		super();
	}
	
	public Drink(String itemID, String itemName, double price, int stock) {
		super(itemID, itemName, price, stock);
	}
	
	public Drink(String itemID, String itemName, double price, int stock, String size, boolean isIced) {
		super(itemID, itemName, price, stock);
		this.size = size;
		this.isIced = isIced;
		
	}
	
	@Override
	public void displayInfo() {
		System.out.println("Loại nước: " + getItemName() + " (ID: " + getItemID() + ")");
		System.out.println("Giá tiền (1 sản phẩm): " + getPrice() + "VND | Hàng trong kho: " + getStock());
		System.out.println("Size (S/M/L): " + getSize() + " | Có đá (Có/Không): " + isIced());
		
		if (getDiscountRate() > 0.0) {
			System.out.println("Giảm giá: " + (getDiscountRate() * 100) + "% | Giá sau giảm: " + getDiscountedPrice() + "VND");
		}
	}
	
	@Override 
	public String toFileString() {
		// Định dạng: Drink,<itemID>,<itemName>,<price>,<stock>,<size>,<isIced>,<discountRate>
		return "Drink," + getItemID() + "," + getItemName() + "," + getPrice() + "," + getStock() + "," + getSize() + "," + isIced() + "," + getDiscountRate();
	}
	
	@Override
	public void fromFileString(String line) {
		String[] st = line.split(",");
		
		setItemID(st[1]);
		setItemName(st[2]);
		setPrice(Double.parseDouble(st[3]));
		setStock(Integer.parseInt(st[4]));
		this.size = st[5];
		this.isIced = Boolean.parseBoolean(st[6]);
		setDiscountRate(Double.parseDouble(st[7]));
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
