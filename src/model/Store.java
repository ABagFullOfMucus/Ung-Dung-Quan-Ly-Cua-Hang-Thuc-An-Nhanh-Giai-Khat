package model;

public class Store {
    private String storeName;
    private String address;
    private String phoneNumber;

    public Store() {
        this.storeName = "Fast Food & Beverage Store";
        this.address = "123 Main Street, District 1, HCMC";
        this.phoneNumber = "0123456789";
    }

    public Store(String storeName, String address, String phoneNumber) {
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getter / Setter
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
