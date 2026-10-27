package service;

import java.util.Arrays;
import utils.FileHandler;
import model.Supplier;

public class SupplierManager {
    private Supplier[] suppliers;
    private int count;

    // Constructor
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

    // Thêm nhà cung cấp
    public boolean addSupplier(Supplier s) {
        if (s == null || findSupplier(s.getSupplierID()) != null) {
            return false;
        }

        if (count >= suppliers.length) {
            resize();
        }

        suppliers[count] = s;
        count++;

        return true;
    }

    // Tìm nhà cung cấp theo mã
    public Supplier findSupplier(String id) {
        if (id == null) return null;    
        for (int i = 0; i < count; i++) {
            if (suppliers[i].getSupplierID().equalsIgnoreCase(id)) {
                return suppliers[i];
            }
        }
        return null;
    }

    // Xóa mềm nhà cung cấp
    public boolean removeSupplier(String id) {
        Supplier s = findSupplier(id);
        if (s == null || !s.isStatus()) {
            return false;
        }
        s.setStatus(false);
        return true;
    }

    // Hiển thị danh sách nhà cung cấp
    public void displayAllSuppliers() {
        if (count == 0) {
            System.out.println("Danh sách nhà cung cấp trống.");
            return;
        }

        boolean hasActive = false;
        for (int i = 0; i < count; i++) {
            if (suppliers[i].isStatus()) {
                suppliers[i].displayInfo();
                System.out.println("-------------------------");
            }
        }
        if (!hasActive) {
            System.out.println("Không có nhà cung cấp nào đang hoạt động.");
        }
    }

    public void saveToFile(String fileName) {
        FileHandler.saveToFile(fileName, suppliers, count);
    }

    public void loadFromFile(String fileName) {
        String[] lines = FileHandler.readLines(fileName);
        if (lines == null) {
            return;
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null && !lines[i].trim().isEmpty()) {
                Supplier s = new Supplier();
                s.fromFileString(lines[i]);
                addSupplier(s);
            }
        }
    }
    
    public Supplier[] getSuppliers() {
        return Arrays.copyOf(suppliers, count);
    }

    public int getCount() {
        return count;
    }
}
