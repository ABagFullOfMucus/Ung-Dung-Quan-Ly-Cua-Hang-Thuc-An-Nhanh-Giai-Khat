package model;

import java.time.LocalDateTime;
import java.util.Arrays;
import interfaces.IStatusable;

public abstract class Transaction<I> extends BaseEntity implements IStatusable{
    private LocalDateTime time;
    private TransactionStatus status;
    private Object[] items;
    private int itemCount;

    public Transaction(String id) {
        super(id);
        this.time = LocalDateTime.now();
        this.status = TransactionStatus.DRAFT;
        this.items = new Object[10];
        this.itemCount = 0;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public TransactionStatus getStatus() {
        return status;
    }

    protected void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public boolean addItem(I item) {
        if (this.status != TransactionStatus.DRAFT || item == null) {
            return false;
        }
        if (itemCount == items.length) {
            items = Arrays.copyOf(items, items.length * 2);
        }
        items[itemCount++] = item;
        return true;
    }

    public boolean removeItem(String itemId) {
        if (this.status != TransactionStatus.DRAFT || itemId == null) {
            return false;
        }
        int foundIndex = -1;
        for (int i = 0; i < itemCount; i++) {
            LineItem currentItem = (LineItem) items[i];
            if (currentItem != null && currentItem.getItem() != null && currentItem.getItem().getId().equals(itemId)) {
                foundIndex = i;
                break;
            }
        }
        if (foundIndex == -1) return false;

        for (int i = foundIndex; i < itemCount - 1; i++) {
            items[i] = items[i + 1];
        }
        items[--itemCount] = null;
        return true;
    }

    @SuppressWarnings("unchecked")
    public I[] getItems() {
        LineItem[] result = new LineItem[itemCount];
        for (int i = 0; i < itemCount; i++) {
            result[i] = (LineItem) items[i];
        }
        return (I[]) result;
    }

    public int getItemCount() {
        return itemCount;
    }

    public double getTotal() {
        double total = 0;
        for (int i = 0; i < itemCount; i++) {
            if (items[i] != null) {
                LineItem currentItem = (LineItem) items[i];
                total += currentItem.getSubTotal();
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
        if (this.status == TransactionStatus.COMPLETED) {
            return false;
        }
        this.status = TransactionStatus.CANCELLED;
        return true;
    }

    protected abstract boolean onConfirm();
}