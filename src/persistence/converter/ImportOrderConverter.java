package persistence.converter;

import java.time.LocalDateTime;
import model.employee.Employee;
import model.menu.Drink;
import model.menu.DrinkSize;
import model.menu.MenuItem;
import model.supplier.Supplier;
import model.transaction.ImportItem;
import model.transaction.ImportOrder;
import model.transaction.TransactionStatus;
import persistence.Repository;
import persistence.converter.LineItemsCodec.Entry;
import persistence.converter.base.BaseTextConverter;

public class ImportOrderConverter extends BaseTextConverter<ImportOrder> {
    private final Repository<MenuItem> menuRepository;
    private final Repository<Supplier> supplierRepository;
    private final Repository<Employee> employeeRepository;

    public ImportOrderConverter() {
        this(null, null, null);
    }

    public ImportOrderConverter(Repository<MenuItem> menuRepository,
                                Repository<Supplier> supplierRepository,
                                Repository<Employee> employeeRepository) {
        this.menuRepository = menuRepository;
        this.supplierRepository = supplierRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public String toLine(ImportOrder entity) {
        if (entity == null) {
            return "";
        }
        String supplierId = entity.getSuppliedBy() != null ? entity.getSuppliedBy().getId() : "";
        String employeeId = entity.getHandledBy() != null ? entity.getHandledBy().getId() : "";
        return join(entity.getId(), entity.getTime(), entity.getStatus(), employeeId, supplierId,
                entity.isActive(), LineItemsCodec.encode(entity.getItems()));
    }

    @Override
    public ImportOrder fromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = split(line);
        // Định dạng cũ: id | time | status | employeeId | supplierId | active
        // Định dạng mới: thêm trường thứ 7 chứa danh sách dòng hàng.
        if (parts.length < 6) {
            return null;
        }
        try {
            ImportOrder order = new ImportOrder();
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
            if (supplierRepository != null && !parts[4].isEmpty()) {
                order.setSuppliedBy(supplierRepository.findById(parts[4].trim()));
            }

            if (parts.length >= 7) {
                restoreItems(order, LineItemsCodec.decode(parts[6]));
            }
            return order;
        } catch (Exception e) {
            return null;
        }
    }

    private void restoreItems(ImportOrder order, Entry[] entries) {
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
            order.addItem(new ImportItem(menuItem, entry.getQuantity(), entry.getUnitAmount()));
        }
        order.setStatus(original);
    }
}
