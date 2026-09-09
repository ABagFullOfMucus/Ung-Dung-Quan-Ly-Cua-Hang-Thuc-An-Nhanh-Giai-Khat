public class Customer extends Person {
	private String memberType;
	
	private int loyaltyPoints;
	
	// constructor
	public Customer() {
		super();
	}
	
	public Customer(String id, String name, String phoneNumber, String memberType, int loyaltyPoints) {
		super(id, name, phoneNumber);
		this.memberType = memberType;
		this.loyaltyPoints = loyaltyPoints;
	}
	
	public void addPoints(int points){
		this.loyaltyPoints += points;
	}
	
	@Override
	public void displayInfo() {
		// pass
	}
	
	// setter
	
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	
	public void setLoyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	
	// getter
	
	public String getMemberType() {
		return this.memberType;
	}
	
	public int getLoyaltyPoints() {
		return this.loyaltyPoints;
	}
	
}
