package model;

public class ImportOrder extends Transaction<ImportItem> {
    private Supplier suppliedBy;

    public ImportOrder() {
        super();
    }

    public ImportOrder(String id, Employee handledBy, Supplier suppliedBy) {
        super(id, handledBy);
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
        return getItemCount() > 0;
    }
}
