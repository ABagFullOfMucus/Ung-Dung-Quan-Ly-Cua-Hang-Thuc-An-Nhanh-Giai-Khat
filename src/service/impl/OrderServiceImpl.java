package service.impl;

import common.Result;
import model.customer.Customer;
import model.customer.MemberDiscount;
import model.customer.NoDiscount;
import model.employee.Employee;
import model.employee.Permission;
import model.inventory.StockEntry;
import model.menu.MenuItem;
import model.transaction.Order;
import model.transaction.OrderItem;
import model.transaction.TransactionStatus;
import persistence.Repository;
import service.base.TransactionService;
import service.interfaces.ICustomerService;
import service.interfaces.IInventoryService;
import service.interfaces.IMenuService;
import service.interfaces.IOrderService;
import utils.Validator;

public class OrderServiceImpl extends TransactionService<Order, OrderItem> implements IOrderService {
    private final ICustomerService customerService;
    private final IMenuService menuService;
    private final IInventoryService inventoryService;

    public OrderServiceImpl(Repository<Order> orderRepository,
            ICustomerService customerService,
            IMenuService menuService,
            IInventoryService inventoryService) {
        super(orderRepository);
        if (customerService == null) {
            throw new IllegalArgumentException("ICustomerService không được null");
        }
        if (menuService == null) {
            throw new IllegalArgumentException("IMenuService không được null");
        }
        if (inventoryService == null) {
            throw new IllegalArgumentException("IInventoryService không được null");
        }
        this.customerService = customerService;
        this.menuService = menuService;
        this.inventoryService = inventoryService;
    }

    @Override
    protected String idPrefix() {
        return "DH";
    }

    @Override
    protected Order[] newArray(int size) {
        return new Order[size];
    }

    @Override
    public Result<Order> createOrder(String customerId, Employee employee) {
        if (!canCreateOrder(employee)) {
            return Result.fail("Nhân viên không có quyền tạo đơn hàng");
        }

        Customer customer = null;
        if (Validator.isNotBlank(customerId)) {
            Result<Customer> customerResult = customerService.getById(customerId.trim());
            if (!customerResult.isSuccess()) {
                return Result.fail(customerResult.getMessage());
            }
            customer = customerResult.getData();
            if (!customer.isActive()) {
                return Result.fail("Khách hàng đã bị vô hiệu hóa");
            }
        }

        Order order = new Order(nextId(), employee, customer);
        if (customer != null) {
            order.setDiscountPolicy(new MemberDiscount());
        } else {
            order.setDiscountPolicy(new NoDiscount());
        }
        return persistNew(order);
    }

    @Override
    public Result<Order> addItem(String orderId, String itemId, int qty) {
        if (qty <= 0) {
            return Result.fail("Số lượng món phải lớn hơn 0");
        }
        if (!Validator.isNotBlank(itemId)) {
            return Result.fail("Mã món không hợp lệ");
        }

        Result<MenuItem> itemResult = menuService.getById(itemId.trim());
        if (!itemResult.isSuccess()) {
            return Result.fail(itemResult.getMessage());
        }
        MenuItem menuItem = itemResult.getData();
        if (!menuItem.isActive()) {
            return Result.fail("Món đã ngừng kinh doanh, không thể thêm vào đơn");
        }
        return addLine(orderId, new OrderItem(menuItem, qty));
    }

    @Override
    public Result<Order> removeItem(String orderId, String itemId) {
        return removeLine(orderId, itemId);
    }

    @Override
    public Result<Order> confirmOrder(String orderId) {
        Result<Order> found = findTransaction(orderId);
        if (!found.isSuccess()) {
            return found;
        }
        Order order = found.getData();
        if (order.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể xác nhận đơn hàng đang ở trạng thái nháp (DRAFT)");
        }
        if (!canCreateOrder(order.getHandledBy())) {
            return Result.fail("Nhân viên phụ trách đơn không còn quyền xác nhận đơn hàng");
        }

        OrderItem[] items = order.getItems();
        if (items == null || items.length == 0) {
            return Result.fail("Không thể xác nhận đơn hàng rỗng");
        }

        for (int i = 0; i < items.length; i++) {
            OrderItem line = items[i];
            if (line == null || line.getItem() == null
                    || !Validator.isNotBlank(line.getItem().getId())
                    || line.getQuantity() <= 0) {
                return Result.fail("Đơn hàng chứa dòng món không hợp lệ");
            }
            int available = inventoryService.getQuantity(line.getItem().getId());
            if (available < line.getQuantity()) {
                return Result.fail("Không đủ tồn kho cho món " + line.getItem().getName()
                        + " (hiện có: " + available + ", cần: " + line.getQuantity() + ")");
            }
        }

        OrderItem[] issuedItems = new OrderItem[items.length];
        int issuedCount = 0;
        for (int i = 0; i < items.length; i++) {
            OrderItem line = items[i];
            Result<StockEntry> issueResult = inventoryService.issue(
                    line.getItem(), line.getQuantity(), order.getId(), order.getHandledBy());
            if (!issueResult.isSuccess()) {
                rollbackIssuedStock(issuedItems, issuedCount, order);
                return Result.fail("Không thể xuất kho: " + issueResult.getMessage());
            }
            issuedItems[issuedCount++] = line;
        }

        if (!order.confirm()) {
            rollbackIssuedStock(issuedItems, issuedCount, order);
            return Result.fail("Không thể chuyển trạng thái đơn sang xác nhận");
        }

        if (!getRepository().update(order)) {
            order.setStatus(TransactionStatus.DRAFT);
            rollbackIssuedStock(issuedItems, issuedCount, order);
            return Result.fail("Không thể lưu trạng thái xác nhận đơn hàng");
        }
        return Result.ok(order);
    }

