package model.transaction;

import model.menu.MenuItem;

public abstract class LineItem {
    private MenuItem item;
    private int quantity;
    private double unitAmount;

    public LineItem() {
    }

    public LineItem(MenuItem item, int quantity, double unitAmount) {
        this.item = item;
        this.quantity = quantity;
        this.unitAmount = unitAmount;
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

    public double getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(double unitAmount) {
        this.unitAmount = unitAmount;
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
