package model.transaction;

import model.menu.MenuItem;

public class ImportItem extends LineItem {
    public ImportItem() {
        super();
    }
    
    public ImportItem(MenuItem item, int quantity, double cost) {
        super(item, quantity, cost);
    }
}
