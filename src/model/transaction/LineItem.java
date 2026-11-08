package model.transaction;

import model.menu.MenuItem;

public abstract class LineItem {
    private MenuItem menuItem;
    private int quantity;
    private double unitAmount;

    public LineItem(MenuItem menuItem, int quantity, double unitAmount) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.unitAmount = unitAmount;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitAmount() {
        return unitAmount;
    }

    public double getSubTotal() {
        return quantity * unitAmount;
    }

    public boolean changeQuantity(int q) {
        if (q <= 0) {
            return false;
        }
        this.quantity = q;
        return true;
    }
}
