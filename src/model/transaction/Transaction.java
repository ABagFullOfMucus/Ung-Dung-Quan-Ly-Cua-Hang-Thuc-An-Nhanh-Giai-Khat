package model.transaction;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import model.base.BaseEntity;
import model.employee.Employee;

public abstract class Transaction<I extends LineItem> extends BaseEntity {
    private LocalDateTime time;
    private TransactionStatus status;
    private Employee handledBy;
    private List<I> items;

    @SuppressWarnings("unchecked")
    public Transaction() {
        super();
        this.time = LocalDateTime.now();
        this.status = TransactionStatus.DRAFT;
        this.items = Arrays.asList((I[]) new LineItem[0]);
    }

    @SuppressWarnings("unchecked")
    public Transaction(String id, Employee handledBy) {
        super(id);
        this.time = LocalDateTime.now();
        this.status = TransactionStatus.DRAFT;
        this.handledBy = handledBy;
        this.items = Arrays.asList((I[]) new LineItem[0]);
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

    public List<I> getItems() {
        return Collections.unmodifiableList(items);
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

        int oldSize = this.items.size();
        I[] newArray = (I[]) new LineItem[oldSize + 1];
        for (int i = 0; i < oldSize; i++) {
            newArray[i] = this.items.get(i);
        }
        newArray[oldSize] = item;
        this.items = Arrays.asList(newArray);
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean removeItem(String itemId) {
        if (this.status != TransactionStatus.DRAFT || itemId == null || this.items.isEmpty()) {
            return false;
        }

        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            I lineItem = items.get(i);
            if (lineItem != null && lineItem.getItem() != null && itemId.equals(lineItem.getItem().getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) return false;

        I[] newArray = (I[]) new LineItem[items.size() - 1];
        int k = 0;
        for (int i = 0; i < items.size(); i++) {
            if (i != index) {
                newArray[k++] = items.get(i);
            }
        }
        this.items = Arrays.asList(newArray);
        return true;
    }

    public double getTotal() {
        double total = 0.0;
        for (int i = 0; i < items.size(); i++) {
            I item = items.get(i);
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