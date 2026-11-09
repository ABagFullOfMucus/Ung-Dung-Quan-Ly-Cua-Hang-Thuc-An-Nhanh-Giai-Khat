package model.transaction;

import model.menu.MenuItem;

public class ImportItem extends LineItem {    
    public ImportItem(MenuItem item, int quantity, double unitAmount) {
        super(item, quantity, unitAmount);
    }
}
