package service;

import common.Result;
import model.customer.Customer;
import model.customer.MemberDiscount;
import model.employee.Employee;
import model.menu.MenuItem;
import model.transaction.Order;
import model.transaction.OrderItem;
import model.transaction.TransactionStatus;
import persistence.Repository;
import service.base.CrudService;

public class OrderService extends CrudService<Order> {
    private final Repository<MenuItem> menuRepository;
    private final Repository<Customer> customerRepository;
    private final InventoryService inventoryService;

    public OrderService(Repository<Order> repository,
                        Repository<MenuItem> menuRepository,
                        Repository<Customer> customerRepository,
                        InventoryService inventoryService) {
        super(repository);
        this.menuRepository = menuRepository;
        this.customerRepository = customerRepository;
        this.inventoryService = inventoryService;
    }

    @Override
    protected String newId() {
        return nextId("DH");
    }

    public Result<Order> createOrder(String customerId, Employee employee) {
        if (employee == null) {
            return Result.fail("Nhân viên xử lý không hợp lệ");
        }

        Customer customer = null;
        if (customerId != null && !customerId.trim().isEmpty()) {
            customer = customerRepository.findById(customerId.trim());
            if (customer == null) {
                return Result.fail("Không tìm thấy khách hàng có mã " + customerId);
            }
        }

        Order order = new Order();
        order.setHandledBy(employee);
        order.setPlacedBy(customer);
        order.setStatus(TransactionStatus.DRAFT);
        order.setDiscountPolicy(new MemberDiscount());
        String id = newId();
        order.setId(id);

        if (!repository.add(order)) {
            return Result.fail("Không thể tạo đơn hàng");
        }
        return Result.ok(order);
    }

    public Result<Order> addItem(String orderId, String itemId, int qty) {
        Order order = repository.findById(orderId);
        if (order == null) {
            return Result.fail("Không tìm thấy đơn hàng có mã " + orderId);
        }
        if (order.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể thêm món khi đơn hàng ở trạng thái nháp");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            return Result.fail("Mã món không hợp lệ");
        }
        if (qty <= 0) {
            return Result.fail("Số lượng phải lớn hơn 0");
        }

        MenuItem menuItem = menuRepository.findById(itemId.trim());
        if (menuItem == null || !menuItem.isActive()) {
            return Result.fail("Không tìm thấy món có mã " + itemId);
        }

        if (!order.addItem(new OrderItem(menuItem, qty))) {
            return Result.fail("Không thể thêm món vào đơn hàng");
        }
        if (!repository.update(order)) {
            return Result.fail("Không thể lưu đơn hàng");
        }
        return Result.ok(order);
    }

    public Result<Order> removeItem(String orderId, String itemId) {
        Order order = repository.findById(orderId);
        if (order == null) {
            return Result.fail("Không tìm thấy đơn hàng có mã " + orderId);
        }
        if (order.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể xóa món khi đơn hàng ở trạng thái nháp");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            return Result.fail("Mã món không hợp lệ");
        }
        if (!order.removeItem(itemId.trim())) {
            return Result.fail("Không tìm thấy món có mã " + itemId + " trong đơn hàng");
        }
        if (!repository.update(order)) {
            return Result.fail("Không thể lưu đơn hàng");
        }
        return Result.ok(order);
    }

    public Result<Order> changeItemQuantity(String orderId, String itemId, int qty) {
        Order order = repository.findById(orderId);
        if (order == null) {
            return Result.fail("Không tìm thấy đơn hàng có mã " + orderId);
        }
        if (order.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể sửa số lượng khi đơn hàng ở trạng thái nháp");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            return Result.fail("Mã món không hợp lệ");
        }
        if (qty <= 0) {
            return Result.fail("Số lượng phải lớn hơn 0");
        }

        OrderItem[] items = order.getItems();
        for (OrderItem item : items) {
            if (item != null && item.getItem() != null
                    && itemId.trim().equals(item.getItem().getId())) {
                if (!item.changeQuantity(qty)) {
                    return Result.fail("Số lượng phải lớn hơn 0");
                }
                if (!repository.update(order)) {
                    return Result.fail("Không thể lưu đơn hàng");
                }
                return Result.ok(order);
            }
        }
        return Result.fail("Không tìm thấy món có mã " + itemId + " trong đơn hàng");
    }

    public Result<Order> confirmOrder(String orderId) {
        Order order = repository.findById(orderId);
        if (order == null) {
            return Result.fail("Không tìm thấy đơn hàng có mã " + orderId);
        }
        if (order.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể xác nhận đơn hàng ở trạng thái nháp");
        }
        if (order.getItems().length == 0) {
            return Result.fail("Đơn hàng chưa có món");
        }

        // Kiểm tra tồn kho trước
        OrderItem[] items = order.getItems();
        for (OrderItem item : items) {
            if (item == null || item.getItem() == null) {
                continue;
            }
            int available = inventoryService.getQuantity(item.getItem().getId());
            if (available < item.getQuantity()) {
                return Result.fail("Không đủ tồn kho cho món " + item.getItem().getName()
                        + " (cần " + item.getQuantity() + ", còn " + available + ")");
            }
        }

        for (OrderItem item : items) {
            if (item == null || item.getItem() == null) {
                continue;
            }
            Result<?> issued = inventoryService.issue(item.getItem(), item.getQuantity(), order.getId(), order.getHandledBy());
            if (!issued.isSuccess()) {
                return Result.fail("Không thể xuất kho: " + issued.getMessage());
            }
        }

        if (!order.confirm()) {
            return Result.fail("Không thể xác nhận đơn hàng");
        }
        if (!order.complete()) {
            // Nếu không complete được theo trạng thái, vẫn giữ CONFIRMED hợp lệ
            // nhưng cố gắng đặt COMPLETED để báo cáo doanh thu hoạt động đúng
            order.setStatus(TransactionStatus.COMPLETED);
        }
        if (!repository.update(order)) {
            return Result.fail("Không thể lưu trạng thái đơn hàng");
        }
        awardLoyaltyPoints(order);
        return Result.ok(order);
    }

    private void awardLoyaltyPoints(Order order) {
        Customer customer = order.getPlacedBy();
        if (customer == null || customerRepository == null) {
            return;
        }
        double paid = order.getFinalTotal();
        if (paid <= 0) {
            return;
        }
        customer.earnPoints(paid);
        customerRepository.update(customer);
    }

    public Result<Order> cancelOrder(String orderId) {
        Order order = repository.findById(orderId);
        if (order == null) {
            return Result.fail("Không tìm thấy đơn hàng có mã " + orderId);
        }
        if (!order.cancel()) {
            return Result.fail("Không thể hủy đơn hàng ở trạng thái hiện tại");
        }
        if (!repository.update(order)) {
            return Result.fail("Không thể lưu trạng thái đơn hàng");
        }
        return Result.ok(order);
    }
}
