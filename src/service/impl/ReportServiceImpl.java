package service.impl;

import java.time.LocalDate;
import model.employee.Employee;
import model.menu.MenuItem;
import model.transaction.Order;
import model.transaction.TransactionStatus;
import service.interfaces.IEmployeeService;
import service.interfaces.IInventoryService;
import service.interfaces.IMenuService;
import service.interfaces.IOrderService;
import service.interfaces.IReportService;
import utils.ArrayUtils;

public class ReportServiceImpl implements IReportService {
    private final IOrderService orderService;
    private final IMenuService menuService;
    private final IInventoryService inventoryService;
    private final IEmployeeService employeeService;

    public ReportServiceImpl(IOrderService orderService,
            IMenuService menuService,
            IInventoryService inventoryService,
            IEmployeeService employeeService) {
        if (orderService == null) {
            throw new IllegalArgumentException("IOrderService không được null");
        }
        if (menuService == null) {
            throw new IllegalArgumentException("IMenuService không được null");
        }
        if (inventoryService == null) {
            throw new IllegalArgumentException("IInventoryService không được null");
        }
        if (employeeService == null) {
            throw new IllegalArgumentException("IEmployeeService không được null");
        }
        this.orderService = orderService;
        this.menuService = menuService;
        this.inventoryService = inventoryService;
        this.employeeService = employeeService;
    }

    @Override
    public double revenueBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            System.out.println("Lỗi: 'Từ ngày' không được lớn hơn 'Đến ngày'!");
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

    @Override
    public MenuItem highestPricedMenuItem() {
        MenuItem[] items = menuService.getActive();
        if (items == null) {
            return null;
        }
        MenuItem best = null;
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item == null) {
                continue;
            }
            if (best == null || item.getPrice() > best.getPrice()) {
                best = item;
            }
        }
        return best;
    }

    @Override
    public MenuItem lowestPricedMenuItem() {
        MenuItem[] items = menuService.getActive();
        if (items == null) {
            return null;
        }
        MenuItem best = null;
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item == null) {
                continue;
            }
            if (best == null || item.getPrice() < best.getPrice()) {
                best = item;
            }
        }
        return best;
    }

    @Override
    public double averageActiveMenuPrice() {
        MenuItem[] items = menuService.getActive();
        if (items == null) {
            return 0.0;
        }
        double total = 0.0;
        int count = 0;
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item == null) {
                continue;
            }
            total += item.getPrice();
            count++;
        }
        return count == 0 ? 0.0 : (total / count);
    }

    @Override
    public Employee highestPaidActiveEmployee() {
        Employee[] employees = employeeService.getActive();
        if (employees == null) {
            return null;
        }
        Employee best = null;
        for (int i = 0; i < employees.length; i++) {
            Employee emp = employees[i];
            if (emp == null) {
                continue;
            }
            if (best == null || emp.calculatePay() > best.calculatePay()) {
                best = emp;
            }
        }
        return best;
    }

    @Override
    public Employee lowestPaidActiveEmployee() {
        Employee[] employees = employeeService.getActive();
        if (employees == null) {
            return null;
        }
        Employee best = null;
        for (int i = 0; i < employees.length; i++) {
            Employee emp = employees[i];
            if (emp == null) {
                continue;
            }
            if (best == null || emp.calculatePay() < best.calculatePay()) {
                best = emp;
            }
        }
        return best;
    }

    @Override
    public double averageActiveEmployeePay() {
        Employee[] employees = employeeService.getActive();
        if (employees == null) {
            return 0.0;
        }
        double total = 0.0;
        int count = 0;
        for (int i = 0; i < employees.length; i++) {
            Employee emp = employees[i];
            if (emp == null) {
                continue;
            }
            total += emp.calculatePay();
            count++;
        }
        return count == 0 ? 0.0 : total / count;
    }

    @Override
    public double totalActivePayroll() {
        Employee[] employees = employeeService.getActive();
        if (employees == null) {
            return 0.0;
        }
        double total = 0.0;
        for (int i = 0; i < employees.length; i++) {
            Employee emp = employees[i];
            if (emp != null) {
                total += emp.calculatePay();
            }
        }
        return total;
    }

    @Override
    public MenuItem highestStockMenuItem() {
        MenuItem[] items = menuService.getActive();
        if (items == null) {
            return null;
        }
        MenuItem best = null;
        int bestQty = Integer.MIN_VALUE;
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item == null || item.getId() == null) {
                continue;
            }
            int qty = inventoryService.getQuantity(item.getId());
            if (best == null || qty > bestQty) {
                best = item;
                bestQty = qty;
            }
        }
        return best;
    }

    @Override
    public MenuItem lowestStockMenuItem() {
        MenuItem[] items = menuService.getActive();
        if (items == null) {
            return null;
        }

        MenuItem best = null;
        int bestQty = Integer.MAX_VALUE;
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item == null || item.getId() == null) {
                continue;
            }

            int qty = inventoryService.getQuantity(item.getId());
            if (best == null || qty < bestQty) {
                best = item;
                bestQty = qty;
            }
        }
        return best;
    }

    @Override
    public int totalStockQuantity() {
        MenuItem[] items = menuService.getActive();
        if (items == null) {
            return 0;
        }

        int total = 0;
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item != null && item.getId() != null) {
                total += inventoryService.getQuantity(item.getId());
            }
        }
        return total;
    }

    @Override
    public double estimatedStockValue() {
        MenuItem[] items = menuService.getActive();
        if (items == null) {
            return 0.0;
        }

        double total = 0.0;
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item != null && item.getId() != null) {
                int qty = inventoryService.getQuantity(item.getId());
                total += item.getPrice() * qty;
            }
        }
        return total;
    }

    @Override
    public Order highestValueOrder() {
        Order[] orders = orderService.getAll();
        if (orders == null) {
            return null;
        }

        Order best = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < orders.length; i++) {
            Order order = orders[i];
            if (order == null || order.getStatus() != TransactionStatus.CANCELLED) {
                continue;
            }

            double value = getOrderValue(order);
            if (best == null || value > bestValue) {
                best = order;
                bestValue = value;
            }
        }
        return best;
    }

    @Override
    public double averageCompletedOrderValue() {
        Order[] orders = orderService.getAll();
        if (orders == null) {
            return 0.0;
        }

        double total = 0.0;
        int count = 0;
        for (int i = 0; i < orders.length; i++) {
            Order order = orders[i];
            if (order == null || order.getStatus() != TransactionStatus.COMPLETED) {
                continue;
            }

            total += order.getFinalTotal();
            count++;
        }
        return count == 0 ? 0.0 : total / count;
    }

    @Override
    public int countOrdersByStatus(TransactionStatus status) {
        if (status == null) {
            return 0;
        }
        Order[] orders = orderService.getAll();
        if (orders == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < orders.length; i++) {
            Order order = orders[i];
            if (order != null && order.getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    private double getOrderValue(Order order) {
        if (order.getStatus() == TransactionStatus.COMPLETED) {
            return order.getFinalTotal();
        }
        return order.getTotal();
    }
}