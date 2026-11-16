package app;

import model.customer.Customer;
import model.employee.Employee;
import model.inventory.StockEntry;
import model.inventory.StockLog;
import model.menu.MenuItem;
import model.supplier.Supplier;
import model.transaction.ImportOrder;
import model.transaction.Order;
import persistence.Repository;
import persistence.TextFileRepository;
import persistence.converter.CustomerConverter;
import persistence.converter.EmployeeConverter;
import persistence.converter.ImportOrderConverter;
import persistence.converter.MenuItemConverter;
import persistence.converter.OrderConverter;
import persistence.converter.StockEntryConverter;
import persistence.converter.StockLogConverter;
import persistence.converter.SupplierConverter;
import service.AuthService;
import service.CustomerService;
import service.EmployeeService;
import service.ImportService;
import service.InventoryService;
import service.MenuService;
import service.OrderService;
import service.ReportService;
import service.SupplierService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Repository<Employee> employeeRepo = new TextFileRepository<>("data/employees.txt", new EmployeeConverter(), Employee.class);
        Repository<Customer> customerRepo = new TextFileRepository<>("data/customers.txt", new CustomerConverter(), Customer.class);
        Repository<Supplier> supplierRepo = new TextFileRepository<>("data/suppliers.txt", new SupplierConverter(), Supplier.class);
        Repository<MenuItem> menuRepo = new TextFileRepository<>("data/menu.txt", new MenuItemConverter(), MenuItem.class);
        Repository<StockEntry> stockRepo = new TextFileRepository<>("data/stock.txt", new StockEntryConverter(), StockEntry.class);
        Repository<StockLog> stockLogRepo = new TextFileRepository<>("data/stock_logs.txt", new StockLogConverter(), StockLog.class);
        Repository<Order> orderRepo = new TextFileRepository<>("data/orders.txt",
                new OrderConverter(menuRepo, customerRepo, employeeRepo), Order.class);
        Repository<ImportOrder> importRepo = new TextFileRepository<>("data/imports.txt",
                new ImportOrderConverter(menuRepo, supplierRepo, employeeRepo), ImportOrder.class);

        loadQuietly(employeeRepo, "nhân viên");
        loadQuietly(customerRepo, "khách hàng");
        loadQuietly(supplierRepo, "nhà cung cấp");
        loadQuietly(menuRepo, "thực đơn");
        loadQuietly(stockRepo, "tồn kho");
        loadQuietly(stockLogRepo, "nhật ký kho");
        loadQuietly(orderRepo, "đơn hàng");
        loadQuietly(importRepo, "phiếu nhập");

        EmployeeService employeeService = new EmployeeService(employeeRepo);
        CustomerService customerService = new CustomerService(customerRepo);
        SupplierService supplierService = new SupplierService(supplierRepo);
        MenuService menuService = new MenuService(menuRepo);
        InventoryService inventoryService = new InventoryService(stockRepo, stockLogRepo);
        OrderService orderService = new OrderService(orderRepo, menuRepo, customerRepo, inventoryService);
        ImportService importService = new ImportService(importRepo, menuRepo, supplierRepo, inventoryService);
        AuthService authService = new AuthService(employeeService);
        ReportService reportService = new ReportService(orderRepo, menuRepo, inventoryService);

        ConsoleApp app = new ConsoleApp(
                customerService,
                employeeService,
                supplierService,
                menuService,
                orderService,
                importService,
                inventoryService,
                authService,
                reportService
        );
        app.run();
    }

    private static void loadQuietly(Repository<?> repository, String label) {
        try {
            if (repository instanceof TextFileRepository) {
                ((TextFileRepository<?>) repository).load();
            }
        } catch (IOException e) {
            System.out.println("Không thể tải dữ liệu " + label + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khi tải dữ liệu " + label + ".");
        }
    }
}
