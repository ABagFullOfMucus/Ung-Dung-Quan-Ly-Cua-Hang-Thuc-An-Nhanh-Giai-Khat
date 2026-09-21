package model;

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
		System.out.println("Khách hàng: " + getName() + " (ID: " + getID() + ")");
		System.out.println("Số điện thoại: " + getPhoneNumber());
		System.out.println("Loại thành viên: " + getMemberType() + " | " + "Điểm thành viên: " + getLoyaltyPoints());
	}
	
	@Override
	public String toFileString() {
		// Định dạng: CUSTOMER,<id>,<name>,<phoneNumber>,<memberType>,<loyaltyPoints>
		return "CUSTOMER," + getID() + "," + getName() + "," + getPhoneNumber() + "," + getMemberType() + "," + getLoyaltyPoints();
	}
	
	@Override
	public void fromFileString(String line) {
		String[] st = line.split(",");
		
		setID(st[1]);
		setName(st[2]);
		setPhoneNumber(st[3]);
		this.memberType = st[4];
		this.loyaltyPoints = Integer.parseInt(st[5]);
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
