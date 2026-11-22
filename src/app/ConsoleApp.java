package app;

import common.Result;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import model.customer.Customer;
import model.employee.Employee;
import model.employee.FullTimeEmployee;
import model.employee.PartTimeEmployee;
import model.employee.Permission;
import model.employee.Role;
import model.inventory.StockEntry;
import model.inventory.StockLog;
import model.menu.Drink;
import model.menu.DrinkSize;
import model.menu.FastFood;
import model.menu.MenuItem;
import model.supplier.Supplier;
import model.transaction.ImportItem;
import model.transaction.ImportOrder;
import model.transaction.Order;
import model.transaction.OrderItem;
import model.transaction.TransactionStatus;
import service.interfaces.IAuthService;
import service.interfaces.ICustomerService;
import service.interfaces.IEmployeeService;
import service.interfaces.IImportService;
import service.interfaces.IInventoryService;
import service.interfaces.IMenuService;
import service.interfaces.IOrderService;
import service.interfaces.IReportService;
import service.interfaces.ISupplierService;
import utils.InputValidator;

public class ConsoleApp {
    private final IAuthService authService;
    private final ICustomerService customerService;
    private final IEmployeeService employeeService;
    private final ISupplierService supplierService;
    private final IMenuService menuService;
    private final IInventoryService inventoryService;
    private final IOrderService orderService;
    private final IImportService importService;
    private final IReportService reportService;

    private Employee currentUser;

    public ConsoleApp(IAuthService authService,
            ICustomerService customerService,
            IEmployeeService employeeService,
            ISupplierService supplierService,
            IMenuService menuService,
            IInventoryService inventoryService,
            IOrderService orderService,
            IImportService importService,
            IReportService reportService) {
        if (authService == null || customerService == null || employeeService == null
                || supplierService == null || menuService == null || inventoryService == null
                || orderService == null || importService == null || reportService == null) {
            throw new IllegalArgumentException("Các service truyền vào ConsoleApp không được null");
        }
        this.authService = authService;
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.supplierService = supplierService;
        this.menuService = menuService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
        this.importService = importService;
        this.reportService = reportService;
    }

    public void run() {
        while (true) {
            if (currentUser == null) {
                if (!handleLoginScreen()) {
                    System.out.println("Tạm biệt!");
                    return;
                }
                continue;
            }
            showMainMenu();
        }
    }

    private boolean handleLoginScreen() {
        System.out.println("\n================ ĐĂNG NHẬP HỆ THỐNG ================");
        System.out.println("1. Đăng nhập");
        System.out.println("0. Thoát chương trình");
        int choice = InputValidator.getInt("Chọn: ");
        if (choice == 0) {
            return false;
        }
        if (choice != 1) {
            System.out.println("Lựa chọn không hợp lệ.");
            return true;
        }

        String username = InputValidator.getNonEmptyString("Tên đăng nhập: ");
        String password = InputValidator.getNonEmptyString("Mật khẩu: ");
        Result<Employee> result = authService.login(username, password);
        if (result.isSuccess()) {
            currentUser = result.getData();
            System.out.println("Đăng nhập thành công! Xin chào "
                    + currentUser.getName() + " [" + currentUser.getRole() + "]");
        } else {
            System.out.println("Đăng nhập thất bại: " + result.getMessage());
        }
        return true;
    }

    private void showMainMenu() {
        System.out.println("\n================ MENU CHÍNH ================");
        System.out.println("Người dùng: " + currentUser.getName()
                + " (" + currentUser.getId() + " - " + currentUser.getRole() + ")");
        System.out.println("1. Quản lý khách hàng");
        System.out.println("2. Quản lý nhân viên & tài khoản" + permissionTag(Permission.MANAGE_STAFF));
        System.out.println("3. Quản lý nhà cung cấp" + permissionTag(Permission.IMPORT_STOCK));
        System.out.println("4. Quản lý thực đơn" + permissionTag(Permission.MANAGE_MENU));
        System.out.println("5. Quản lý tồn kho" + permissionTag(Permission.IMPORT_STOCK));
        System.out.println("6. Quản lý nhập hàng" + permissionTag(Permission.IMPORT_STOCK));
        System.out.println("7. Quản lý bán hàng (Đơn hàng)" + permissionTag(Permission.CREATE_ORDER));
        System.out.println("8. Báo cáo & thống kê" + permissionTag(Permission.VIEW_REPORT));
        System.out.println("9. Đổi mật khẩu cá nhân");
        System.out.println("0. Đăng xuất");

        int choice = InputValidator.getInt("Chọn chức năng: ");
        switch (choice) {
            case 1:
                menuCustomers();
                break;
            case 2:
                if (requirePermission(Permission.MANAGE_STAFF)) {
                    menuEmployees();
                }
                break;
            case 3:
                if (requirePermission(Permission.IMPORT_STOCK)) {
                    menuSuppliers();
                }
                break;
            case 4:
                if (requirePermission(Permission.MANAGE_MENU)) {
                    menuMenuItems();
                }
                break;
            case 5:
                if (requirePermission(Permission.IMPORT_STOCK)) {
                    menuInventory();
                }
                break;
            case 6:
                if (requirePermission(Permission.IMPORT_STOCK)) {
                    menuImports();
                }
                break;
            case 7:
                if (requirePermission(Permission.CREATE_ORDER)) {
                    menuOrders();
                }
                break;
            case 8:
                if (requirePermission(Permission.VIEW_REPORT)) {
                    menuReports();
                }
                break;
            case 9:
                handleChangeOwnPassword();
                break;
            case 0:
                currentUser = null;
                System.out.println("Đã đăng xuất.");
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                break;
        }
    }

