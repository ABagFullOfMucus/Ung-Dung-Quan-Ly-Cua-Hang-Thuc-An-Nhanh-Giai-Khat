package model;

public class Customer extends Person {
    private static final double AMOUNT_PER_POINT = 1000.0;
    private static final int SILVER_THRESHOLD = 200;
    private static final int GOLD_THRESHOLD = 1000;

    private int loyaltyPoints;
    private MemberTier tier;

    public Customer() {
        this.tier = MemberTier.NONE;
    }

    public Customer(String id, String name, String phoneNumber, String address,
                    int loyaltyPoints, MemberTier tier) {
        super(id, name, phoneNumber, address);
        this.loyaltyPoints = loyaltyPoints;
        this.tier = tier == null ? MemberTier.NONE : tier;
        refreshTier();
    }

    public void earnPoints(double amountSpent) {
        if (amountSpent <= 0) {
            return;
        }
        this.loyaltyPoints += (int) (amountSpent / AMOUNT_PER_POINT);
        refreshTier();
    }

    public boolean redeemPoints(int points) {
        if (points <= 0 || points > this.loyaltyPoints) {
            return false;
        }
        this.loyaltyPoints -= points;
        refreshTier();
        return true;
    }

    private void refreshTier() {
        if (this.loyaltyPoints >= GOLD_THRESHOLD) {
            this.tier = MemberTier.GOLD;
        } else if (this.loyaltyPoints >= SILVER_THRESHOLD) {
            this.tier = MemberTier.SILVER;
        } else {
            this.tier = MemberTier.NONE;
        }
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
        refreshTier();
    }

    public MemberTier getTier() {
        return tier;
    }

    public void setTier(MemberTier tier) {
        this.tier = tier == null ? MemberTier.NONE : tier;
    }
}
