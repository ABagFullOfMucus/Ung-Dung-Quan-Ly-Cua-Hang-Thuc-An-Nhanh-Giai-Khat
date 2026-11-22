package service.interfaces;

import common.Result;
import model.employee.Employee;
import model.inventory.StockEntry;
import model.inventory.StockLog;
import model.menu.MenuItem;

public interface IInventoryService {
    Result<StockEntry> receive(MenuItem item, int qty, String ref, Employee by);

    Result<StockEntry> issue(MenuItem item, int qty, String ref, Employee by);

    Result<StockEntry> adjust(MenuItem item, int newQty, Employee by);

    Result<StockEntry> returnStock(MenuItem item, int qty, String ref, Employee by);

    int getQuantity(String itemId);

    StockLog[] getHistory(String itemId);
}