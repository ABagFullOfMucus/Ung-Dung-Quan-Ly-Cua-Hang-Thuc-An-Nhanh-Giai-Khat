package model.transaction;

import model.menu.MenuItem;

public class OrderItem extends LineItem {
    public OrderItem() {
        super();
    }

    public OrderItem(MenuItem item, int quantity, double unitAmount) {
        super(item, quantity, unitAmount);
    }
}
