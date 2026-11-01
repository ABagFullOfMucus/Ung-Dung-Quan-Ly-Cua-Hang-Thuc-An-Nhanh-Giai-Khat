package model.customer;

import model.base.Person;

public class Customer extends Person {
    private static final double AMOUNT_PER_POINT = 1000.0;

    private int loyaltyPoints;
    private MemberTier tier;

    public Customer() {
        this.tier = MemberTier.NONE;
    }

    public Customer(String id, String name, String phoneNumber, String address,
            int loyaltyPoints) {
        super(id, name, phoneNumber, address);
        this.loyaltyPoints = loyaltyPoints;
        this.tier = MemberTier.calculateTier(this.loyaltyPoints);
    }

    public void earnPoints(double amountSpent) {
        if (amountSpent <= 0) {
            return;
        }
        this.loyaltyPoints += (int) (amountSpent / AMOUNT_PER_POINT);
        this.tier = MemberTier.calculateTier(this.loyaltyPoints);
    }

    public boolean redeemPoints(int points) {
        if (points <= 0 || points > this.loyaltyPoints) {
            return false;
        }
        this.loyaltyPoints -= points;
        this.tier = MemberTier.calculateTier(this.loyaltyPoints);
        return true;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public MemberTier getTier() {
        return tier;
    }
    /*
     * public void setLoyaltyPoints(int loyaltyPoints) {
     * this.loyaltyPoints = loyaltyPoints;
     * this.tier = MemberTier.calculateTier(this.loyaltyPoints);
     * }
     * 
     * 
     * public void setTier(MemberTier tier) {
     * this.tier = tier == null ? MemberTier.NONE : tier;
     * }
     */
}
