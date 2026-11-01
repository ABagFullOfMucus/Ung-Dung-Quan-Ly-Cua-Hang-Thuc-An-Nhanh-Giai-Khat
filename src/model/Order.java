package model;

import interfaces.DiscountPolicy;
import interfaces.IDiscountable;

public class Order extends Transaction<OrderItem> implements IDiscountable {
    private Employee handledBy;
    private Customer customer;
    private DiscountPolicy discountPolicy;

    public Order(String id, Employee handledBy, Customer customer) {
        super(id);
        this.handledBy = handledBy;
        this.customer = customer;
        this.discountPolicy = (customer != null) ? new MemberDiscount() : new NoDiscount();
    }

    public Employee getHandledBy() {
        return handledBy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    @Override
    public double getFinalTotal() {
        double total = getTotal();
        if (discountPolicy != null) {
            return discountPolicy.apply(total, customer);
        }
        return total;
    }

    public boolean complete() {
        if (getStatus() != TransactionStatus.CONFIRMED) {
            return false;
        }
        double finalTotal = getFinalTotal();
        if (customer != null) {
            customer.earnPoints(finalTotal);
        }
        setStatus(TransactionStatus.COMPLETED);
        return true;
    }

    @Override
    protected boolean onConfirm() {
        return getItemCount() > 0;
    }
}
