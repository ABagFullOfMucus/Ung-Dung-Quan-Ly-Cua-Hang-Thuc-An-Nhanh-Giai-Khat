public class Store {
	private String storeName;
	private String address;
	private String phoneNumber;
	
	// constructor
	public Store() {
		
	}
	
	public Store(String storeName, String address, String phoneNumber) {
		this.storeName = storeName;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
	
	// setter
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	// getter
	public String getStoreName() {
		return this.storeName;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
}
