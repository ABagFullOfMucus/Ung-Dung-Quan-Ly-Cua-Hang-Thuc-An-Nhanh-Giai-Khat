package service.impl;

import java.time.LocalDate;
import model.menu.MenuItem;
import model.transaction.Order;
import model.transaction.TransactionStatus;
import service.interfaces.IInventoryService;
import service.interfaces.IMenuService;
import service.interfaces.IOrderService;
import service.interfaces.IReportService;
import utils.ArrayUtils;

/**
 * Dịch vụ báo cáo doanh thu và cảnh báo tồn kho.
 *
 * <p>
 * Lưu ý (D3): {@link Order#getFinalTotal()} hiện tính chiết khấu động theo hạng
 * {@code MemberTier} hiện tại của khách hàng. Khi khách lên hạng sau khi mua,
 * doanh thu các đơn cũ có thể lệch theo hạng mới. Để cố định số liệu lịch sử ở
 * phiên bản sau, nên lưu chốt {@code finalTotal} vào {@link Order} lúc hoàn tất
 * đơn.
 * </p>
 */
public class ReportServiceImpl implements IReportService {
    private final IOrderService orderService;
    private final IMenuService menuService;
    private final IInventoryService inventoryService;

    public ReportServiceImpl(IOrderService orderService,
            IMenuService menuService,
            IInventoryService inventoryService) {
        if (orderService == null) {
            throw new IllegalArgumentException("IOrderService không được null");
        }
        if (menuService == null) {
            throw new IllegalArgumentException("IMenuService không được null");
        }
        if (inventoryService == null) {
            throw new IllegalArgumentException("IInventoryService không được null");
        }
        this.orderService = orderService;
        this.menuService = menuService;
        this.inventoryService = inventoryService;
    }

    @Override
    public double revenueBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            return 0.0;
        }
        Order[] orders = orderService.getAll();
        if (orders == null) {
            return 0.0;
        }
        double total = 0.0;
        for (int i = 0; i < orders.length; i++) {
            Order order = orders[i];
            if (order == null || order.getStatus() != TransactionStatus.COMPLETED || order.getTime() == null) {
                continue;
            }
            LocalDate orderDate = order.getTime().toLocalDate();
            if (!orderDate.isBefore(from) && !orderDate.isAfter(to)) {
                total += order.getFinalTotal();
            }
        }
        return total;
    }

    @Override
    public MenuItem[] lowStockItems(int threshold) {
        if (threshold < 0) {
            return new MenuItem[0];
        }
        MenuItem[] activeItems = menuService.getActive();
        if (activeItems == null) {
            return new MenuItem[0];
        }

        MenuItem[] buffer = new MenuItem[activeItems.length];
        for (int i = 0; i < activeItems.length; i++) {
            MenuItem item = activeItems[i];
            if (item != null && inventoryService.getQuantity(item.getId()) <= threshold) {
                buffer[i] = item;
            }
        }
        int matchedCount = ArrayUtils.countNonNull(buffer);
        return ArrayUtils.compact(buffer, new MenuItem[matchedCount]);
    }
}