    @Override
    public Result<Order> completeOrder(String orderId) {
        Result<Order> found = findTransaction(orderId);
        if (!found.isSuccess()) {
            return found;
        }
        Order order = found.getData();
        if (order.getStatus() != TransactionStatus.CONFIRMED) {
            return Result.fail("Chỉ có thể hoàn tất đơn hàng đã xác nhận (CONFIRMED)");
        }

        double finalTotal = order.getFinalTotal();
        if (!order.complete()) {
            return Result.fail("Không thể hoàn tất đơn hàng");
        }

        if (!getRepository().update(order)) {
            order.setStatus(TransactionStatus.CONFIRMED);
            return Result.fail("Không thể lưu trạng thái hoàn tất đơn hàng");
        }

        Customer customer = order.getPlacedBy();
        if (customer != null && Validator.isNotBlank(customer.getId()) && finalTotal > 0) {
            Result<Customer> latestCustomerRes = customerService.getById(customer.getId());
            if (latestCustomerRes.isSuccess() && latestCustomerRes.getData().isActive()) {
                Result<Customer> earnResult = customerService.earnPoints(
                        latestCustomerRes.getData().getId(), finalTotal);
                if (!earnResult.isSuccess()) {
                    order.setStatus(TransactionStatus.CONFIRMED);
                    getRepository().update(order);
                    return Result.fail("Hoàn tất đơn thất bại do không cộng được điểm: " + earnResult.getMessage());
                }
            }
        }
        return Result.ok(order);
    }

    @Override
    public Result<Order> cancelOrder(String orderId) {
        Result<Order> found = findTransaction(orderId);
        if (!found.isSuccess()) {
            return found;
        }
        Order order = found.getData();
        TransactionStatus status = order.getStatus();

        if (status == TransactionStatus.COMPLETED || status == TransactionStatus.CANCELLED) {
            return Result.fail("Không thể hủy đơn hàng ở trạng thái " + status);
        }
        if (status == TransactionStatus.DRAFT) {
            return cancelDraft(orderId);
        }
        if (!canCreateOrder(order.getHandledBy())) {
            return Result.fail("Nhân viên phụ trách đơn không còn quyền hoàn kho khi hủy đơn");
        }

        OrderItem[] items = order.getItems();
        OrderItem[] returnedItems = new OrderItem[items == null ? 0 : items.length];
        int returnedCount = 0;

        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                OrderItem line = items[i];
                if (line == null || line.getItem() == null) {
                    continue;
                }
                Result<StockEntry> returnResult = inventoryService.returnStock(
                        line.getItem(), line.getQuantity(), order.getId(), order.getHandledBy());
                if (!returnResult.isSuccess()) {
                    rollbackReturnedStock(returnedItems, returnedCount, order);
                    return Result.fail("Không thể hoàn kho khi hủy đơn: " + returnResult.getMessage());
                }
                returnedItems[returnedCount++] = line;
            }
        }

        if (!order.cancel()) {
            rollbackReturnedStock(returnedItems, returnedCount, order);
            return Result.fail("Không thể hủy đơn hàng");
        }

        if (!getRepository().update(order)) {
            order.setStatus(TransactionStatus.CONFIRMED);
            rollbackReturnedStock(returnedItems, returnedCount, order);
            return Result.fail("Không thể lưu trạng thái hủy đơn hàng");
        }
        return Result.ok(order);
    }

    private void rollbackIssuedStock(OrderItem[] items, int count, Order order) {
        for (int i = count - 1; i >= 0; i--) {
            OrderItem line = items[i];
            if (line != null && line.getItem() != null) {
                inventoryService.returnStock(
                        line.getItem(), line.getQuantity(), order.getId(), order.getHandledBy());
            }
        }
    }

    private void rollbackReturnedStock(OrderItem[] items, int count, Order order) {
        for (int i = count - 1; i >= 0; i--) {
            OrderItem line = items[i];
            if (line != null && line.getItem() != null) {
                inventoryService.issue(
                        line.getItem(), line.getQuantity(), order.getId(), order.getHandledBy());
            }
        }
    }

    private boolean canCreateOrder(Employee employee) {
        return employee != null
                && employee.isActive()
                && employee.getRole() != null
                && employee.getRole().can(Permission.CREATE_ORDER);
    }
}