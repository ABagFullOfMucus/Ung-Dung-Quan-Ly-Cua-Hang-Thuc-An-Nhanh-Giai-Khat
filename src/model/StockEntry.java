package model;

public class StockEntry extends BaseEntity {
    private MenuItem item;
    private int quantity;

    public StockEntry(MenuItem item, int quantity) {
        super(item != null ? item.getId() : null);
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean increase(int n) {
        if (n <= 0) return false;
        this.quantity += n;
        return true;
    }

    public boolean decrease(int n) {
        if (n <= 0 || n > this.quantity) {
            return false;
        }
        this.quantity -= n;
        return true;
    }
}
