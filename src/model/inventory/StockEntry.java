package model.inventory;

import model.base.BaseEntity;
import model.menu.MenuItem;

public class StockEntry extends BaseEntity {
    private MenuItem item;
    private int quantity;

    public StockEntry() {
        super();
    }

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

    public void increase(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    public boolean decrease(int amount) {
        if (amount <= 0 || amount > this.quantity) {
            return false;
        }
        this.quantity -= amount;
        return true;
    }
}
