package model;

public class FastFood extends MenuItem {
	private double calories;
	private double shelfLifeHours;
	
	
	// constructor
	
	public FastFood() {
		super();
	}
	
	public FastFood(String itemID, String itemName, double price, int stock, double calories, double shelfLifeHours) {
		super(itemID, itemName, price, stock);
		this.calories = calories;
		this.shelfLifeHours = shelfLifeHours;
	}
	
	@Override
	public void displayInfo() {
		System.out.println("Tên sản phẩm: " + getItemName() + " (ID: " + getItemID() + ")");
		System.out.println("Giá: " + getPrice() + " | Số lượng hàng còn lại trong kho: " + getStock());
		System.out.println("Lượng calo: " + getCalories());
		System.out.println("Hạn sử dụng (giờ): " + getShelfLifeHours());
		
		if (getDiscountRate() > 0.0) {
			System.out.println("Giảm giá: " + (getDiscountRate() * 100) + "% | Giá sau giảm: " + getDiscountedPrice() + "VND");
		}
	}
	
	@Override
	public String toFileString() {
		// Định dạng: FastFood,<itemID>,<itemName>,<price>,<stock>,<calories>,<shelfLifeHours>,<discountRate>
		return "FastFood," + getItemID() + "," + getItemName() + "," + getPrice() + "," + getStock() + "," + getCalories() + "," + getShelfLifeHours() + "," + getDiscountRate();
	}
	
	@Override
	public void fromFileString(String line) {
		String[] st = line.split(",");
		
		setItemID(st[1]);
		setItemName(st[2]);
		setPrice(Double.parseDouble(st[3]));
		setStock(Integer.parseInt(st[4]));
		this.calories = Double.parseDouble(st[5]);
		this.shelfLifeHours = Double.parseDouble(st[6]);
		setDiscountRate(Double.parseDouble(st[7]));
	}
	
	// setter
	
	
	public void setCalories(double calories) {
		this.calories = calories;
	}
	
	public void setShelfLifeHours(double shelfLifeHours) {
		this.shelfLifeHours = shelfLifeHours;
	}
	
	
	// getter
	
	public double getCalories() {
		return this.calories;
	}
	
	public double getShelfLifeHours() {
		return this.shelfLifeHours;
	}
}
