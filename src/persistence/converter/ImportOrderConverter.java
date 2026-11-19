package persistence.converter;

import java.time.LocalDateTime;

import model.employee.Employee;
import model.menu.MenuItem;
import model.supplier.Supplier;
import model.transaction.ImportItem;
import model.transaction.ImportOrder;
import model.transaction.TransactionStatus;
import persistence.Repository;
import persistence.converter.base.BaseTextConverter;

public class ImportOrderConverter extends BaseTextConverter<ImportOrder> {
    private static final int HEADER_COUNT = 6;
    private static final int ITEM_FIELD_COUNT = 3;

    private final Repository<MenuItem> menuRepository;
    private final Repository<Employee> employeeRepository;
    private final Repository<Supplier> supplierRepository;

    public ImportOrderConverter(Repository<MenuItem> menuRepository,
            Repository<Employee> employeeRepository,
            Repository<Supplier> supplierRepository) {
        this.menuRepository = menuRepository;
        this.employeeRepository = employeeRepository;
        this.supplierRepository = supplierRepository;
    }

    /**
     * id|time|status|handledById|supplierId|itemCount|menuItemId|quantity|unitAmount
     */
    @Override
    public String toLine(ImportOrder entity) {
        if (entity == null) {
            return "";
        }

        String time = entity.getTime() == null ? "" : entity.getTime().toString();
        String employeeId = entity.getHandledBy() == null ? "" : entity.getHandledBy().getId();
        String supplierId = entity.getSuppliedBy() == null ? "" : entity.getSuppliedBy().getId();

        StringBuilder itemLines = new StringBuilder();
        int itemCount = 0;
        for (ImportItem item : entity.getItems()) {
            if (item == null || item.getItem() == null) {
                continue;
            }
            itemLines.append(SEPARATOR)
                    .append(join(item.getItem().getId(), item.getQuantity(), item.getUnitAmount()));
            itemCount++;
        }

        return join(entity.getId(), time, entity.getStatus(), employeeId, supplierId, itemCount) + itemLines;
    }

    @Override
    public ImportOrder fromLine(String line) {
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
            Employee handledBy = employeeId.isEmpty() || employeeRepository == null
                    ? null
                    : employeeRepository.findById(employeeId);
            if (handledBy == null) {
                return null;
            }

            String supplierId = parts[4].trim();
            Supplier suppliedBy = supplierId.isEmpty() || supplierRepository == null
                    ? null
                    : supplierRepository.findById(supplierId);

            ImportOrder importOrder = new ImportOrder(id, handledBy, suppliedBy);
            importOrder.setTime(time);

            int itemCount = Integer.parseInt(parts[5].trim());
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
                if (!importOrder.addItem(new ImportItem(menuItem, quantity, unitAmount))) {
                    return null;
                }
            }

            importOrder.setStatus(status);
            return importOrder;
        } catch (Exception e) {
            return null;
        }
    }
}
