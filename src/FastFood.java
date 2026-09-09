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
