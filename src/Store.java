public class Store implements IStorable {
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
	
	@Override
    public String toFileString() {
        return storeName + "," + address + "," + phoneNumber;
    }

    @Override
    public void fromFileString(String line) {
        String[] st = line.split(",");
        this.storeName = st[0];
        this.address = st[1];
        this.phoneNumber = st[2];
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
