public class FastFood extends Product {
	private double calories;
	private double shelfLifeHours;
	
	
	// constructor
	
	public FastFood() {
		super();
	}
	
	public FastFood(String productID, String productName, double price, int stock, double calories, double shelfLifeHours) {
		super(productID, productName, price, stock);
		this.calories = calories;
		this.shelfLifeHours= shelfLifeHours;
	}
	
	@Override
	public void displayInfo() {
		System.out.println("Tên sản phẩm: " + getProductName() + " (ID: " + getProductID() + ")");
		System.out.println("Giá: " + getPrice() + " | Số lượng hàng còn lại trong kho: " + getStock());
		System.out.println("Lượng calo: " + getCalories());
		System.out.println("Hạn sử dụng (giờ): " + getShelfLifeHours());
	}
	
	@Override
	public String toFileString() {
		return "FastFood," + getProductName() + "," + getProductID() + "," + getPrice() + "," + getStock() + "," + getCalories() + "," + getShelfLifeHours();
	}
	
	@Override
	public void fromFileString(String line) {
		String[] st = line.split(",");
		
		setProductName(st[0]);
		setProductID(st[1]);
		setPrice(Double.parseDouble(st[2]));
		setStock(Integer.parseInt(st[3]));
		this.calories = Double.parseDouble(st[4]);
		this.shelfLifeHours = Double.parseDouble(st[5]);
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
