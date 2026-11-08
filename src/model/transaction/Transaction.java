package model.transaction;

import java.time.LocalDateTime;
import model.base.BaseEntity;
import model.employee.Employee;

public abstract class Transaction<I extends LineItem> extends BaseEntity {
    private LocalDateTime time;
    private TransactionStatus status;
    private Employee handledBy;
    private LineItem[] items;
    private int itemCount;

    public Transaction(String id, Employee handledBy) {
        super(id);
        this.time = LocalDateTime.now();
        this.status = TransactionStatus.DRAFT;
        this.handledBy = handledBy;
        this.items = new LineItem[0];
        this.itemCount = 0;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Employee getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(Employee handledBy) {
        this.handledBy = handledBy;
    }

    public LineItem[] getItems() {
        LineItem[] result = new LineItem[itemCount];
        for (int i = 0; i < itemCount; i++) {
            result[i] = items[i];
        }
        return result;
    }

    public boolean addItem(I item) {
        if (this.status != TransactionStatus.DRAFT || item == null || itemCount == 0) {
            return false;
        }

        // Kiểm tra xem item đã tồn tại trong danh sách chưa
        for (int i = 0; i < itemCount; i++) {
            LineItem existing = items[i];
            if (existing != null && existing.getMenuItem() != null && item.getMenuItem() != null
                    && existing.getMenuItem().getId().equals(item.getMenuItem().getId())) {
                return existing.changeQuantity(existing.getQuantity() + item.getQuantity());
            }
        }

        // Tăng dung lượng mảng
        LineItem[] newArray = new LineItem[itemCount + 1];
        for (int i = 0; i < itemCount; i++) {
            newArray[i] = items[i];
        }
        newArray[itemCount] = item;
        this.items = newArray;
        this.itemCount++;
        return true;
    }

    public boolean removeItem(String itemId) {
        if (this.status != TransactionStatus.DRAFT || itemId == null || this.itemCount == 0) {
            return false;
        }

        int index = -1;
        for (int i = 0; i < itemCount; i++) {
            LineItem lineItem = items[i];
            if (lineItem != null && lineItem.getMenuItem() != null && itemId.equals(lineItem.getMenuItem().getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) return false;

        LineItem[] newArray = new LineItem[itemCount - 1];
        int k = 0;
        for (int i = 0; i < itemCount; i++) {
            if (i != index) {
                newArray[k++] = items[i];
            }
        }
        this.items = newArray;
        this.itemCount--;
        return true;
    }

    public double getTotal() {
        double total = 0.0;
        for (int i = 0; i < itemCount; i++) {
            LineItem item = items[i];
            if (item != null) {
                total += item.getSubTotal();
            }
        }
        return total;
    }

    public final boolean confirm() {
        if (this.status != TransactionStatus.DRAFT) {
            return false;
        }
        onConfirm();
        this.status = TransactionStatus.CONFIRMED;
        return true;
    }

    public boolean cancel() {
        if (this.status != TransactionStatus.DRAFT) {
            return false;
        }
        this.status = TransactionStatus.CANCELLED;
        return true;
    }

    protected abstract void onConfirm();
}