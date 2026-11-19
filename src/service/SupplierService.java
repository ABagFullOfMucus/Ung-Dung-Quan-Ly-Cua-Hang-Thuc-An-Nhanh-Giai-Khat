package service;

import model.supplier.Supplier;
import persistence.Repository;
import service.base.CrudService;

public class SupplierService extends CrudService<Supplier> {

    public SupplierService(Repository<Supplier> repository) {
        super(repository);
    }

    @Override
    protected String newId() {
        return nextId("NCC");
    }
}