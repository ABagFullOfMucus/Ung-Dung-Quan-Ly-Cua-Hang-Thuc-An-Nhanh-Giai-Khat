package model;

import interfaces.IStatusable;
import interfaces.IStorable;

public class Supplier implements IStorable, IStatusable {
    private String supplierID;
    private String supplierName;
    private String phoneNumber;
    private String address;
    private boolean status;

    public Supplier() {
        this.status = true;
    }

    public Supplier(String supplierID, String supplierName, String phoneNumber, String address) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = true;
    }

    // Getter / Setter 
    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }    

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public void updateStatus() {
        this.status = !this.status;
    }

    public void displayInfo() {
        System.out.println("Mã nhà cung cấp: " + supplierID);
        System.out.println("Tên nhà cung cấp: " + supplierName);
        System.out.println("Số điện thọai: " + phoneNumber);
        System.out.println("Địa chỉ: " + address);
        System.out.println("Trạng thái: " + (status ? "Hoạt động" : "Ngưng hoạt động"));
    }

    @Override
    public String toFileString() {
        return supplierID + "," + supplierName + "," + phoneNumber + "," + address + "," + status;
    }

    @Override
    public void fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        String[] data = line.split(",");

        if (data.length >= 5) {
            this.supplierID = data[0];
            this.supplierName = data[1];
            this.phoneNumber = data[2];
            this.address = data[3];
            this.status = Boolean.parseBoolean(data[4]);
        }
    }        
}
