package model.transaction;

import interfaces.IDiscountPolicy;
import model.customer.Customer;
import model.customer.NoDiscount;
import model.employee.Employee;

public class Order extends Transaction<OrderItem> {
    private Customer placedBy;
    private IDiscountPolicy discountPolicy;
    private double finalTotal = -1;

    public Order() {
        super(OrderItem.class);
        this.discountPolicy = new NoDiscount();
    }

    public Order(String id, Employee handledBy, Customer placedBy) {
        super(id, handledBy, OrderItem.class);
        this.placedBy = placedBy;
        this.discountPolicy = new NoDiscount();
    }

    public Customer getPlacedBy() {
        return placedBy;
    }

    public void setPlacedBy(Customer placedBy) {
        this.placedBy = placedBy;
    }

    public IDiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public void setDiscountPolicy(IDiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public double getFinalTotal() {
        if (getStatus() == TransactionStatus.COMPLETED && this.finalTotal >= 0) {
            return this.finalTotal;
        }
        return calculateDiscountedTotal();
    }

    public void setFinalTotal(double finalTotal) {
        this.finalTotal = finalTotal;
    }

    private double calculateDiscountedTotal() {
        double total = getTotal();
        if (discountPolicy != null) {
            return discountPolicy.apply(total, this.placedBy);
        }
        return total;
    }

    public boolean complete() {
        if (getStatus() != TransactionStatus.CONFIRMED) {
            return false;
        }
        this.finalTotal = calculateDiscountedTotal();
        setStatus(TransactionStatus.COMPLETED);
        return true;
    }

    @Override
    protected boolean onConfirm() {
        return true;
    }
}