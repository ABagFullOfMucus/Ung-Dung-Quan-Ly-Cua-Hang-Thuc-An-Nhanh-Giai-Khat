package model;

public class Supplier extends Party {
    private String companyName;
    private String email;

    public Supplier() {
    }

    public Supplier(String id, String name, String phoneNumber, String address,
                    String companyName, String email) {
        super(id, name, phoneNumber, address);
        this.companyName = companyName;
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
