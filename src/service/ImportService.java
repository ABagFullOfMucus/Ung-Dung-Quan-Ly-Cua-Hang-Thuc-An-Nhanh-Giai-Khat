package service;

import common.Result;
import model.employee.Employee;
import model.menu.MenuItem;
import model.supplier.Supplier;
import model.transaction.ImportItem;
import model.transaction.ImportOrder;
import model.transaction.TransactionStatus;
import persistence.Repository;
import service.base.CrudService;

public class ImportService extends CrudService<ImportOrder> {
    private final Repository<MenuItem> menuRepository;
    private final Repository<Supplier> supplierRepository;
    private final InventoryService inventoryService;

    public ImportService(Repository<ImportOrder> repository,
                         Repository<MenuItem> menuRepository,
                         Repository<Supplier> supplierRepository,
                         InventoryService inventoryService) {
        super(repository);
        this.menuRepository = menuRepository;
        this.supplierRepository = supplierRepository;
        this.inventoryService = inventoryService;
    }

    @Override
    protected String newId() {
        return nextId("PN");
    }

    public Result<ImportOrder> createImport(String supplierId, Employee employee) {
        if (employee == null) {
            return Result.fail("Nhân viên xử lý không hợp lệ");
        }
        if (supplierId == null || supplierId.trim().isEmpty()) {
            return Result.fail("Vui lòng chọn nhà cung cấp");
        }
        Supplier supplier = supplierRepository.findById(supplierId.trim());
        if (supplier == null) {
            return Result.fail("Không tìm thấy nhà cung cấp có mã " + supplierId);
        }

        ImportOrder order = new ImportOrder();
        order.setHandledBy(employee);
        order.setSuppliedBy(supplier);
        order.setStatus(TransactionStatus.DRAFT);
        order.setId(newId());

        if (!repository.add(order)) {
            return Result.fail("Không thể tạo phiếu nhập");
        }
        return Result.ok(order);
    }

    public Result<ImportOrder> addItem(String importId, String itemId, int qty, double cost) {
        ImportOrder order = repository.findById(importId);
        if (order == null) {
            return Result.fail("Không tìm thấy phiếu nhập có mã " + importId);
        }
        if (order.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể thêm món khi phiếu nhập ở trạng thái nháp");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            return Result.fail("Mã món không hợp lệ");
        }
        if (qty <= 0) {
            return Result.fail("Số lượng phải lớn hơn 0");
        }
        if (cost < 0) {
            return Result.fail("Giá nhập không được âm");
        }

        MenuItem menuItem = menuRepository.findById(itemId.trim());
        if (menuItem == null) {
            return Result.fail("Không tìm thấy món có mã " + itemId);
        }

        if (!order.addItem(new ImportItem(menuItem, qty, cost))) {
            return Result.fail("Không thể thêm món vào phiếu nhập");
        }
        if (!repository.update(order)) {
            return Result.fail("Không thể lưu phiếu nhập");
        }
        return Result.ok(order);
    }

    public Result<ImportOrder> receiveImport(String importId) {
        ImportOrder order = repository.findById(importId);
        if (order == null) {
            return Result.fail("Không tìm thấy phiếu nhập có mã " + importId);
        }
        if (order.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể nhận phiếu nhập ở trạng thái nháp");
        }
        if (order.getItems().length == 0) {
            return Result.fail("Phiếu nhập chưa có món");
        }

        ImportItem[] items = order.getItems();
        for (ImportItem item : items) {
            if (item == null || item.getItem() == null) {
                continue;
            }
            Result<?> received = inventoryService.receive(item.getItem(), item.getQuantity(), order.getId(), order.getHandledBy());
            if (!received.isSuccess()) {
                return Result.fail("Không thể nhập kho: " + received.getMessage());
            }
        }

        if (!order.confirm()) {
            return Result.fail("Không thể xác nhận phiếu nhập");
        }
        order.setStatus(TransactionStatus.COMPLETED);
        if (!repository.update(order)) {
            return Result.fail("Không thể lưu trạng thái phiếu nhập");
        }
        return Result.ok(order);
    }
}
