package model.inventory;

import java.time.LocalDateTime;
import model.base.BaseEntity;
import model.employee.Employee;
import model.menu.MenuItem;

public class StockLog extends BaseEntity {
    private final String menuItemId;
    private final LocalDateTime logTime;
    private final StockChangeType changeType;
    private final int quantity;
    private final int quantityBefore;
    private final int quantityAfter;
    private final String refId;

    public StockLog(String id, String menuItemId, LocalDateTime logTime, StockChangeType changeType,
                    int quantity, int quantityBefore, int quantityAfter, String refId) {
        super(id);
        this.menuItemId = menuItemId;
        this.logTime = logTime;
        this.changeType = changeType;
        this.quantity = quantity;
        this.quantityBefore = quantityBefore;
        this.quantityAfter = quantityAfter;
        this.refId = refId;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public StockChangeType getChangeType() {
        return changeType;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantityBefore() {
        return quantityBefore;
    }

    public int getQuantityAfter() {
        return quantityAfter;
    }

    public String getRefId() {
        return refId;
    }
}
