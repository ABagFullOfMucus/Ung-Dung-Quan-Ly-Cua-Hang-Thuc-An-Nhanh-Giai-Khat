package app;

import common.Result;
import java.time.LocalDate;
import model.customer.Customer;
import model.employee.Account;
import model.employee.Employee;
import model.employee.FullTimeEmployee;
import model.employee.PartTimeEmployee;
import model.employee.Role;
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
import service.AuthService;
import service.CustomerService;
import service.EmployeeService;
import service.ImportService;
import service.InventoryService;
import service.MenuService;
import service.OrderService;
import service.ReportService;
import service.SupplierService;
import utils.ConsoleInput;
import utils.Validator;

public class ConsoleApp {
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final SupplierService supplierService;
    private final MenuService menuService;
    private final OrderService orderService;
    private final ImportService importService;
    private final InventoryService inventoryService;
    private final AuthService authService;
    private final ReportService reportService;

    private Employee currentUser;

    public ConsoleApp(CustomerService customerService,
                      EmployeeService employeeService,
                      SupplierService supplierService,
                      MenuService menuService,
                      OrderService orderService,
                      ImportService importService,
                      InventoryService inventoryService,
                      AuthService authService,
                      ReportService reportService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.supplierService = supplierService;
        this.menuService = menuService;
        this.orderService = orderService;
        this.importService = importService;
        this.inventoryService = inventoryService;
        this.authService = authService;
        this.reportService = reportService;
    }

