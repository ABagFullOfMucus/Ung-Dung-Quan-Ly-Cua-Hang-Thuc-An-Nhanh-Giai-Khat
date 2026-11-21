package app;

import common.Result;
import model.customer.Customer;
import model.employee.Employee;
import model.employee.FullTimeEmployee;
import model.employee.Role;
import model.inventory.StockEntry;
import model.inventory.StockLog;
import model.menu.MenuItem;
import model.supplier.Supplier;
import model.transaction.ImportOrder;
import model.transaction.Order;
import persistence.TextFileRepository;
import persistence.converter.CustomerConverter;
import persistence.converter.EmployeeConverter;
import persistence.converter.ImportOrderConverter;
import persistence.converter.MenuItemConverter;
import persistence.converter.OrderConverter;
import persistence.converter.StockEntryConverter;
import persistence.converter.StockLogConverter;
import persistence.converter.SupplierConverter;
import service.interfaces.IAuthService;
import service.interfaces.ICustomerService;
import service.interfaces.IEmployeeService;
import service.interfaces.IImportService;
import service.interfaces.IInventoryService;
import service.interfaces.IMenuService;
import service.interfaces.IOrderService;
import service.interfaces.IReportService;
import service.interfaces.ISupplierService;
import service.impl.AuthServiceImpl;
import service.impl.CustomerServiceImpl;
import service.impl.EmployeeServiceImpl;
import service.impl.ImportServiceImpl;
import service.impl.InventoryServiceImpl;
import service.impl.MenuServiceImpl;
import service.impl.OrderServiceImpl;
import service.impl.ReportServiceImpl;
import service.impl.SupplierServiceImpl;

public class Main {
    private static final String DATA_DIR = "data";

    public static void main(String[] args) {
        // 1. Khởi tạo các Repository (P1: truyền Class<T> để findAll() đúng kiểu)
        TextFileRepository<Employee> employeeRepo = new TextFileRepository<>(
                DATA_DIR + "/employees.txt", new EmployeeConverter(), Employee.class);
        TextFileRepository<Customer> customerRepo = new TextFileRepository<>(
                DATA_DIR + "/customers.txt", new CustomerConverter(), Customer.class);
        TextFileRepository<Supplier> supplierRepo = new TextFileRepository<>(
                DATA_DIR + "/suppliers.txt", new SupplierConverter(), Supplier.class);
        TextFileRepository<MenuItem> menuRepo = new TextFileRepository<>(
                DATA_DIR + "/menu_items.txt", new MenuItemConverter(), MenuItem.class);
        TextFileRepository<StockEntry> stockEntryRepo = new TextFileRepository<>(
                DATA_DIR + "/stock_entries.txt", new StockEntryConverter(menuRepo), StockEntry.class);
        TextFileRepository<Order> orderRepo = new TextFileRepository<>(
                DATA_DIR + "/orders.txt", new OrderConverter(menuRepo, employeeRepo, customerRepo), Order.class);
        TextFileRepository<ImportOrder> importOrderRepo = new TextFileRepository<>(
                DATA_DIR + "/import_orders.txt",
                new ImportOrderConverter(menuRepo, employeeRepo, supplierRepo),
                ImportOrder.class);
        TextFileRepository<StockLog> stockLogRepo = new TextFileRepository<>(
                DATA_DIR + "/stock_logs.txt", new StockLogConverter(), StockLog.class);

        // 2. Load theo đúng thứ tự phụ thuộc
        employeeRepo.load();
        customerRepo.load();
        supplierRepo.load();
        menuRepo.load();
        stockEntryRepo.load();
        orderRepo.load();
        importOrderRepo.load();
        stockLogRepo.load();

        // 3. Khởi tạo các Service từ dưới lên (DI, không vòng phụ thuộc)
        ICustomerService customerService = new CustomerServiceImpl(customerRepo);
        IEmployeeService employeeService = new EmployeeServiceImpl(employeeRepo);
        ISupplierService supplierService = new SupplierServiceImpl(supplierRepo);
        IMenuService menuService = new MenuServiceImpl(menuRepo);

        IAuthService authService = new AuthServiceImpl(employeeService);
        IInventoryService inventoryService = new InventoryServiceImpl(stockEntryRepo, stockLogRepo);
        IOrderService orderService = new OrderServiceImpl(
                orderRepo, customerService, menuService, inventoryService);
        IImportService importService = new ImportServiceImpl(
                importOrderRepo, supplierService, menuService, inventoryService);
        IReportService reportService = new ReportServiceImpl(
                orderService, menuService, inventoryService, employeeService); // Thêm employeeService 

        // 4. Tạo ADMIN mặc định khi chưa có nhân viên
        bootstrapDefaultAdmin(employeeService);

        // 5. Khởi chạy giao diện Console
        ConsoleApp app = new ConsoleApp(
                authService,
                customerService,
                employeeService,
                supplierService,
                menuService,
                inventoryService,
                orderService,
                importService,
                reportService);
        app.run();
    }

    private static void bootstrapDefaultAdmin(IEmployeeService employeeService) {
        Employee[] existing = employeeService.getAll();
        if (existing != null && existing.length > 0) {
            return;
        }
        Employee admin = new FullTimeEmployee(
                null, "Quản trị viên", "0900000000", "Trụ sở chính", Role.ADMIN, 15000000);
        Result<Employee> added = employeeService.add(admin);
        if (!added.isSuccess()) {
            System.out.println("Lỗi khởi tạo ADMIN mặc định: " + added.getMessage());
            return;
        }
        Result<Employee> createdAcc = employeeService.createAccount(
                added.getData().getId(), "admin", "admin123");
        if (createdAcc.isSuccess()) {
            System.out.println("====================================================");
            System.out.println("Hệ thống chưa có nhân viên. Đã tạo tài khoản ADMIN mặc định:");
            System.out.println("  Tên đăng nhập : admin");
            System.out.println("  Mật khẩu      : admin123");
            System.out.println("====================================================");
        } else {
            System.out.println("Lỗi tạo tài khoản ADMIN mặc định: " + createdAcc.getMessage());
        }
    }
}