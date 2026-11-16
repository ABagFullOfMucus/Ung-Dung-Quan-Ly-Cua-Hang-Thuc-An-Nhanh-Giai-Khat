package persistence.converter;

import java.time.LocalDateTime;
import model.customer.Customer;
import model.customer.MemberDiscount;
import model.employee.Employee;
import model.menu.Drink;
import model.menu.DrinkSize;
import model.menu.MenuItem;
import model.transaction.Order;
import model.transaction.OrderItem;
import model.transaction.TransactionStatus;
import persistence.Repository;
import persistence.converter.LineItemsCodec.Entry;
import persistence.converter.base.BaseTextConverter;

public class OrderConverter extends BaseTextConverter<Order> {
    private final Repository<MenuItem> menuRepository;
    private final Repository<Customer> customerRepository;
    private final Repository<Employee> employeeRepository;

    public OrderConverter() {
        this(null, null, null);
    }

    public OrderConverter(Repository<MenuItem> menuRepository,
                          Repository<Customer> customerRepository,
                          Repository<Employee> employeeRepository) {
        this.menuRepository = menuRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public String toLine(Order entity) {
        if (entity == null) {
            return "";
        }
        String customerId = entity.getPlacedBy() != null ? entity.getPlacedBy().getId() : "";
        String employeeId = entity.getHandledBy() != null ? entity.getHandledBy().getId() : "";
        return join(entity.getId(), entity.getTime(), entity.getStatus(), employeeId, customerId,
                entity.isActive(), LineItemsCodec.encode(entity.getItems()));
    }

    @Override
    public Order fromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = split(line);
        // Định dạng cũ: id | time | status | employeeId | customerId | active
        // Định dạng mới: thêm trường thứ 7 chứa danh sách dòng hàng.
        if (parts.length < 6) {
            return null;
        }
        try {
            Order order = new Order();
            order.setId(parts[0]);
            order.setTime(LocalDateTime.parse(parts[1]));
            order.setStatus(TransactionStatus.valueOf(parts[2].trim().toUpperCase()));

            boolean active = "true".equalsIgnoreCase(parts[5].trim());
            if (active) {
                order.activate();
            } else {
                order.deactivate();
            }

            if (employeeRepository != null && !parts[3].isEmpty()) {
                order.setHandledBy(employeeRepository.findById(parts[3].trim()));
            }
            if (customerRepository != null && !parts[4].isEmpty()) {
                order.setPlacedBy(customerRepository.findById(parts[4].trim()));
            }
            order.setDiscountPolicy(new MemberDiscount());

            if (parts.length >= 7) {
                restoreItems(order, LineItemsCodec.decode(parts[6]));
            }
            return order;
        } catch (Exception e) {
            return null;
        }
    }

    private void restoreItems(Order order, Entry[] entries) {
        if (entries.length == 0) {
            return;
        }
        TransactionStatus original = order.getStatus();
        order.setStatus(TransactionStatus.DRAFT);
        for (Entry entry : entries) {
            MenuItem menuItem = menuRepository != null ? menuRepository.findById(entry.getId()) : null;
            if (menuItem == null) {
                menuItem = new Drink(entry.getId(), entry.getName(), entry.getUnitAmount(), "", DrinkSize.M);
            }
            order.addItem(new OrderItem(menuItem, entry.getQuantity(), entry.getUnitAmount()));
        }
        order.setStatus(original);
    }
}
