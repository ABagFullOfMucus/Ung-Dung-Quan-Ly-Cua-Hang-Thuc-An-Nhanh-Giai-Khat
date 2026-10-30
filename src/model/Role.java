package model;

public enum Role {
    ADMIN,
    MANAGER,
    CASHIER,
    WAITER;

    public boolean can(Permission permission) {
        if (permission == null) {
            return false;
        }
        switch (this) {
            case ADMIN:
                return true;
            case MANAGER:
                return true;
            case CASHIER:
                return permission == Permission.CREATE_ORDER
                        || permission == Permission.VIEW_REPORT;
            case WAITER:
                return permission == Permission.CREATE_ORDER;
            default:
                return false;
        }
    }
}