    public void run() {
        System.out.println("=== ỨNG DỤNG QUẢN LÝ CỬA HÀNG THỨC ĂN NHANH / GIẢI KHÁT ===");
        ensureCurrentUser();
        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = ConsoleInput.readIntInRange("Chọn chức năng: ", 0, 10);
            switch (choice) {
                case 1:
                    menuModule();
                    break;
                case 2:
                    orderModule();
                    break;
                case 3:
                    importModule();
                    break;
                case 4:
                    inventoryModule();
                    break;
                case 5:
                    customerModule();
                    break;
                case 6:
                    supplierModule();
                    break;
                case 7:
                    employeeModule();
                    break;
                case 8:
                    reportModule();
                    break;
                case 9:
                    login();
                    break;
                case 10:
                    printCurrentUser();
                    break;
                case 0:
                    running = false;
                    System.out.println("Tạm biệt!");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println();
        System.out.println("----- MENU CHÍNH -----");
        System.out.println("1. Thực đơn");
        System.out.println("2. Đơn hàng");
        System.out.println("3. Nhập hàng");
        System.out.println("4. Tồn kho");
        System.out.println("5. Khách hàng");
        System.out.println("6. Nhà cung cấp");
        System.out.println("7. Nhân viên");
        System.out.println("8. Báo cáo");
        System.out.println("9. Đăng nhập");
        System.out.println("10. Người dùng hiện tại");
        System.out.println("0. Thoát");
    }

    private void ensureCurrentUser() {
        if (employeeService == null) {
            return;
        }
        Employee[] employees = getEmployeesSafe();
        if (employees.length == 1) {
            currentUser = employees[0];
            System.out.println("Tự động chọn nhân viên: " + displayEmployee(currentUser));
        } else if (currentUser == null && employees.length > 1) {
            System.out.println("Chưa đăng nhập. Bạn có thể dùng mục Đăng nhập hoặc chọn nhân viên khi tạo giao dịch.");
        }
    }

    private Employee requireEmployee() {
        if (currentUser != null) {
            return currentUser;
        }
        Employee[] employees = getEmployeesSafe();
        if (employees.length == 0) {
            System.out.println("Chưa có dữ liệu nhân viên. Vui lòng thêm nhân viên trước.");
            return null;
        }
        System.out.println("Chọn nhân viên xử lý:");
        for (int i = 0; i < employees.length; i++) {
            System.out.println((i + 1) + ". " + displayEmployee(employees[i]));
        }
        int idx = ConsoleInput.readIntInRange("Nhập số thứ tự: ", 1, employees.length) - 1;
        currentUser = employees[idx];
        return currentUser;
    }

    private Employee[] getEmployeesSafe() {
        if (employeeService == null) {
            return new Employee[0];
        }
        Object[] all = employeeService.getAll();
        if (all == null) {
            return new Employee[0];
        }
        Employee[] result = new Employee[all.length];
        for (int i = 0; i < all.length; i++) {
            result[i] = (Employee) all[i];
        }
        return result;
    }

    private void login() {
        if (authService == null) {
            System.out.println("Chức năng đăng nhập chưa sẵn sàng.");
            return;
        }
        String username = ConsoleInput.readRequiredText("Tên đăng nhập: ");
        String password = ConsoleInput.readRequiredText("Mật khẩu: ");
        Result<Employee> result = authService.login(username, password);
        if (result.isSuccess()) {
            currentUser = result.getData();
            System.out.println("Đăng nhập thành công: " + displayEmployee(currentUser));
        } else {
            System.out.println(result.getMessage());
        }
    }

    private void printCurrentUser() {
        if (currentUser == null) {
            System.out.println("Chưa có người dùng hiện tại.");
        } else {
            System.out.println("Người dùng hiện tại: " + displayEmployee(currentUser));
        }
    }

    // =====================================================================
    // THỰC ĐƠN
    // =====================================================================

    private void menuModule() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- THỰC ĐƠN ---");
            System.out.println("1. Xem thực đơn");
            System.out.println("2. Thêm món");
            System.out.println("3. Sửa món");
            System.out.println("4. Ngừng bán / Bán lại");
            System.out.println("0. Quay lại");
            int choice = ConsoleInput.readIntInRange("Chọn: ", 0, 4);
            switch (choice) {
                case 1:
                    listMenuItems();
                    break;
                case 2:
                    addMenuItem();
                    break;
                case 3:
                    editMenuItem();
                    break;
                case 4:
                    toggleMenuItem();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void listMenuItems() {
        MenuItem[] items = getMenuItemsSafe();
        if (items.length == 0) {
            System.out.println("Chưa có món trong thực đơn.");
            return;
        }
        for (MenuItem item : items) {
            if (item == null) {
                continue;
            }
            System.out.println("- " + describeMenuItem(item));
        }
    }

    private void addMenuItem() {
        if (menuService == null) {
            System.out.println("MenuService chưa được khởi tạo.");
            return;
        }
        int kind = ConsoleInput.readIntInRange("Loại món (1. Đồ uống, 2. Đồ ăn): ", 1, 2);
        String name = ConsoleInput.readRequiredText("Tên món: ");
        double price = ConsoleInput.readPositiveDouble("Giá bán: ");
        String description = ConsoleInput.readLine("Mô tả (bỏ trống nếu không có): ");

        MenuItem item;
        if (kind == 1) {
            DrinkSize size = chooseEnum("Kích cỡ", DrinkSize.class);
            item = new Drink(null, name, price, description, size);
        } else {
            double calories = readNonNegativeDouble("Calories: ");
            item = new FastFood(null, name, price, description, calories);
        }

        Result<MenuItem> result = menuService.add(item);
        printResult(result, "Đã thêm món.");
        if (result.isSuccess() && result.getData() != null) {
            System.out.println("Mã món: " + result.getData().getId());
        }
    }

    private void editMenuItem() {
        if (menuService == null) {
            System.out.println("MenuService chưa được khởi tạo.");
            return;
        }
        MenuItem item = requireMenuItem();
        if (item == null) {
            return;
        }
        System.out.println("Hiện tại: " + describeMenuItem(item));

        item.setName(promptKeepText("Tên món", item.getName()));
        item.changePrice(promptKeepPositiveDouble("Giá bán", item.getPrice()));
        item.setDescription(promptKeepText("Mô tả", item.getDescription()));

        if (item instanceof Drink) {
            Drink drink = (Drink) item;
            drink.setSize(promptKeepEnum("Kích cỡ", DrinkSize.class, drink.getSize()));
        } else if (item instanceof FastFood) {
            FastFood food = (FastFood) item;
            food.setCalories(promptKeepNonNegativeDouble("Calories", food.getCalories()));
        }

        printResult(menuService.update(item), "Đã cập nhật món.");
    }

    private void toggleMenuItem() {
        if (menuService == null) {
            System.out.println("MenuService chưa được khởi tạo.");
            return;
        }
        MenuItem item = requireMenuItem();
        if (item == null) {
            return;
        }
        boolean wasActive = item.isActive();
        Result<MenuItem> result = wasActive
                ? menuService.deactivate(item.getId())
                : menuService.activate(item.getId());
        printResult(result, wasActive ? "Đã ngừng bán món." : "Đã bán lại món.");
    }

    private MenuItem requireMenuItem() {
        String id = ConsoleInput.readRequiredText("Mã món: ");
        Result<MenuItem> result = menuService.getById(id.trim());
        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
            return null;
        }
        return result.getData();
    }

    private MenuItem[] getMenuItemsSafe() {
        if (menuService == null) {
            return new MenuItem[0];
        }
        Object[] all = menuService.getAll();
        if (all == null) {
            return new MenuItem[0];
        }
        MenuItem[] result = new MenuItem[all.length];
        for (int i = 0; i < all.length; i++) {
            result[i] = (MenuItem) all[i];
        }
        return result;
    }

    private String describeMenuItem(MenuItem item) {
        String kind;
        if (item instanceof Drink) {
            kind = "Đồ uống/" + ((Drink) item).getSize();
        } else if (item instanceof FastFood) {
            kind = "Đồ ăn/" + formatNumber(((FastFood) item).getCalories()) + " kcal";
        } else {
            kind = "Khác";
        }
        String status = item.isActive() ? "" : " | ngừng bán";
        return item.getId() + " | " + kind + " | " + item.getName() + " | " + formatMoney(item.getPrice())
                + " | " + safe(item.getDescription()) + status;
    }

    // =====================================================================
    // KHÁCH HÀNG
    // =====================================================================

    private void customerModule() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- KHÁCH HÀNG ---");
            System.out.println("1. Xem danh sách");
            System.out.println("2. Thêm khách hàng");
            System.out.println("3. Sửa khách hàng");
            System.out.println("4. Ngừng hoạt động / Kích hoạt lại");
            System.out.println("0. Quay lại");
            int choice = ConsoleInput.readIntInRange("Chọn: ", 0, 4);
            switch (choice) {
                case 1:
                    listCustomers();
                    break;
                case 2:
                    addCustomer();
                    break;
                case 3:
                    editCustomer();
                    break;
                case 4:
                    toggleCustomer();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void listCustomers() {
        Customer[] customers = getCustomersSafe();
        if (customers.length == 0) {
            System.out.println("Chưa có khách hàng.");
            return;
        }
        for (Customer customer : customers) {
            if (customer == null) {
                continue;
            }
            System.out.println("- " + describeCustomer(customer));
        }
    }

    private void listCustomersBrief() {
        Customer[] customers = getCustomersSafe();
        if (customers.length == 0) {
            return;
        }
        System.out.println("Khách hàng hiện có:");
        for (Customer customer : customers) {
            if (customer != null && customer.isActive()) {
                System.out.println("  " + customer.getId() + " - " + customer.getName());
            }
        }
    }

    private void addCustomer() {
        if (customerService == null) {
            System.out.println("CustomerService chưa được khởi tạo.");
            return;
        }
        String name = ConsoleInput.readRequiredText("Tên khách hàng: ");
        String phone = readOptionalPhone("Số điện thoại (bỏ trống nếu không có): ");
        String address = ConsoleInput.readLine("Địa chỉ (bỏ trống nếu không có): ");
        int points = readNonNegativeInt("Điểm tích lũy ban đầu: ");

        Result<Customer> result = customerService.add(new Customer(null, name, phone, address, points));
        printResult(result, "Đã thêm khách hàng.");
        if (result.isSuccess() && result.getData() != null) {
            System.out.println("Mã khách hàng: " + result.getData().getId());
        }
    }

    private void editCustomer() {
        if (customerService == null) {
            System.out.println("CustomerService chưa được khởi tạo.");
            return;
        }
        Customer customer = requireCustomer();
        if (customer == null) {
            return;
        }
        System.out.println("Hiện tại: " + describeCustomer(customer));
        customer.setName(promptKeepText("Tên khách hàng", customer.getName()));
        customer.setPhoneNumber(promptKeepPhone("Số điện thoại", customer.getPhoneNumber()));
        customer.setAddress(promptKeepText("Địa chỉ", customer.getAddress()));
        printResult(customerService.update(customer), "Đã cập nhật khách hàng.");
    }

    private void toggleCustomer() {
        if (customerService == null) {
            System.out.println("CustomerService chưa được khởi tạo.");
            return;
        }
        Customer customer = requireCustomer();
        if (customer == null) {
            return;
        }
        boolean wasActive = customer.isActive();
        Result<Customer> result = wasActive
                ? customerService.deactivate(customer.getId())
                : customerService.activate(customer.getId());
        printResult(result, wasActive ? "Đã ngừng khách hàng." : "Đã kích hoạt khách hàng.");
    }

    private Customer requireCustomer() {
        String id = ConsoleInput.readRequiredText("Mã khách hàng: ");
        Result<Customer> result = customerService.getById(id.trim());
        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
            return null;
        }
        return result.getData();
    }

    private Customer[] getCustomersSafe() {
        if (customerService == null) {
            return new Customer[0];
        }
        Object[] all = customerService.getAll();
        if (all == null) {
            return new Customer[0];
        }
        Customer[] result = new Customer[all.length];
        for (int i = 0; i < all.length; i++) {
            result[i] = (Customer) all[i];
        }
        return result;
    }

    private String describeCustomer(Customer customer) {
        String phone = safe(customer.getPhoneNumber());
        String phoneMark = (!phone.isEmpty() && !Validator.isValidPhone(phone)) ? " [SĐT không hợp lệ]" : "";
        String status = customer.isActive() ? "" : " | ngừng hoạt động";
        return customer.getId() + " | " + customer.getName() + " | " + phone + phoneMark
                + " | " + customer.getLoyaltyPoints() + " điểm | " + customer.getTier() + status;
    }

    // =====================================================================
    // NHÀ CUNG CẤP
    // =====================================================================

    private void supplierModule() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- NHÀ CUNG CẤP ---");
            System.out.println("1. Xem danh sách");
            System.out.println("2. Thêm nhà cung cấp");
            System.out.println("3. Sửa nhà cung cấp");
            System.out.println("4. Ngừng hoạt động / Kích hoạt lại");
            System.out.println("0. Quay lại");
            int choice = ConsoleInput.readIntInRange("Chọn: ", 0, 4);
            switch (choice) {
                case 1:
                    listSuppliers();
                    break;
                case 2:
                    addSupplier();
                    break;
                case 3:
                    editSupplier();
                    break;
                case 4:
                    toggleSupplier();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void listSuppliers() {
        Supplier[] suppliers = getSuppliersSafe();
        if (suppliers.length == 0) {
            System.out.println("Chưa có nhà cung cấp.");
            return;
        }
        for (Supplier supplier : suppliers) {
            if (supplier == null) {
                continue;
            }
            System.out.println("- " + describeSupplier(supplier));
        }
    }

    private void listSuppliersBrief() {
        Supplier[] suppliers = getSuppliersSafe();
        if (suppliers.length == 0) {
            return;
        }
        System.out.println("Nhà cung cấp hiện có:");
        for (Supplier supplier : suppliers) {
            if (supplier != null && supplier.isActive()) {
                System.out.println("  " + supplier.getId() + " - " + supplier.getName());
            }
        }
    }

    private void addSupplier() {
        if (supplierService == null) {
            System.out.println("SupplierService chưa được khởi tạo.");
            return;
        }
        String name = ConsoleInput.readRequiredText("Tên nhà cung cấp: ");
        String phone = readOptionalPhone("Số điện thoại (bỏ trống nếu không có): ");
        String address = ConsoleInput.readLine("Địa chỉ (bỏ trống nếu không có): ");
        String company = ConsoleInput.readLine("Tên công ty (bỏ trống nếu không có): ");
        String email = readOptionalEmail("Email (bỏ trống nếu không có): ");

        Result<Supplier> result = supplierService.add(
                new Supplier(null, name, phone, address, company, email));
        printResult(result, "Đã thêm nhà cung cấp.");
        if (result.isSuccess() && result.getData() != null) {
            System.out.println("Mã nhà cung cấp: " + result.getData().getId());
        }
    }

    private void editSupplier() {
        if (supplierService == null) {
            System.out.println("SupplierService chưa được khởi tạo.");
            return;
        }
        Supplier supplier = requireSupplier();
        if (supplier == null) {
            return;
        }
        System.out.println("Hiện tại: " + describeSupplier(supplier));
        supplier.setName(promptKeepText("Tên nhà cung cấp", supplier.getName()));
        supplier.setPhoneNumber(promptKeepPhone("Số điện thoại", supplier.getPhoneNumber()));
        supplier.setAddress(promptKeepText("Địa chỉ", supplier.getAddress()));
        supplier.setCompanyName(promptKeepText("Tên công ty", supplier.getCompanyName()));
        supplier.setEmail(promptKeepEmail("Email", supplier.getEmail()));
        printResult(supplierService.update(supplier), "Đã cập nhật nhà cung cấp.");
    }

    private void toggleSupplier() {
        if (supplierService == null) {
            System.out.println("SupplierService chưa được khởi tạo.");
            return;
        }
        Supplier supplier = requireSupplier();
        if (supplier == null) {
            return;
        }
        boolean wasActive = supplier.isActive();
        Result<Supplier> result = wasActive
                ? supplierService.deactivate(supplier.getId())
                : supplierService.activate(supplier.getId());
        printResult(result, wasActive ? "Đã ngừng nhà cung cấp." : "Đã kích hoạt nhà cung cấp.");
    }

    private Supplier requireSupplier() {
        String id = ConsoleInput.readRequiredText("Mã nhà cung cấp: ");
        Result<Supplier> result = supplierService.getById(id.trim());
        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
            return null;
        }
        return result.getData();
    }

    private Supplier[] getSuppliersSafe() {
        if (supplierService == null) {
            return new Supplier[0];
        }
        Object[] all = supplierService.getAll();
        if (all == null) {
            return new Supplier[0];
        }
        Supplier[] result = new Supplier[all.length];
        for (int i = 0; i < all.length; i++) {
            result[i] = (Supplier) all[i];
        }
        return result;
    }

    private String describeSupplier(Supplier supplier) {
        String email = safe(supplier.getEmail());
        String emailMark = (!email.isEmpty() && !Validator.isValidEmail(email)) ? " [Email không hợp lệ]" : "";
        String status = supplier.isActive() ? "" : " | ngừng hoạt động";
        return supplier.getId() + " | " + supplier.getName() + " | " + safe(supplier.getCompanyName())
                + " | " + safe(supplier.getPhoneNumber()) + " | " + email + emailMark + status;
    }

    // =====================================================================
    // NHÂN VIÊN
    // =====================================================================

    private void employeeModule() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- NHÂN VIÊN ---");
            System.out.println("1. Xem danh sách");
            System.out.println("2. Thêm nhân viên");
            System.out.println("3. Sửa nhân viên");
            System.out.println("4. Ngừng hoạt động / Kích hoạt lại");
            System.out.println("0. Quay lại");
            int choice = ConsoleInput.readIntInRange("Chọn: ", 0, 4);
            switch (choice) {
                case 1:
                    listEmployees();
                    break;
                case 2:
                    addEmployee();
                    break;
                case 3:
                    editEmployee();
                    break;
                case 4:
                    toggleEmployee();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void listEmployees() {
        Employee[] employees = getEmployeesSafe();
        if (employees.length == 0) {
            System.out.println("Chưa có nhân viên.");
            return;
        }
        for (Employee employee : employees) {
            if (employee != null) {
                System.out.println("- " + describeEmployeeFull(employee));
            }
        }
    }

    private void addEmployee() {
        if (employeeService == null) {
            System.out.println("EmployeeService chưa được khởi tạo.");
            return;
        }
        int type = ConsoleInput.readIntInRange("Loại nhân viên (1. Toàn thời gian, 2. Bán thời gian): ", 1, 2);
        String name = ConsoleInput.readRequiredText("Họ tên: ");
        String phone = readOptionalPhone("Số điện thoại (bỏ trống nếu không có): ");
        String address = ConsoleInput.readLine("Địa chỉ (bỏ trống nếu không có): ");
        Role role = chooseEnum("Vai trò", Role.class);

        Employee employee;
        if (type == 1) {
            double salary = ConsoleInput.readPositiveDouble("Lương tháng: ");
            employee = new FullTimeEmployee(null, name, phone, address, role, salary);
        } else {
            double rate = ConsoleInput.readPositiveDouble("Lương/giờ: ");
            double hours = readNonNegativeDouble("Số giờ đã làm: ");
            employee = new PartTimeEmployee(null, name, phone, address, role, rate, hours);
        }

        String username = ConsoleInput.readLine("Tên đăng nhập (bỏ trống nếu chưa cấp): ");
        if (!username.isEmpty()) {
            if (isUsernameTaken(username, null)) {
                System.out.println("Tên đăng nhập đã được dùng.");
                return;
            }
            String password = ConsoleInput.readRequiredText("Mật khẩu: ");
            employee.setAccount(new Account(username, password));
        }

        Result<Employee> result = employeeService.add(employee);
        printResult(result, "Đã thêm nhân viên.");
        if (result.isSuccess() && result.getData() != null) {
            System.out.println("Mã nhân viên: " + result.getData().getId());
        }
    }

    private void editEmployee() {
        if (employeeService == null) {
            System.out.println("EmployeeService chưa được khởi tạo.");
            return;
        }
        Employee employee = requireEmployeeById();
        if (employee == null) {
            return;
        }
        System.out.println("Hiện tại: " + describeEmployeeFull(employee));

        employee.setName(promptKeepText("Họ tên", employee.getName()));
        employee.setPhoneNumber(promptKeepPhone("Số điện thoại", employee.getPhoneNumber()));
        employee.setAddress(promptKeepText("Địa chỉ", employee.getAddress()));
        employee.setRole(promptKeepEnum("Vai trò", Role.class, employee.getRole()));

        if (employee instanceof FullTimeEmployee) {
            FullTimeEmployee fullTime = (FullTimeEmployee) employee;
            fullTime.setMonthlySalary(promptKeepPositiveDouble("Lương tháng", fullTime.getMonthlySalary()));
        } else if (employee instanceof PartTimeEmployee) {
            PartTimeEmployee partTime = (PartTimeEmployee) employee;
            partTime.setHourlyRate(promptKeepPositiveDouble("Lương/giờ", partTime.getHourlyRate()));
            partTime.setHoursWorked(promptKeepNonNegativeDouble("Số giờ đã làm", partTime.getHoursWorked()));
        }

        String currentUsername = employee.getUsername() == null ? "" : employee.getUsername();
        String username = promptKeepText("Tên đăng nhập", currentUsername);
        if (!username.isEmpty() && !username.equalsIgnoreCase(currentUsername)) {
            if (isUsernameTaken(username, employee.getId())) {
                System.out.println("Tên đăng nhập đã được dùng.");
                return;
            }
        }
        if (employee.getAccount() == null) {
            employee.setAccount(new Account());
        }
        employee.getAccount().setUsername(username.isEmpty() ? null : username);

        String newPassword = ConsoleInput.readLine("Mật khẩu mới (bỏ trống để giữ nguyên): ");
        if (!newPassword.isEmpty()) {
            employee.getAccount().setPasswordHash(Account.hash(newPassword));
        }

        printResult(employeeService.update(employee), "Đã cập nhật nhân viên.");
    }

    private void toggleEmployee() {
        if (employeeService == null) {
            System.out.println("EmployeeService chưa được khởi tạo.");
            return;
        }
        Employee employee = requireEmployeeById();
        if (employee == null) {
            return;
        }
        if (employee.isActive() && currentUser != null && employee.getId().equals(currentUser.getId())) {
            System.out.println("Không thể ngừng nhân viên đang đăng nhập.");
            return;
        }
        boolean wasActive = employee.isActive();
        Result<Employee> result = wasActive
                ? employeeService.deactivate(employee.getId())
                : employeeService.activate(employee.getId());
        printResult(result, wasActive ? "Đã ngừng nhân viên." : "Đã kích hoạt nhân viên.");
    }

    private Employee requireEmployeeById() {
        String id = ConsoleInput.readRequiredText("Mã nhân viên: ");
        Result<Employee> result = employeeService.getById(id.trim());
        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
            return null;
        }
        return result.getData();
    }

    private boolean isUsernameTaken(String username, String exceptEmployeeId) {
        if (username == null || username.isBlank()) {
            return false;
        }
        for (Employee employee : getEmployeesSafe()) {
            if (employee == null) {
                continue;
            }
            if (exceptEmployeeId != null && exceptEmployeeId.equals(employee.getId())) {
                continue;
            }
            if (employee.getUsername() != null && employee.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    private String describeEmployeeFull(Employee employee) {
        String type = employee instanceof FullTimeEmployee ? "Toàn thời gian"
                : employee instanceof PartTimeEmployee ? "Bán thời gian" : "Khác";
        String username = employee.getUsername() == null || employee.getUsername().isEmpty()
                ? "(chưa có tài khoản)" : employee.getUsername();
        String status = employee.isActive() ? "" : " | ngừng hoạt động";
        return employee.getId() + " | " + employee.getName() + " | " + employee.getRole()
                + " | " + type + " | lương: " + formatMoney(employee.calculatePay())
                + " | TK: " + username + status;
    }

    private String displayEmployee(Employee employee) {
        if (employee == null) {
            return "";
        }
        return employee.getId() + " - " + employee.getName();
    }

    // =====================================================================
    // ĐƠN HÀNG
    // =====================================================================

    private void orderModule() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- ĐƠN HÀNG ---");
            System.out.println("1. Tạo đơn hàng");
            System.out.println("2. Thêm món vào đơn");
            System.out.println("3. Sửa số lượng món");
            System.out.println("4. Xóa món khỏi đơn");
            System.out.println("5. Xác nhận đơn (xuất kho & hoàn tất)");
            System.out.println("6. Hủy đơn");
            System.out.println("7. Xem đơn theo mã");
            System.out.println("8. Danh sách đơn hàng");
            System.out.println("0. Quay lại");
            int choice = ConsoleInput.readIntInRange("Chọn: ", 0, 8);
            switch (choice) {
                case 1:
                    createOrder();
                    break;
                case 2:
                    addOrderItem();
                    break;
                case 3:
                    changeOrderItemQuantity();
                    break;
                case 4:
                    removeOrderItem();
                    break;
                case 5:
                    confirmOrder();
                    break;
                case 6:
                    cancelOrder();
                    break;
                case 7:
                    viewOrder();
                    break;
                case 8:
                    listOrders();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void createOrder() {
        if (orderService == null) {
            System.out.println("OrderService chưa được khởi tạo.");
            return;
        }
        Employee employee = requireEmployee();
        if (employee == null) {
            return;
        }
        listCustomersBrief();
        String customerId = ConsoleInput.readLine("Mã khách hàng (bỏ trống nếu khách lẻ): ");
        Result<Order> result = orderService.createOrder(customerId, employee);
        printResult(result, "Đã tạo đơn hàng.");
        if (result.isSuccess() && result.getData() != null) {
            System.out.println("Mã đơn: " + result.getData().getId());
        }
    }

    private void addOrderItem() {
        if (orderService == null) {
            return;
        }
        listMenuItemsBrief();
        String orderId = ConsoleInput.readRequiredText("Mã đơn hàng: ");
        String itemId = ConsoleInput.readRequiredText("Mã món: ");
        int qty = ConsoleInput.readPositiveInt("Số lượng: ");
        Result<Order> result = orderService.addItem(orderId, itemId, qty);
        printResult(result, "Đã cập nhật đơn hàng.");
    }

    private void changeOrderItemQuantity() {
        if (orderService == null) {
            return;
        }
        String orderId = ConsoleInput.readRequiredText("Mã đơn hàng: ");
        String itemId = ConsoleInput.readRequiredText("Mã món: ");
        int qty = ConsoleInput.readPositiveInt("Số lượng mới: ");
        Result<Order> result = orderService.changeItemQuantity(orderId, itemId, qty);
        printResult(result, "Đã cập nhật số lượng món.");
    }

    private void removeOrderItem() {
        if (orderService == null) {
            return;
        }
        String orderId = ConsoleInput.readRequiredText("Mã đơn hàng: ");
        String itemId = ConsoleInput.readRequiredText("Mã món cần xóa: ");
        Result<Order> result = orderService.removeItem(orderId, itemId);
        printResult(result, "Đã xóa món khỏi đơn hàng.");
    }

    private void confirmOrder() {
        if (orderService == null) {
            return;
        }
        String orderId = ConsoleInput.readRequiredText("Mã đơn hàng: ");
        Result<Order> result = orderService.confirmOrder(orderId);
        printResult(result, "Đã xác nhận đơn hàng.");
        if (result.isSuccess() && result.getData() != null) {
            Order order = result.getData();
            System.out.printf("Tạm tính: %.2f | Thanh toán: %.2f%n", order.getTotal(), order.getFinalTotal());
        }
    }

    private void cancelOrder() {
        if (orderService == null) {
            return;
        }
        String orderId = ConsoleInput.readRequiredText("Mã đơn hàng: ");
        Result<Order> result = orderService.cancelOrder(orderId);
        printResult(result, "Đã hủy đơn hàng.");
    }

    private void viewOrder() {
        if (orderService == null) {
            return;
        }
        String orderId = ConsoleInput.readRequiredText("Mã đơn hàng: ");
        Result<Order> result = orderService.getById(orderId);
        if (!result.isSuccess() || result.getData() == null) {
            System.out.println(result.getMessage() != null ? result.getMessage() : "Không tìm thấy đơn hàng.");
            return;
        }
        printOrder(result.getData());
    }

    private void listOrders() {
        if (orderService == null) {
            return;
        }
        Object[] all = orderService.getAll();
        if (all == null || all.length == 0) {
            System.out.println("Chưa có đơn hàng.");
            return;
        }
        for (Object object : all) {
            Order order = (Order) object;
            if (order == null) {
                continue;
            }
            System.out.printf("- %s | %s | %s | %s | %.2f%n",
                    order.getId(), order.getTime(), order.getStatus(),
                    displayCustomer(order.getPlacedBy()), order.getFinalTotal());
        }
    }

    private void printOrder(Order order) {
        System.out.println("Mã: " + order.getId() + " | Trạng thái: " + order.getStatus());
        System.out.println("Thời gian: " + order.getTime()
                + " | Nhân viên: " + displayEmployee(order.getHandledBy())
                + " | Khách hàng: " + displayCustomer(order.getPlacedBy()));
        OrderItem[] items = order.getItems();
        if (items == null || items.length == 0) {
            System.out.println("(Chưa có món)");
        } else {
            for (OrderItem item : items) {
                if (item == null || item.getItem() == null) {
                    continue;
                }
                System.out.printf("- %s x%d = %.2f%n", item.getItem().getName(),
                        item.getQuantity(), item.getSubTotal());
            }
        }
        double discount = order.getTotal() - order.getFinalTotal();
        System.out.printf("Tạm tính: %.2f%n", order.getTotal());
        if (discount > 0.001) {
            System.out.printf("Giảm giá thành viên: %.2f%n", discount);
        }
        System.out.printf("Thanh toán: %.2f%n", order.getFinalTotal());
    }

    private void listMenuItemsBrief() {
        MenuItem[] items = getMenuItemsSafe();
        if (items.length == 0) {
            return;
        }
        System.out.println("Món hiện có:");
        for (MenuItem item : items) {
            if (item != null && item.isActive()) {
                System.out.println("  " + item.getId() + " - " + item.getName()
                        + " (" + formatMoney(item.getPrice()) + ")");
            }
        }
    }

    private String displayCustomer(Customer customer) {
        return customer == null ? "Khách lẻ" : customer.getId() + " - " + customer.getName();
    }

    // =====================================================================
    // NHẬP HÀNG
    // =====================================================================

    private void importModule() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- NHẬP HÀNG ---");
            System.out.println("1. Tạo phiếu nhập");
            System.out.println("2. Thêm món vào phiếu nhập");
            System.out.println("3. Nhận hàng (cộng kho & hoàn tất)");
            System.out.println("4. Xem phiếu nhập theo mã");
            System.out.println("5. Danh sách phiếu nhập");
            System.out.println("0. Quay lại");
            int choice = ConsoleInput.readIntInRange("Chọn: ", 0, 5);
            switch (choice) {
                case 1:
                    createImport();
                    break;
                case 2:
                    addImportItem();
                    break;
                case 3:
                    receiveImport();
                    break;
                case 4:
                    viewImport();
                    break;
                case 5:
                    listImports();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void createImport() {
        if (importService == null) {
            return;
        }
        Employee employee = requireEmployee();
        if (employee == null) {
            return;
        }
        listSuppliersBrief();
        String supplierId = ConsoleInput.readRequiredText("Mã nhà cung cấp: ");
        Result<ImportOrder> result = importService.createImport(supplierId, employee);
        printResult(result, "Đã tạo phiếu nhập.");
        if (result.isSuccess() && result.getData() != null) {
            System.out.println("Mã phiếu: " + result.getData().getId());
        }
    }

    private void addImportItem() {
        if (importService == null) {
            return;
        }
        listMenuItemsBrief();
        String importId = ConsoleInput.readRequiredText("Mã phiếu nhập: ");
        String itemId = ConsoleInput.readRequiredText("Mã món: ");
        int qty = ConsoleInput.readPositiveInt("Số lượng: ");
        double cost = ConsoleInput.readPositiveDouble("Giá nhập/đơn vị: ");
        Result<ImportOrder> result = importService.addItem(importId, itemId, qty, cost);
        printResult(result, "Đã cập nhật phiếu nhập.");
    }

    private void receiveImport() {
        if (importService == null) {
            return;
        }
        String importId = ConsoleInput.readRequiredText("Mã phiếu nhập: ");
        Result<ImportOrder> result = importService.receiveImport(importId);
        printResult(result, "Đã nhận hàng.");
    }

    private void viewImport() {
        if (importService == null) {
            return;
        }
        String importId = ConsoleInput.readRequiredText("Mã phiếu nhập: ");
        Result<ImportOrder> result = importService.getById(importId);
        if (!result.isSuccess() || result.getData() == null) {
            System.out.println(result.getMessage() != null ? result.getMessage() : "Không tìm thấy phiếu nhập.");
            return;
        }
        printImport(result.getData());
    }

    private void listImports() {
        if (importService == null) {
            return;
        }
        Object[] all = importService.getAll();
        if (all == null || all.length == 0) {
            System.out.println("Chưa có phiếu nhập.");
            return;
        }
        for (Object object : all) {
            ImportOrder order = (ImportOrder) object;
            if (order == null) {
                continue;
            }
            System.out.printf("- %s | %s | %s | %s | %.2f%n",
                    order.getId(), order.getTime(), order.getStatus(),
                    displaySupplier(order.getSuppliedBy()), order.getTotal());
        }
    }

    private void printImport(ImportOrder order) {
        System.out.println("Mã: " + order.getId() + " | Trạng thái: " + order.getStatus());
        System.out.println("Thời gian: " + order.getTime()
                + " | Nhân viên: " + displayEmployee(order.getHandledBy())
                + " | Nhà cung cấp: " + displaySupplier(order.getSuppliedBy()));
        ImportItem[] items = order.getItems();
        if (items == null || items.length == 0) {
            System.out.println("(Chưa có món)");
        } else {
            for (ImportItem item : items) {
                if (item == null || item.getItem() == null) {
                    continue;
                }
                System.out.printf("- %s x%d @ %.2f = %.2f%n", item.getItem().getName(),
                        item.getQuantity(), item.getUnitAmount(), item.getSubTotal());
            }
        }
        System.out.printf("Tổng giá trị nhập: %.2f%n", order.getTotal());
    }

    private String displaySupplier(Supplier supplier) {
        return supplier == null ? "(không có)" : supplier.getId() + " - " + supplier.getName();
    }

    // =====================================================================
    // TỒN KHO
    // =====================================================================

    private void inventoryModule() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- TỒN KHO ---");
            System.out.println("1. Xem tồn kho theo mã món");
            System.out.println("2. Danh sách tồn kho");
            System.out.println("3. Điều chỉnh tồn kho");
            System.out.println("4. Món sắp hết (theo ngưỡng)");
            System.out.println("5. Lịch sử kho theo mã món");
            System.out.println("0. Quay lại");
            int choice = ConsoleInput.readIntInRange("Chọn: ", 0, 5);
            switch (choice) {
                case 1:
                    viewStock();
                    break;
                case 2:
                    listStock();
                    break;
                case 3:
                    adjustStock();
                    break;
                case 4:
                    lowStock();
                    break;
                case 5:
                    stockHistory();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void viewStock() {
        if (inventoryService == null) {
            return;
        }
        String itemId = ConsoleInput.readRequiredText("Mã món: ");
        int qty = inventoryService.getQuantity(itemId);
        String name = resolveMenuItemName(itemId);
        System.out.println("Tồn kho " + itemId + (name != null ? " (" + name + ")" : "") + ": " + qty);
    }

    private void listStock() {
        if (inventoryService == null) {
            return;
        }
        MenuItem[] items = getMenuItemsSafe();
        if (items.length == 0) {
            System.out.println("Chưa có món trong thực đơn.");
            return;
        }
        for (MenuItem item : items) {
            if (item == null) {
                continue;
            }
            System.out.printf("- %s | %s | tồn: %d%s%n", item.getId(), item.getName(),
                    inventoryService.getQuantity(item.getId()), item.isActive() ? "" : " | ngừng bán");
        }
    }

    private void adjustStock() {
        if (inventoryService == null || menuService == null) {
            return;
        }
        Employee employee = requireEmployee();
        if (employee == null) {
            return;
        }
        String itemId = ConsoleInput.readRequiredText("Mã món: ");
        MenuItem item = findMenuItem(itemId);
        if (item == null) {
            System.out.println("Không tìm thấy món.");
            return;
        }
        int newQty = readNonNegativeInt("Số lượng tồn mới: ");
        Result<?> result = inventoryService.adjust(item, newQty, employee);
        printResult(result, "Đã điều chỉnh tồn kho.");
    }

    private void lowStock() {
        if (reportService == null) {
            return;
        }
        int threshold = readNonNegativeInt("Ngưỡng tồn kho: ");
        MenuItem[] items = reportService.lowStockItems(threshold);
        if (items == null || items.length == 0) {
            System.out.println("Không có món nào dưới/ngang ngưỡng.");
            return;
        }
        System.out.println("Món tồn kho thấp:");
        for (MenuItem item : items) {
            if (item == null) {
                continue;
            }
            System.out.printf("- %s | %s | tồn: %d%n", item.getId(), item.getName(),
                    inventoryService.getQuantity(item.getId()));
        }
    }

    private void stockHistory() {
        if (inventoryService == null) {
            return;
        }
        String itemId = ConsoleInput.readRequiredText("Mã món: ");
        StockLog[] logs = inventoryService.getHistory(itemId);
        if (logs == null || logs.length == 0) {
            System.out.println("Chưa có lịch sử kho cho món " + itemId + ".");
            return;
        }
        System.out.println("Lịch sử kho của " + itemId + " (mới nhất trước):");
        for (int i = logs.length - 1; i >= 0; i--) {
            StockLog log = logs[i];
            if (log == null) {
                continue;
            }
            System.out.printf("- %s | %s | %d | %d -> %d | %s%n",
                    log.getLogTime(), log.getChangeType(), log.getQuantity(),
                    log.getQuantityBefore(), log.getQuantityAfter(),
                    log.getRefId() == null || log.getRefId().isEmpty() ? "(thủ công)" : log.getRefId());
        }
    }

    private MenuItem findMenuItem(String id) {
        if (menuService == null || id == null) {
            return null;
        }
        Result<MenuItem> result = menuService.getById(id.trim());
        return result.isSuccess() ? result.getData() : null;
    }

    private String resolveMenuItemName(String id) {
        MenuItem item = findMenuItem(id);
        return item != null ? item.getName() : null;
    }

    // =====================================================================
    // BÁO CÁO
    // =====================================================================

    private void reportModule() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- BÁO CÁO ---");
            System.out.println("1. Doanh thu theo khoảng ngày (yyyy-MM-dd)");
            System.out.println("2. Doanh thu hôm nay");
            System.out.println("0. Quay lại");
            int choice = ConsoleInput.readIntInRange("Chọn: ", 0, 2);
            switch (choice) {
                case 1:
                    revenueBetween();
                    break;
                case 2:
                    todayRevenue();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void revenueBetween() {
        if (reportService == null) {
            return;
        }
        try {
            LocalDate from = LocalDate.parse(ConsoleInput.readRequiredText("Từ ngày: "));
            LocalDate to = LocalDate.parse(ConsoleInput.readRequiredText("Đến ngày: "));
            if (to.isBefore(from)) {
                System.out.println("Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.");
                return;
            }
            double revenue = reportService.revenueBetween(from, to);
            System.out.printf("Doanh thu từ %s đến %s: %.2f%n", from, to, revenue);
        } catch (Exception e) {
            System.out.println("Định dạng ngày không hợp lệ.");
        }
    }

    private void todayRevenue() {
        if (reportService == null) {
            return;
        }
        LocalDate today = LocalDate.now();
        System.out.printf("Doanh thu hôm nay (%s): %.2f%n", today, reportService.revenueBetween(today, today));
    }

    // =====================================================================
    // TIỆN ÍCH NHẬP LIỆU
    // =====================================================================

    private int readNonNegativeInt(String prompt) {
        while (true) {
            int value = ConsoleInput.readInt(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Giá trị không được âm.");
        }
    }

    private double readNonNegativeDouble(String prompt) {
        while (true) {
            double value = ConsoleInput.readDouble(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Giá trị không được âm.");
        }
    }

    private String readOptionalPhone(String prompt) {
        while (true) {
            String value = ConsoleInput.readLine(prompt);
            if (value.isEmpty() || Validator.isValidPhone(value)) {
                return value;
            }
            System.out.println("Số điện thoại không hợp lệ (bắt đầu bằng 0, 10-11 chữ số).");
        }
    }

    private String readOptionalEmail(String prompt) {
        while (true) {
            String value = ConsoleInput.readLine(prompt);
            if (value.isEmpty() || Validator.isValidEmail(value)) {
                return value;
            }
            System.out.println("Email không hợp lệ.");
        }
    }

    private String promptKeepPhone(String label, String current) {
        String shown = current == null ? "" : current;
        while (true) {
            String value = ConsoleInput.readLine(label + " [" + shown + "] (bỏ trống để giữ nguyên): ");
            if (value.isEmpty()) {
                return current;
            }
            if (Validator.isValidPhone(value)) {
                return value;
            }
            System.out.println("Số điện thoại không hợp lệ (bắt đầu bằng 0, 10-11 chữ số).");
        }
    }

    private String promptKeepEmail(String label, String current) {
        String shown = current == null ? "" : current;
        while (true) {
            String value = ConsoleInput.readLine(label + " [" + shown + "] (bỏ trống để giữ nguyên): ");
            if (value.isEmpty()) {
                return current;
            }
            if (Validator.isValidEmail(value)) {
                return value;
            }
            System.out.println("Email không hợp lệ.");
        }
    }

    private String promptKeepText(String label, String current) {
        String shown = current == null ? "" : current;
        String value = ConsoleInput.readLine(label + " [" + shown + "] (bỏ trống để giữ nguyên): ");
        return value.isEmpty() ? current : value;
    }

    private double promptKeepPositiveDouble(String label, double current) {
        while (true) {
            String value = ConsoleInput.readLine(
                    label + " [" + formatNumber(current) + "] (bỏ trống để giữ nguyên): ");
            if (value.isEmpty()) {
                return current;
            }
            try {
                double parsed = Double.parseDouble(value);
                if (parsed > 0) {
                    return parsed;
                }
            } catch (NumberFormatException e) {
                // In thông báo bên dưới.
            }
            System.out.println("Giá trị phải là số lớn hơn 0.");
        }
    }

    private double promptKeepNonNegativeDouble(String label, double current) {
        while (true) {
            String value = ConsoleInput.readLine(
                    label + " [" + formatNumber(current) + "] (bỏ trống để giữ nguyên): ");
            if (value.isEmpty()) {
                return current;
            }
            try {
                double parsed = Double.parseDouble(value);
                if (parsed >= 0) {
                    return parsed;
                }
            } catch (NumberFormatException e) {
                // In thông báo bên dưới.
            }
            System.out.println("Giá trị phải là số không âm.");
        }
    }

    private <E extends Enum<E>> E chooseEnum(String label, Class<E> enumType) {
        E[] values = enumType.getEnumConstants();
        System.out.println(label + ":");
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ". " + values[i]);
        }
        int index = ConsoleInput.readIntInRange("Chọn " + label + ": ", 1, values.length);
        return values[index - 1];
    }

    private <E extends Enum<E>> E promptKeepEnum(String label, Class<E> enumType, E current) {
        E[] values = enumType.getEnumConstants();
        System.out.println(label + " hiện tại: " + current);
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ". " + values[i]);
        }
        String raw = ConsoleInput.readLine("Chọn số thứ tự mới (bỏ trống để giữ nguyên): ");
        if (raw.isEmpty()) {
            return current;
        }
        try {
            int index = Integer.parseInt(raw);
            if (index >= 1 && index <= values.length) {
                return values[index - 1];
            }
        } catch (NumberFormatException e) {
            // Giữ nguyên giá trị cũ.
        }
        System.out.println("Lựa chọn không hợp lệ, giữ nguyên giá trị cũ.");
        return current;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String formatMoney(double value) {
        return String.format("%.2f", value);
    }

    private String formatNumber(double value) {
        return String.format("%.2f", value);
    }

    private void printResult(Result<?> result, String successMessage) {
        if (result == null) {
            System.out.println("Không có kết quả.");
            return;
        }
        if (result.isSuccess()) {
            System.out.println(successMessage != null && !successMessage.isEmpty()
                    ? successMessage : "Thành công.");
        } else {
            System.out.println(result.getMessage() != null ? result.getMessage() : "Thao tác thất bại.");
        }
    }
}
