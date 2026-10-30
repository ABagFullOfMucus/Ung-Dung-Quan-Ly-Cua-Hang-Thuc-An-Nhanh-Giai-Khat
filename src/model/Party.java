package model;

public abstract class Party extends BaseEntity {
    protected String name;
    protected String phoneNumber;
    protected String address;

    public Party() {
    }

    public Party(String id, String name, String phoneNumber, String address) {
        super(id);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void rename(String name) {
        this.name = name;
    }

    public void changeContact(String phone, String address) {
        this.phoneNumber = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
