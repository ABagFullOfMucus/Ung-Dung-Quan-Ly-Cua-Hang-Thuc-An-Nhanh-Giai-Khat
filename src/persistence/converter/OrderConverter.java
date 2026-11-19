package persistence.converter;

import java.time.LocalDateTime;

import model.customer.Customer;
import model.customer.MemberDiscount;
import model.customer.NoDiscount;
import model.employee.Employee;
import model.menu.MenuItem;
import model.transaction.Order;
import model.transaction.OrderItem;
import model.transaction.TransactionStatus;
import persistence.Repository;
import persistence.converter.base.BaseTextConverter;

public class OrderConverter extends BaseTextConverter<Order> {
    private static final String MEMBER = "MEMBER";
    private static final String NONE = "NONE";
    private static final int HEADER_COUNT = 7;
    private static final int ITEM_FIELD_COUNT = 3;

    private final Repository<MenuItem> menuRepository;
    private final Repository<Employee> employeeRepository;
    private final Repository<Customer> customerRepository;

    public OrderConverter(Repository<MenuItem> menuRepository, Repository<Employee> employeeRepository, Repository<Customer> customerRepository) {
        this.menuRepository = menuRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * id|time|status|handledById|placedById|discountType|itemCount|menuItemId|quantity|unitAmount
     */
    @Override
    public String toLine(Order entity) {
        if (entity == null) {
            return "";
        }

        String time = entity.getTime() == null ? "" : entity.getTime().toString();
        String employeeId = entity.getHandledBy() == null ? "" : entity.getHandledBy().getId();
        String customerId = entity.getPlacedBy() == null ? "" : entity.getPlacedBy().getId();
        String discountType = entity.getDiscountPolicy() instanceof MemberDiscount ? MEMBER : NONE;

        StringBuilder itemLines = new StringBuilder();
        int itemCount = 0;
        for (OrderItem item : entity.getItems()) {
            if (item == null || item.getItem() == null) {
                continue;
            }
            itemLines.append(SEPARATOR).append(join(item.getItem().getId(), item.getQuantity(), item.getUnitAmount()));
            itemCount++;
        }

        return join(entity.getId(), time, entity.getStatus(), employeeId, customerId, discountType, itemCount) + itemLines;
    }

    @Override
    public Order fromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] parts = split(line);
        if (parts == null || parts.length < HEADER_COUNT) {
            return null;
        }

        String id = parts[0].trim();
        if (id.isEmpty()) {
            return null;
        }

        try {
            LocalDateTime time = LocalDateTime.parse(parts[1].trim());
            TransactionStatus status = TransactionStatus.valueOf(parts[2].trim().toUpperCase());

            String employeeId = parts[3].trim();
            Employee handledBy = employeeId.isEmpty() || employeeRepository == null ? null : employeeRepository.findById(employeeId);
            if (handledBy == null) {
                return null;
            }

            String customerId = parts[4].trim();
            Customer placedBy = customerId.isEmpty() || customerRepository == null ? null : customerRepository.findById(customerId);

            String discountType = parts[5].trim();
            Order order = new Order(id, handledBy, placedBy);
            order.setTime(time);
            order.setDiscountPolicy(MEMBER.equalsIgnoreCase(discountType) ? new MemberDiscount() : new NoDiscount());

            int itemCount = Integer.parseInt(parts[6].trim());
            if (itemCount < 0 || parts.length != HEADER_COUNT + itemCount * ITEM_FIELD_COUNT) {
                return null;
            }

            for (int i = 0; i < itemCount; i++) {
                int base = HEADER_COUNT + i * ITEM_FIELD_COUNT;
                String menuItemId = parts[base].trim();
                MenuItem menuItem = menuRepository == null ? null : menuRepository.findById(menuItemId);
                if (menuItem == null) {
                    return null;
                }

                int quantity = Integer.parseInt(parts[base + 1].trim());
                double unitAmount = Double.parseDouble(parts[base + 2].trim());
                if (!order.addItem(new OrderItem(menuItem, quantity, unitAmount))) {
                    return null;
                }
            }

            order.setStatus(status);
            return order;
        } catch (Exception e) {
            return null;
        }
    }
}
