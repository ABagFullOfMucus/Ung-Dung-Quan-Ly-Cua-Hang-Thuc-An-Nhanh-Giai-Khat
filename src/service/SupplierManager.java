package service;

import model.Supplier;

public class SupplierManager {
    private Supplier[] suppliers;
    private int count;

    public SupplierManager() {
        suppliers = new Supplier[10];
        count = 0;
    }

    private void resize() {
        Supplier[] newSuppliers = new Supplier[suppliers.length * 2];
        for (int i = 0; i < count; i++) {
            newSuppliers[i] = suppliers[i];
        }
        suppliers = newSuppliers;
    }

    public boolean addSupplier(Supplier s) {
        if (s == null || findSupplier(s.getId()) != null) {
            return false;
        }

        if (count >= suppliers.length) {
            resize();
        }

        suppliers[count] = s;
        count++;
        return true;
    }

    public Supplier findSupplier(String id) {
        if (id == null) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            if (suppliers[i].getId().equalsIgnoreCase(id)) {
                return suppliers[i];
            }
        }
        return null;
    }

    public boolean removeSupplier(String id) {
        for (int i = 0; i < count; i++) {
            if (suppliers[i].getId().equalsIgnoreCase(id)) {
                for (int j = i; j < count - 1; j++) {
                    suppliers[j] = suppliers[j + 1];
                }
                suppliers[count - 1] = null;
                count--;
                return true;
            }
        }
        return false;
    }

    public void displayAllSuppliers() {
        if (count == 0) {
            System.out.println("Danh sách nhà cung cấp trống.");
            return;
        }

        System.out.println("Danh sách nhà cung cấp:");
        for (int i = 0; i < count; i++) {
            Supplier s = suppliers[i];
            System.out.println(s.getId() + " | " + s.getName() + " | "
                    + s.getPhoneNumber() + " | " + s.getAddress() + " | "
                    + s.getCompanyName() + " | " + s.getEmail());
        }
    }

    public Supplier[] getSuppliers() {
        Supplier[] result = new Supplier[count];
        for (int i = 0; i < count; i++) {
            result[i] = suppliers[i];
        }
        return result;
    }

    public int getCount() {
        return count;
    }
}
