package model.inventory;

import java.time.LocalDateTime;
import model.base.BaseEntity;
import model.employee.Employee;
import model.menu.MenuItem;

public class StockLog extends BaseEntity {
    private LocalDateTime logTime;
    private StockChangeType changeType;
    private int quantity;
    private int quantityBefore;
    private int quantityAfter;
    private String refId;
    private MenuItem item;
    private Employee performedBy;

    public StockLog() {
        super();
        this.logTime = LocalDateTime.now();
    }

    public StockLog(StockChangeType changeType, int quantity, int quantityBefore, int quantityAfter, String refId, MenuItem item, Employee performedBy) {
        super();
        this.logTime = LocalDateTime.now();
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

    public void setLogTime(LocalDateTime logTime) { 
        this.logTime = logTime; 
    }

    public StockChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(StockChangeType changeType) { 
        this.changeType = changeType; 
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }

    public int getQuantityBefore() {
        return quantityBefore;
    }

    public void setQuantityBefore(int quantityBefore) { 
        this.quantityBefore = quantityBefore; 
    }

    public int getQuantityAfter() {
        return quantityAfter;
    }

    public void setQuantityAfter(int quantityAfter) { 
        this.quantityAfter = quantityAfter; 
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) { 
        this.refId = refId; 
    }

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) { 
        this.item = item; 
    }

    public Employee getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(Employee performedBy) { 
        this.performedBy = performedBy; 
    }
}
