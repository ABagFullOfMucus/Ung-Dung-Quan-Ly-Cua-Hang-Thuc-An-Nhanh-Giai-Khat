package service.interfaces;

import common.Result;
import model.employee.Employee;
import model.inventory.StockEntry;
import model.inventory.StockLog;
import model.menu.MenuItem;

/**
 * Dịch vụ quản lý tồn kho và nhật ký biến động kho.
 *
 * <p>
 * Lưu ý (D1): tham số {@code by} dùng để kiểm tra phân quyền thao tác kho.
 * {@link StockLog} hiện chưa có trường lưu mã nhân viên thực hiện nên chưa ghi
 * vết
 * người thực hiện vào nhật ký.
 * </p>
 */
public interface IInventoryService {
    Result<StockEntry> receive(MenuItem item, int qty, String ref, Employee by);

    Result<StockEntry> issue(MenuItem item, int qty, String ref, Employee by);

    Result<StockEntry> adjust(MenuItem item, int newQty, Employee by);

    Result<StockEntry> returnStock(MenuItem item, int qty, String ref, Employee by);

    int getQuantity(String itemId);

    StockLog[] getHistory(String itemId);
}