    private void menuCustomers() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ KHÁCH HÀNG ---");
            System.out.println("1. Danh sách khách hàng đang hoạt động");
            System.out.println("2. Thêm khách hàng");
            System.out.println("3. Cập nhật khách hàng");
            System.out.println("4. Vô hiệu hóa khách hàng");
            System.out.println("0. Quay lại");
            int choice = InputValidator.getInt("Chọn: ");
            switch (choice) {
                case 1:
                    Customer[] customers = customerService.getActive();
                    if (customers.length == 0) {
                        System.out.println("Chưa có khách hàng nào.");
                    } else {
                        for (Customer c : customers) {
                            System.out.printf("%s | %s | SĐT: %s | ĐC: %s | Điểm: %d | Hạng: %s%n",
                                    c.getId(), c.getName(), c.getPhoneNumber(),
                                    c.getAddress(), c.getLoyaltyPoints(), c.getTier());
                        }
                    }
                    break;
                case 2: {
                    String name = InputValidator.getNonEmptyString("Tên khách hàng: ");
                    String phone = InputValidator.getNonEmptyString("Số điện thoại: ");
                    String address = InputValidator.getString("Địa chỉ (có thể để trống): ");
                    Customer customer = new Customer(null, name, phone, address, 0);
                    printResult(customerService.add(customer), "Đã thêm khách hàng");
                    break;
                }
                case 3: {
                    String id = InputValidator.getNonEmptyString("Nhập mã khách hàng cần sửa: ");
                    Result<Customer> existingRes = customerService.getById(id);
                    if (!existingRes.isSuccess()) {
                        System.out.println("Lỗi: " + existingRes.getMessage());
                        break;
                    }
                    Customer old = existingRes.getData();
                    String name = InputValidator.getNonEmptyString("Tên mới (" + old.getName() + "): ");
                    String phone = InputValidator.getNonEmptyString("SĐT mới (" + old.getPhoneNumber() + "): ");
                    String address = InputValidator.getString("Địa chỉ mới (" + old.getAddress() + "): ");
                    Customer updated = new Customer(old.getId(), name, phone, address, old.getLoyaltyPoints());
                    printResult(customerService.update(updated), "Đã cập nhật khách hàng");
                    break;
                }
                case 4: {
                    String id = InputValidator.getNonEmptyString("Nhập mã khách hàng cần khóa: ");
                    printResult(customerService.deactivate(id), "Đã vô hiệu hóa khách hàng");
                    break;
                }
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        }
    }

    private void menuEmployees() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ NHÂN VIÊN ---");
            System.out.println("1. Danh sách nhân viên đang hoạt động");
            System.out.println("2. Thêm nhân viên");
            System.out.println("3. Cập nhật thông tin nhân viên");
            System.out.println("4. Tạo tài khoản đăng nhập cho nhân viên");
            System.out.println("5. Vô hiệu hóa nhân viên");
            System.out.println("0. Quay lại");
            int choice = InputValidator.getInt("Chọn: ");
            switch (choice) {
                case 1:
                    Employee[] employees = employeeService.getActive();
                    if (employees.length == 0) {
                        System.out.println("Chưa có nhân viên nào.");
                    } else {
                        for (Employee e : employees) {
                            String user = (e.getUsername() == null || e.getUsername().isEmpty())
                                    ? "(chưa có TK)"
                                    : e.getUsername();
                            System.out.printf("%s | %s | %s | SĐT: %s | Lương: %,.0f | TK: %s%n",
                                    e.getId(), e.getName(), e.getRole(), e.getPhoneNumber(),
                                    e.calculatePay(), user);
                        }
                    }
                    break;
                case 2: {
                    String name = InputValidator.getNonEmptyString("Tên nhân viên: ");
                    String phone = InputValidator.getNonEmptyString("Số điện thoại: ");
                    String address = InputValidator.getString("Địa chỉ: ");
                    Role role = readRole();
                    System.out.println("Loại nhân viên: 1. Toàn thời gian (Full-time) | 2. Bán thời gian (Part-time)");
                    int type = InputValidator.getInt("Chọn: ");
                    Employee emp;
                    if (type == 1) {
                        double salary = InputValidator.getDouble("Lương tháng: ");
                        emp = new FullTimeEmployee(null, name, phone, address, role, salary);
                    } else if (type == 2) {
                        double rate = InputValidator.getDouble("Đơn giá giờ: ");
                        double hours = InputValidator.getDouble("Số giờ làm: ");
                        emp = new PartTimeEmployee(null, name, phone, address, role, rate, hours);
                    } else {
                        System.out.println("Loại nhân viên không hợp lệ.");
                        break;
                    }
                    printResult(employeeService.add(emp), "Đã thêm nhân viên");
                    break;
                }
                case 3: {
                    String id = InputValidator.getNonEmptyString("Nhập mã nhân viên cần sửa: ");
                    Result<Employee> existingRes = employeeService.getById(id);
                    if (!existingRes.isSuccess()) {
                        System.out.println("Lỗi: " + existingRes.getMessage());
                        break;
                    }
                    Employee old = existingRes.getData();
                    String name = InputValidator.getNonEmptyString("Tên mới (" + old.getName() + "): ");
                    String phone = InputValidator.getNonEmptyString("SĐT mới (" + old.getPhoneNumber() + "): ");
                    String address = InputValidator.getString("Địa chỉ mới (" + old.getAddress() + "): ");
                    Role role = readRole();
                    Employee updated;
                    if (old instanceof FullTimeEmployee) {
                        double salary = InputValidator.getDouble("Lương tháng mới: ");
                        updated = new FullTimeEmployee(old.getId(), name, phone, address, role, salary);
                    } else {
                        double rate = InputValidator.getDouble("Đơn giá giờ mới: ");
                        double hours = InputValidator.getDouble("Số giờ làm mới: ");
                        updated = new PartTimeEmployee(old.getId(), name, phone, address, role, rate, hours);
                    }
                    updated.setAccount(old.getAccount());
                    printResult(employeeService.update(updated), "Đã cập nhật nhân viên");
                    break;
                }
                case 4: {
                    String empId = InputValidator.getNonEmptyString("Mã nhân viên: ");
                    String username = InputValidator
                            .getNonEmptyString("Tên đăng nhập (>= 3 ký tự, không khoảng trắng): ");
                    String password = InputValidator.getNonEmptyString("Mật khẩu (>= 6 ký tự): ");
                    printResult(employeeService.createAccount(empId, username, password), "Đã tạo tài khoản");
                    break;
                }
                case 5: {
                    String id = InputValidator.getNonEmptyString("Nhập mã nhân viên cần khóa: ");
                    printResult(employeeService.deactivate(id), "Đã vô hiệu hóa nhân viên");
                    break;
                }
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        }
    }

    private void menuSuppliers() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ NHÀ CUNG CẤP ---");
            System.out.println("1. Danh sách nhà cung cấp đang hoạt động");
            System.out.println("2. Thêm nhà cung cấp");
            System.out.println("3. Cập nhật nhà cung cấp");
            System.out.println("4. Vô hiệu hóa nhà cung cấp");
            System.out.println("0. Quay lại");
            int choice = InputValidator.getInt("Chọn: ");
            switch (choice) {
                case 1:
                    Supplier[] suppliers = supplierService.getActive();
                    if (suppliers.length == 0) {
                        System.out.println("Chưa có nhà cung cấp nào.");
                    } else {
                        for (Supplier s : suppliers) {
                            System.out.printf("%s | %s (%s) | SĐT: %s | Email: %s%n",
                                    s.getId(), s.getCompanyName(), s.getName(), s.getPhoneNumber(), s.getEmail());
                        }
                    }
                    break;
                case 2: {
                    String name = InputValidator.getNonEmptyString("Tên người liên hệ: ");
                    String company = InputValidator.getNonEmptyString("Tên công ty: ");
                    String phone = InputValidator.getNonEmptyString("Số điện thoại: ");
                    String email = InputValidator.getNonEmptyString("Email: ");
                    String address = InputValidator.getString("Địa chỉ: ");
                    Supplier supplier = new Supplier(null, name, phone, address, company, email);
                    printResult(supplierService.add(supplier), "Đã thêm nhà cung cấp");
                    break;
                }
                case 3: {
                    String id = InputValidator.getNonEmptyString("Nhập mã NCC cần sửa: ");
                    Result<Supplier> existingRes = supplierService.getById(id);
                    if (!existingRes.isSuccess()) {
                        System.out.println("Lỗi: " + existingRes.getMessage());
                        break;
                    }
                    Supplier old = existingRes.getData();
                    String name = InputValidator.getNonEmptyString("Tên liên hệ mới (" + old.getName() + "): ");
                    String company = InputValidator.getNonEmptyString("Công ty mới (" + old.getCompanyName() + "): ");
                    String phone = InputValidator.getNonEmptyString("SĐT mới (" + old.getPhoneNumber() + "): ");
                    String email = InputValidator.getNonEmptyString("Email mới (" + old.getEmail() + "): ");
                    String address = InputValidator.getString("Địa chỉ mới (" + old.getAddress() + "): ");
                    Supplier updated = new Supplier(old.getId(), name, phone, address, company, email);
                    printResult(supplierService.update(updated), "Đã cập nhật nhà cung cấp");
                    break;
                }
                case 4: {
                    String id = InputValidator.getNonEmptyString("Nhập mã NCC cần khóa: ");
                    printResult(supplierService.deactivate(id), "Đã vô hiệu hóa nhà cung cấp");
                    break;
                }
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        }
    }

    private void menuMenuItems() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ THỰC ĐƠN ---");
            System.out.println("1. Danh sách món đang bán");
            System.out.println("2. Thêm món mới");
            System.out.println("3. Cập nhật món");
            System.out.println("4. Ngừng bán (vô hiệu hóa) món");
            System.out.println("0. Quay lại");
            int choice = InputValidator.getInt("Chọn: ");
            switch (choice) {
                case 1:
                    MenuItem[] items = menuService.getActive();
                    if (items.length == 0) {
                        System.out.println("Thực đơn đang trống.");
                    } else {
                        for (MenuItem item : items) {
                            String detail = item instanceof Drink
                                    ? "Đồ uống - Size " + ((Drink) item).getSize()
                                    : item instanceof FastFood
                                            ? "Đồ ăn nhanh - " + ((FastFood) item).getCalories() + " kcal"
                                            : "";
                            System.out.printf("%s | %s | Giá: %,.0f | %s | Tồn: %d%n",
                                    item.getId(), item.getName(), item.getPrice(),
                                    detail, inventoryService.getQuantity(item.getId()));
                        }
                    }
                    break;
                case 2: {
                    int type = -1;
                    while (true) {
                        System.out.println("Loại món: 1. Đồ uống (Drink) | 2. Đồ ăn nhanh (FastFood) | 0. Quay lại");
                        type = InputValidator.getInt("Chọn: ");
                        if (type == 0) {
                            break;
                        }
                        if (type == 1 || type == 2) {
                            break;
                        }
                        System.out.println("Lựa chọn không hợp lệ, vui lòng chọn 1, 2 hoặc 0.");
                    }
                    if (type == 0) {
                        break;
                    }

                    String name = InputValidator.getNonEmptyString("Tên món: ");
                    double price = InputValidator.getDouble("Giá bán: ");
                    String desc = InputValidator.getString("Mô tả: ");
                    MenuItem item;
                    if (type == 1) {
                        DrinkSize size = readDrinkSize();
                        if (size == null) {
                            System.out.println("Đã hủy thêm món.");
                            break;
                        }
                        item = new Drink(null, name, price, desc, size);
                    } else {
                        double cal = InputValidator.getDouble("Calories: ");
                        item = new FastFood(null, name, price, desc, cal);
                    }
                    printResult(menuService.add(item), "Đã thêm món mới");
                    break;
                }
                case 3: {
                    String id = InputValidator.getNonEmptyString("Nhập mã món cần sửa: ");
                    Result<MenuItem> existingRes = menuService.getById(id);
                    if (!existingRes.isSuccess()) {
                        System.out.println("Lỗi: " + existingRes.getMessage());
                        break;
                    }
                    MenuItem old = existingRes.getData();
                    String name = InputValidator.getNonEmptyString("Tên mới (" + old.getName() + "): ");
                    double price = InputValidator.getDouble("Giá mới (" + old.getPrice() + "): ");
                    String desc = InputValidator.getString("Mô tả mới (" + old.getDescription() + "): ");
                    MenuItem updated;
                    if (old instanceof Drink) {
                        DrinkSize size = readDrinkSize();
                        updated = new Drink(old.getId(), name, price, desc, size);
                    } else {
                        double cal = InputValidator.getDouble("Calories mới: ");
                        updated = new FastFood(old.getId(), name, price, desc, cal);
                    }
                    printResult(menuService.update(updated), "Đã cập nhật món");
                    break;
                }
                case 4: {
                    String id = InputValidator.getNonEmptyString("Nhập mã món cần ngừng bán: ");
                    printResult(menuService.deactivate(id), "Đã ngừng bán món");
                    break;
                }
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        }
    }

    private void menuInventory() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ TỒN KHO ---");
            System.out.println("1. Xem tồn kho các món đang bán");
            System.out.println("2. Điều chỉnh số lượng tồn kho");
            System.out.println("3. Xem lịch sử biến động kho theo món");
            System.out.println("0. Quay lại");
            int choice = InputValidator.getInt("Chọn: ");
            switch (choice) {
                case 1:
                    MenuItem[] items = menuService.getActive();
                    for (MenuItem item : items) {
                        System.out.printf("%s | %-25s | Tồn kho: %d%n",
                                item.getId(), item.getName(), inventoryService.getQuantity(item.getId()));
                    }
                    break;
                case 2: {
                    String itemId = InputValidator.getNonEmptyString("Mã món: ");
                    Result<MenuItem> itemRes = menuService.getById(itemId);
                    if (!itemRes.isSuccess()) {
                        System.out.println("Lỗi: " + itemRes.getMessage());
                        break;
                    }
                    int newQty = InputValidator.getInt("Số lượng tồn kho thực tế mới: ");
                    Result<StockEntry> res = inventoryService.adjust(itemRes.getData(), newQty, currentUser);
                    printResult(res, "Đã điều chỉnh tồn kho");
                    break;
                }
                case 3: {
                    String itemId = InputValidator.getNonEmptyString("Mã món: ");
                    StockLog[] logs = inventoryService.getHistory(itemId);
                    if (logs.length == 0) {
                        System.out.println("Chưa có lịch sử biến động cho món " + itemId);
                    } else {
                        for (StockLog log : logs) {
                            System.out.printf("%s | %s | %s | SL: %d (%d -> %d) | Ref: %s%n",
                                    log.getId(), log.getLogTime(), log.getChangeType(),
                                    log.getQuantity(), log.getQuantityBefore(), log.getQuantityAfter(),
                                    log.getRefId());
                        }
                    }
                    break;
                }
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        }
    }

    private void menuImports() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ NHẬP HÀNG ---");
            System.out.println("1. Danh sách phiếu nhập");
            System.out.println("2. Tạo phiếu nhập mới");
            System.out.println("3. Thêm món vào phiếu nhập");
            System.out.println("4. Xóa món khỏi phiếu nhập");
            System.out.println("5. Xác nhận nhận hàng (cộng kho)");
            System.out.println("6. Hủy phiếu nhập nháp");
            System.out.println("0. Quay lại");
            int choice = InputValidator.getInt("Chọn: ");
            switch (choice) {
                case 1:
                    ImportOrder[] imports = importService.getAll();
                    if (imports.length == 0) {
                        System.out.println("Chưa có phiếu nhập nào.");
                    } else {
                        for (ImportOrder imp : imports) {
                            String supName = imp.getSuppliedBy() == null ? "N/A" : imp.getSuppliedBy().getCompanyName();
                            System.out.printf("%s | %s | NCC: %s | Tổng: %,.0f | %d dòng%n",
                                    imp.getId(), imp.getStatus(), supName, imp.getTotal(), imp.getItems().length);
                            for (ImportItem line : imp.getItems()) {
                                System.out.printf("   - %s (%s) x %d @ %,.0f = %,.0f%n",
                                        line.getItem().getId(), line.getItem().getName(),
                                        line.getQuantity(), line.getUnitAmount(), line.getSubTotal());
                            }
                        }
                    }
                    break;
                case 2: {
                    String supplierId = InputValidator.getNonEmptyString("Mã nhà cung cấp: ");
                    Result<ImportOrder> res = importService.createImport(supplierId, currentUser);
                    printResult(res, res.isSuccess() ? "Đã tạo phiếu nhập " + res.getData().getId() : "");
                    break;
                }
                case 3: {
                    String importId = InputValidator.getNonEmptyString("Mã phiếu nhập: ");
                    String itemId = InputValidator.getNonEmptyString("Mã món: ");
                    int qty = InputValidator.getPositiveInt("Số lượng nhập: ");
                    double cost = InputValidator.getPositiveDouble("Đơn giá nhập: ");
                    printResult(importService.addItem(importId, itemId, qty, cost), "Đã thêm món vào phiếu nhập");
                    break;
                }
                case 4: {
                    String importId = InputValidator.getNonEmptyString("Mã phiếu nhập: ");
                    String itemId = InputValidator.getNonEmptyString("Mã món cần xóa: ");
                    printResult(importService.removeItem(importId, itemId), "Đã xóa món khỏi phiếu nhập");
                    break;
                }
                case 5: {
                    String importId = InputValidator.getNonEmptyString("Mã phiếu nhập cần nhận hàng: ");
                    printResult(importService.receiveImport(importId), "Đã nhận hàng và cập nhật tồn kho");
                    break;
                }
                case 6: {
                    String importId = InputValidator.getNonEmptyString("Mã phiếu nhập cần hủy: ");
                    printResult(importService.cancelImport(importId), "Đã hủy phiếu nhập");
                    break;
                }
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        }
    }

    private void menuOrders() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ ĐƠN HÀNG ---");
            System.out.println("1. Danh sách đơn hàng");
            System.out.println("2. Tạo đơn hàng mới");
            System.out.println("3. Thêm món vào đơn hàng");
            System.out.println("4. Xóa món khỏi đơn hàng");
            System.out.println("5. Xác nhận đơn hàng (trừ kho)");
            System.out.println("6. Hoàn tất đơn hàng (tính doanh thu & cộng điểm)");
            System.out.println("7. Hủy đơn hàng");
            System.out.println("0. Quay lại");
            int choice = InputValidator.getInt("Chọn: ");
            switch (choice) {
                case 1:
                    Order[] orders = orderService.getAll();
                    if (orders.length == 0) {
                        System.out.println("Chưa có đơn hàng nào.");
                    } else {
                        for (Order o : orders) {
                            String custName = o.getPlacedBy() == null ? "Khách vãng lai" : o.getPlacedBy().getName();
                            System.out.printf("%s | %s | KH: %s | Tạm tính: %,.0f | Thành tiền: %,.0f%n",
                                    o.getId(), o.getStatus(), custName, o.getTotal(), o.getFinalTotal());
                            for (OrderItem line : o.getItems()) {
                                System.out.printf("   - %s (%s) x %d @ %,.0f = %,.0f%n",
                                        line.getItem().getId(), line.getItem().getName(),
                                        line.getQuantity(), line.getUnitAmount(), line.getSubTotal());
                            }
                        }
                    }
                    break;
                case 2: {
                    String customerId = InputValidator.getString("Mã khách hàng (để trống nếu khách vãng lai): ");
                    Result<Order> res = orderService.createOrder(customerId, currentUser);
                    printResult(res, res.isSuccess() ? "Đã tạo đơn hàng " + res.getData().getId() : "");
                    break;
                }
                case 3: {
                    String orderId = InputValidator.getNonEmptyString("Mã đơn hàng: ");
                    String itemId = InputValidator.getNonEmptyString("Mã món: ");
                    int qty = InputValidator.getPositiveInt("Số lượng: ");
                    printResult(orderService.addItem(orderId, itemId, qty), "Đã thêm món vào đơn hàng");
                    break;
                }
                case 4: {
                    String orderId = InputValidator.getNonEmptyString("Mã đơn hàng: ");
                    String itemId = InputValidator.getNonEmptyString("Mã món cần xóa: ");
                    printResult(orderService.removeItem(orderId, itemId), "Đã xóa món khỏi đơn hàng");
                    break;
                }
                case 5: {
                    String orderId = InputValidator.getNonEmptyString("Mã đơn hàng cần xác nhận: ");
                    printResult(orderService.confirmOrder(orderId), "Đã xác nhận đơn hàng và xuất kho");
                    break;
                }
                case 6: {
                    String orderId = InputValidator.getNonEmptyString("Mã đơn hàng cần hoàn tất: ");
                    printResult(orderService.completeOrder(orderId), "Đã hoàn tất đơn hàng");
                    break;
                }
                case 7: {
                    String orderId = InputValidator.getNonEmptyString("Mã đơn hàng cần hủy: ");
                    printResult(orderService.cancelOrder(orderId), "Đã hủy đơn hàng");
                    break;
                }
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        }
    }

    private void menuReports() {
        while (true) {
            System.out.println("\n--- BÁO CÁO & THỐNG KÊ ---");
            System.out.println("1. Doanh thu theo khoảng ngày");
            System.out.println("2. Danh sách món sắp hết hàng");
            System.out.println("3. Thống kê thực đơn");
            System.out.println("4. Thống kê nhân viên");
            System.out.println("5. Thống kê tồn kho");
            System.out.println("6. Thống kê đơn hàng");
            System.out.println("0. Quay lại");
            int choice = InputValidator.getInt("Chọn: ");
            switch (choice) {
                case 1: {
                    LocalDate from = readDate("Từ ngày (yyyy-MM-dd): ");
                    LocalDate to = readDate("Đến ngày (yyyy-MM-dd): ");
                    double revenue = reportService.revenueBetween(from, to);
                    System.out.printf("Tổng doanh thu từ %s đến %s: %,.0f VND%n", from, to, revenue);
                    break;
                }
                case 2: {
                    int threshold = InputValidator.getInt("Ngưỡng tồn kho cảnh báo: ");
                    MenuItem[] lowItems = reportService.lowStockItems(threshold);
                    if (lowItems.length == 0) {
                        System.out.println("Không có món nào dưới ngưỡng " + threshold);
                    } else {
                        System.out.println("Các món có tồn kho <= " + threshold + ":");
                        for (MenuItem item : lowItems) {
                            System.out.printf(" - %s | %s | Tồn: %d%n",
                                    item.getId(), item.getName(), inventoryService.getQuantity(item.getId()));
                        }
                    }
                    break;
                }
                case 3:
                    reportMenuStatistics();
                    break;
                case 4:
                    reportEmployeeStatistics();
                    break;
                case 5:
                    reportStockStatistics();
                    break;
                case 6:
                    reportOrderStatistics();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        }
    }

    private void reportMenuStatistics() {
        System.out.println("\n-- Thống kê thực đơn --");
        MenuItem highest = reportService.highestPricedMenuItem();
        MenuItem lowest = reportService.lowestPricedMenuItem();
        double avgPrice = reportService.averageActiveMenuPrice();
        MenuItem[] activeItems = menuService.getActive();
        int drinkCount = 0;
        int foodCount = 0;
        for (int i = 0; i < activeItems.length; i++) {
            MenuItem item = activeItems[i];
            if (item instanceof Drink) {
                drinkCount++;
            } else if (item instanceof FastFood) {
                foodCount++;
            }
        }

        if (highest == null || lowest == null) {
            System.out.println("Chưa có món đang bán.");
            return;
        }

        System.out.printf("Món giá cao nhất: %s | %s | %,.0f VND%n", highest.getId(), highest.getName(),
                highest.getPrice());
        System.out.printf("Món giá thấp nhất: %s | %s | %,.0f VND%n", lowest.getId(), lowest.getName(),
                lowest.getPrice());
        System.out.printf("Giá trung bình món đang bán: %,.0f VND%n", avgPrice);
        System.out.printf("Số món đang bán: %d (Đồ uống: %d, Đồ ăn nhanh: %d)%n", activeItems.length, drinkCount,
                foodCount);
    }

    private void reportEmployeeStatistics() {
        System.out.println("\n-- Thống kê nhân viên --");
        Employee highest = reportService.highestPaidActiveEmployee();
        Employee lowest = reportService.lowestPaidActiveEmployee();
        double avgPay = reportService.averageActiveEmployeePay();
        double totalPayroll = reportService.totalActivePayroll();
        int activeCount = employeeService.getActive().length;

        if (highest == null || lowest == null) {
            System.out.println("Chưa có nhân viên đang hoạt động.");
            return;
        }

        System.out.printf("Nhân viên lương cao nhất: %s | %s | %s | %,.0f VND%n", highest.getId(), highest.getName(),
                highest.getRole(), highest.calculatePay());
        System.out.printf("Nhân viên lương thấp nhất: %s | %s | %s | %,.0f VND%n", lowest.getId(), lowest.getName(),
                lowest.getRole(), lowest.calculatePay());
        System.out.printf("Thu nhập trung bình của nhân viên: %,.0f VND%n", avgPay);
        System.out.printf("Tổng quỹ lương của (%d nhân viên đang hoạt động): %,.0f VND%n", activeCount, totalPayroll);
    }

    private void reportStockStatistics() {
        System.out.println("\n-- Thống kê tồn kho --");
        MenuItem highest = reportService.highestStockMenuItem();
        MenuItem lowest = reportService.lowestStockMenuItem();
        int totalQty = reportService.totalStockQuantity();
        double estimatedValue = reportService.estimatedStockValue();

        if (highest == null || lowest == null) {
            System.out.println("Chưa có món đang bán.");
            return;
        }

        int highestQty = inventoryService.getQuantity(highest.getId());
        int lowestQty = inventoryService.getQuantity(lowest.getId());
        System.out.printf("Món tồn kho cao nhất: %s | %s | Tồn kho: %d%n", highest.getId(), highest.getName(),
                highestQty);
        System.out.printf("Món tồn kho thấp nhất: %s | %s | Tồn kho: %d%n", lowest.getId(), lowest.getName(),
                lowestQty);
        System.out.printf("Tổng số lượng tồn kho: %d%n", totalQty);
        System.out.printf("Giá trị tồn kho ước tính (theo giá bán): %,.0f VND%n", estimatedValue);
    }

    private void reportOrderStatistics() {
        System.out.println("\n-- Thống kê đơn hàng --");
        Order highest = reportService.highestValueOrder();
        double avgCompleted = reportService.averageCompletedOrderValue();
        int draft = reportService.countOrdersByStatus(TransactionStatus.DRAFT);
        int confirmed = reportService.countOrdersByStatus(TransactionStatus.CONFIRMED);
        int completed = reportService.countOrdersByStatus(TransactionStatus.COMPLETED);
        int cancelled = reportService.countOrdersByStatus(TransactionStatus.CANCELLED);

        if (highest == null) {
            System.out.println("Chưa có đơn hàng.");
        } else {
            double value = highest.getStatus() == TransactionStatus.COMPLETED
                    ? highest.getFinalTotal()
                    : highest.getTotal();
            String customerName = highest.getPlacedBy() == null ? "Khách vãng lai" : highest.getPlacedBy().getName();
            System.out.printf("Đơn giá trị cao nhất: %s | %s | Khách hàng: %s | Giá trị: %,.0f VND%n", highest.getId(),
                    highest.getStatus(), customerName, value);
        }

        System.out.printf("Giá trị trung bình đơn hoàn tất: %,.0f VND%n", avgCompleted);
        System.out.printf("Số đơn theo trạng thái - DRAFT: %d, CONFIRMED: %d, COMPLETED: %d, CANCELLED: %d%n",
                draft, confirmed, completed, cancelled);
    }

    private void handleChangeOwnPassword() {
        String oldPw = InputValidator.getNonEmptyString("Mật khẩu hiện tại: ");
        String newPw = InputValidator.getNonEmptyString("Mật khẩu mới (>= 6 ký tự): ");
        Result<Employee> res = employeeService.changePassword(currentUser.getId(), oldPw, newPw);
        printResult(res, "Đã đổi mật khẩu thành công");
    }

    private boolean requirePermission(Permission permission) {
        if (currentUser == null || currentUser.getRole() == null || !currentUser.getRole().can(permission)) {
            System.out.println("Bạn không có quyền truy cập chức năng này (" + permission + ").");
            return false;
        }
        return true;
    }

    private String permissionTag(Permission permission) {
        if (currentUser != null && currentUser.getRole() != null && currentUser.getRole().can(permission)) {
            return "";
        }
        return " [Không có quyền]";
    }

    private Role readRole() {
        while (true) {
            System.out.println("Chọn vai trò: 1. ADMIN | 2. MANAGER | 3. CASHIER | 4. WAITER | 0. Hủy ");
            int choice = InputValidator.getInt("Chọn: ");
            switch (choice) {
                case 0:
                    return null;
                case 1:
                    return Role.ADMIN;
                case 2:
                    return Role.MANAGER;
                case 3:
                    return Role.CASHIER;
                case 4:
                    return Role.WAITER;
                default:
                    System.out.println("Lựa chọn vai trò không hợp lệ.");
                    break;
            }
        }
    }

    private DrinkSize readDrinkSize() {
        while (true) {
            System.out.println("Chọn kích cỡ: 1. S | 2. M | 3. L | 0.Hủy");
            int choice = InputValidator.getInt("Chọn: ");
            switch (choice) {
                case 0:
                    return null;
                case 1:
                    return DrinkSize.S;
                case 2:
                    return DrinkSize.M;
                case 3:
                    return DrinkSize.L;
                default:
                    System.out.println("Lựa chọn kích cỡ không hợp lệ.");
                    break;
            }
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            String raw = InputValidator.getNonEmptyString(prompt);
            try {
                return LocalDate.parse(raw);
            } catch (DateTimeParseException e) {
                System.out.println("Định dạng ngày không hợp lệ, vui lòng nhập dạng yyyy-MM-dd (vd: 2026-11-20).");
            }
        }
    }

    private <T> void printResult(Result<T> result, String successMessage) {
        if (result.isSuccess()) {
            System.out.println("Thành công: " + successMessage);
        } else {
            System.out.println("Thất bại: " + result.getMessage());
        }
    }
}