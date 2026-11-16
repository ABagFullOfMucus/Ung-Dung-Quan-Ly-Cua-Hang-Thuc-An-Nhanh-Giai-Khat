package model.transaction;

import model.employee.Employee;
import model.supplier.Supplier;

public class ImportOrder extends Transaction<ImportItem> {
    private Supplier suppliedBy;

    public ImportOrder() {
        super(ImportItem.class);
    }

    public ImportOrder(String id, Employee handledBy, Supplier suppliedBy) {
        super(ImportItem.class, id, handledBy);
        this.suppliedBy = suppliedBy;
    }

    public Supplier getSuppliedBy() {
        return suppliedBy;
    }

    public void setSuppliedBy(Supplier suppliedBy) {
        this.suppliedBy = suppliedBy;
    }

    @Override
    protected boolean onConfirm() {
        return getItems().length > 0;
    }
}
