package model.transaction;

import java.time.LocalDateTime;
import model.base.BaseEntity;
import model.employee.Employee;

public abstract class Transaction<I extends LineItem> extends BaseEntity {
    private LocalDateTime time;
    private TransactionStatus status;
    private Employee handledBy;
    private I[] items;
    private int itemCount;

    @SuppressWarnings("unchecked")
    public Transaction() {
        super();
        this.time = LocalDateTime.now();
        this.status = TransactionStatus.DRAFT;
        this.items = (I[]) new LineItem[0];
        this.itemCount = 0;
    }

    @SuppressWarnings("unchecked")
    public Transaction(String id, Employee handledBy) {
        super(id);
        this.time = LocalDateTime.now();
        this.status = TransactionStatus.DRAFT;
        this.handledBy = handledBy;
        this.items = (I[]) new LineItem[0];
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

    @SuppressWarnings("unchecked")
    public I[] getItems() {
        I[] result = (I[]) new LineItem[itemCount];
        for (int i = 0; i < itemCount; i++) {
            result[i] = items[i];
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public boolean addItem(I item) {
        if (this.status != TransactionStatus.DRAFT || item == null) {
            return false;
        }

        // Kiểm tra xem item đã tồn tại trong danh sách chưa
        for (int i = 0; i < itemCount; i++) {
            I existing = items[i];
            if (existing != null && existing.getItem() != null && item.getItem() != null
                    && existing.getItem().getId().equals(item.getItem().getId())) {
                return existing.changeQuantity(existing.getQuantity() + item.getQuantity());
            }
        }

        // Tăng dung lượng mảng
        I[] newArray = (I[]) new LineItem[itemCount + 1];
        for (int i = 0; i < itemCount; i++) {
            newArray[i] = items[i];
        }
        newArray[itemCount] = item;
        this.items = newArray;
        this.itemCount++;
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean removeItem(String itemId) {
        if (this.status != TransactionStatus.DRAFT || itemId == null || this.itemCount == 0) {
            return false;
        }

        int index = -1;
        for (int i = 0; i < itemCount; i++) {
            I lineItem = items[i];
            if (lineItem != null && lineItem.getItem() != null && itemId.equals(lineItem.getItem().getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) return false;

        I[] newArray = (I[]) new LineItem[itemCount - 1];
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
            I item = items[i];
            if (item != null) {
                total += item.getSubTotal();
            }
        }
        return total;
    }

    public boolean confirm() {
        if (this.status != TransactionStatus.DRAFT) {
            return false;
        }
        if (!onConfirm()) {
            return false;
        }
        this.status = TransactionStatus.CONFIRMED;
        return true;
    }

    public boolean cancel() {
        if (this.status == TransactionStatus.COMPLETED || this.status == TransactionStatus.CANCELLED) {
            return false;
        }
        this.status = TransactionStatus.CANCELLED;
        return true;
    }

    protected abstract boolean onConfirm();
}
