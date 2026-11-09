package model.inventory;

import java.time.LocalDateTime;

import model.base.BaseEntity;
import model.menu.MenuItem;
import model.employee.Employee;

public class StockLog extends BaseEntity {
    private final LocalDateTime logTime;
    private final StockChangeType changeType;
    private final int quantity;
    private final int quantityBefore;
    private final int quantityAfter;
    private final String refId;
    private final MenuItem item;
    private final Employee performedBy;

    public StockLog(String id, LocalDateTime logTime, StockChangeType changeType,
                    int quantity, int quantityBefore, int quantityAfter, String refId, MenuItem item, Employee performedBy) {
        super(id);
        this.logTime = logTime;
        this.changeType = changeType;
        this.quantity = quantity;
        this.quantityBefore = quantityBefore;
        this.quantityAfter = quantityAfter;
        this.refId = refId;
        this.item = item;
        this.performedBy = performedBy;
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

    public MenuItem getItem() {
        return item;
    }

    public Employee getPerformedBy(){
        return performedBy;
    }
}