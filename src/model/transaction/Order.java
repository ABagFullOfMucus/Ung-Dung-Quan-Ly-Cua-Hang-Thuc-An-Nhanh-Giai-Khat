package model.transaction;

import interfaces.DiscountPolicy;
import model.customer.Customer;
import model.customer.NoDiscount;

public class Order extends Transaction<OrderItem> {
    private Customer placedBy;
    private DiscountPolicy discountPolicy;

    public Order() {
        super();
        this.discountPolicy = new NoDiscount();
    }

    public Order(String id, String employeeId, Customer placedBy) {
        super(id, employeeId);
        this.placedBy = placedBy;
        this.discountPolicy = new NoDiscount();
    }

    public Customer getPlacedBy() {
        return placedBy;
    }

    public void setPlacedBy(Customer placedBy) {
        this.placedBy = placedBy;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public double getFinalTotal() {
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
        setStatus(TransactionStatus.COMPLETED);
        return true;
    }

    @Override
    protected boolean onConfirm() {
        return true;
    }
}