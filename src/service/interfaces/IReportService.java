package service.interfaces;

import java.time.LocalDate;
import model.employee.Employee;
import model.menu.MenuItem;
import model.transaction.Order;
import model.transaction.TransactionStatus;

public interface IReportService {
    double revenueBetween(LocalDate from, LocalDate to);

    MenuItem[] lowStockItems(int threshold);

    // Thống Kê thuộc phần menu
    MenuItem highestPricedMenuItem();

    MenuItem lowestPricedMenuItem();

    double averageActiveMenuPrice();

    // Thống kê nhân viên
    Employee highestPaidActiveEmployee();

    Employee lowestPaidActiveEmployee();

    double averageActiveEmployeePay();

    double totalActivePayroll();

    // Thống kê kho hàng
    MenuItem highestStockMenuItem();

    MenuItem lowestStockMenuItem();

    int totalStockQuantity();

    double estimatedStockValue();

    // Thống kê order
    Order highestValueOrder();

    double averageCompletedOrderValue();

    int countOrdersByStatus(TransactionStatus status);
}