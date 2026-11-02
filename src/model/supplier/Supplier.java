package model.supplier;

import model.base.Party;

public class Supplier extends Party {
    private String email;

    public Supplier() {
    }

    public Supplier(String id, String name, String phoneNumber, String address, String email) {
        super(id, name, phoneNumber, address);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
