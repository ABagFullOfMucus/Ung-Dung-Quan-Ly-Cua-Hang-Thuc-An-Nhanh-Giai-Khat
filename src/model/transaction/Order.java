package model.transaction;

import interfaces.DiscountPolicy;
import model.customer.Customer;
import model.customer.MemberDiscount;
import model.customer.NoDiscount;
import model.employee.Employee;

public class Order extends Transaction<OrderItem> {
    private Customer placedBy;
    private DiscountPolicy discountPolicy;

    public Order() {
        super();
        this.discountPolicy = new NoDiscount();
    }

    public Order(String id, Employee handledBy, Customer placedBy) {
        super(id, handledBy);
        this.placedBy = placedBy;
        this.discountPolicy = (placedBy != null) ? new MemberDiscount() : new NoDiscount();
    }

    public Customer getPlacedBy() {
        return placedBy;
    }

    public void setPlacedBy(Customer placedBy) {
        this.placedBy = placedBy;
        if (this.placedBy != null && this.discountPolicy instanceof NoDiscount) {
            this.discountPolicy = new MemberDiscount();
        }
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public double getFinalTotal() {
        return (discountPolicy == null) ? getTotal() : discountPolicy.apply(getTotal(), placedBy);
    }

    public boolean complete() {
        if (getStatus() != TransactionStatus.CONFIRMED) {
            return false;
        }
        if (placedBy != null) {
            placedBy.earnPoints(getFinalTotal());
        }
        setStatus(TransactionStatus.COMPLETED);
        return true;
    }

    @Override
    protected boolean onConfirm() {
        return !getItems().isEmpty();
    }
}