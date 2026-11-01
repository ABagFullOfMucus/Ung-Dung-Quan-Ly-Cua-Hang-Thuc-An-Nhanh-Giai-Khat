package model;

public class OrderItem extends LineItem {
    public OrderItem() {
    }

    public OrderItem(MenuItem item, int quantity) {
        super(item, quantity, item != null ? item.getPrice() : 0.0);
    }

    public OrderItem(MenuItem item, int quantity, double unitAmount) {
        super(item, quantity, unitAmount);
    }
}
