package service;

import model.menu.MenuItem;
import persistence.Repository;
import service.base.CrudService;

public class MenuService extends CrudService<MenuItem> {

    public MenuService(Repository<MenuItem> repository) {
        super(repository);
    }

    @Override
    protected String newId() {
        return nextId("MN");
    }
}