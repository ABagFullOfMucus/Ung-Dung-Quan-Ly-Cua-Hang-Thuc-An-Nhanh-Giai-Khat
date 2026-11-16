package model.customer;

public enum MemberTier {
    NONE(0),
    SILVER(200),
    GOLD(1000);

    private final int minPoints;

    MemberTier(int minPoints) {
        this.minPoints = minPoints;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public static MemberTier calculateTier(int points) {
        if (points >= GOLD.minPoints) {
            return GOLD;
        } else if (points >= SILVER.minPoints) {
            return SILVER;
        } else {
            return NONE;
        }
    }
}
