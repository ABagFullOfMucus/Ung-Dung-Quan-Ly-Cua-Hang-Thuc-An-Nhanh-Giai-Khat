package model.transaction;

import java.time.LocalDateTime;
import java.util.Arrays;
import model.base.BaseEntity;
import model.employee.Employee;

public abstract class Transaction<I extends LineItem> extends BaseEntity {
    private LocalDateTime time;
    private TransactionStatus status;
    private Employee handledBy;
    private I[] items;

    @SuppressWarnings("unchecked")
    public Transaction() {
        super();
        this.time = LocalDateTime.now();
        this.status = TransactionStatus.DRAFT;
        this.items = (I[]) new LineItem[0];
    }

    @SuppressWarnings("unchecked")
    public Transaction(String id, Employee handledBy) {
        super(id);
        this.time = LocalDateTime.now();
        this.status = TransactionStatus.DRAFT;
        this.handledBy = handledBy;
        this.items = (I[]) new LineItem[0];
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

    public I[] getItems() {
        return Arrays.copyOf(items, items.length);
    }

    @SuppressWarnings("unchecked")
    public boolean addItem(I item) {
        if (this.status != TransactionStatus.DRAFT || item == null) {
            return false;
        }

        for (I existing : items) {
            if (existing != null && existing.getItem() != null && item.getItem() != null
                    && existing.getItem().getId().equals(item.getItem().getId())) {
                return existing.changeQuantity(existing.getQuantity() + item.getQuantity());
            }
        }

        int oldSize = this.items.length;
        I[] newArray = (I[]) new LineItem[oldSize + 1];
        for (int i = 0; i < oldSize; i++) {
            newArray[i] = this.items[i];
        }
        newArray[oldSize] = item;
        this.items = newArray;
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean removeItem(String itemId) {
        if (this.status != TransactionStatus.DRAFT || itemId == null || this.items.length == 0) {
            return false;
        }

        int index = -1;
        for (int i = 0; i < items.length; i++) {
            I lineItem = items[i];
            if (lineItem != null && lineItem.getItem() != null && itemId.equals(lineItem.getItem().getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) return false;

        I[] newArray = (I[]) new LineItem[items.length - 1];
        int k = 0;
        for (int i = 0; i < items.length; i++) {
            if (i != index) {
                newArray[k++] = items[i];
            }
        }
        this.items = newArray;
        return true;
    }

    public double getTotal() {
        double total = 0.0;
        for (I item : items) {
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
        onConfirm();
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
    protected abstract void onConfirm();
}