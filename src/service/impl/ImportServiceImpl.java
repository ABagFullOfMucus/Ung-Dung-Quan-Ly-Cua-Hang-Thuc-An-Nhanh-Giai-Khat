package service.impl;

import common.Result;
import model.employee.Employee;
import model.employee.Permission;
import model.inventory.StockEntry;
import model.menu.MenuItem;
import model.supplier.Supplier;
import model.transaction.ImportItem;
import model.transaction.ImportOrder;
import model.transaction.TransactionStatus;
import persistence.Repository;
import service.base.TransactionService;
import service.interfaces.IImportService;
import service.interfaces.IInventoryService;
import service.interfaces.IMenuService;
import service.interfaces.ISupplierService;
import utils.Validator;

public class ImportServiceImpl extends TransactionService<ImportOrder, ImportItem> implements IImportService {
    private final ISupplierService supplierService;
    private final IMenuService menuService;
    private final IInventoryService inventoryService;

    public ImportServiceImpl(Repository<ImportOrder> importRepository,
            ISupplierService supplierService,
            IMenuService menuService,
            IInventoryService inventoryService) {
        super(importRepository);
        if (supplierService == null) {
            throw new IllegalArgumentException("ISupplierService không được null");
        }
        if (menuService == null) {
            throw new IllegalArgumentException("IMenuService không được null");
        }
        if (inventoryService == null) {
            throw new IllegalArgumentException("IInventoryService không được null");
        }
        this.supplierService = supplierService;
        this.menuService = menuService;
        this.inventoryService = inventoryService;
    }

    @Override
    protected String idPrefix() {
        return "PN";
    }

    @Override
    protected ImportOrder[] newArray(int size) {
        return new ImportOrder[size];
    }

    @Override
    public Result<ImportOrder> createImport(String supplierId, Employee employee) {
        if (!canImportStock(employee)) {
            return Result.fail("Nhân viên không có quyền tạo phiếu nhập hàng");
        }
        if (!Validator.isNotBlank(supplierId)) {
            return Result.fail("Mã nhà cung cấp không hợp lệ");
        }

        Result<Supplier> supplierResult = supplierService.getById(supplierId.trim());
        if (!supplierResult.isSuccess()) {
            return Result.fail(supplierResult.getMessage());
        }
        Supplier supplier = supplierResult.getData();
        if (!supplier.isActive()) {
            return Result.fail("Nhà cung cấp đã bị vô hiệu hóa");
        }

        ImportOrder importOrder = new ImportOrder(nextId(), employee, supplier);
        return persistNew(importOrder);
    }

    @Override
    public Result<ImportOrder> addItem(String importId, String itemId, int qty, double cost) {
        if (qty <= 0) {
            return Result.fail("Số lượng nhập phải lớn hơn 0");
        }
        if (!Double.isFinite(cost) || cost <= 0) {
            return Result.fail("Đơn giá nhập phải lớn hơn 0");
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
            return Result.fail("Món đã ngừng kinh doanh, không thể nhập hàng");
        }
        return addLine(importId, new ImportItem(menuItem, qty, cost));
    }

    @Override
    public Result<ImportOrder> removeItem(String importId, String itemId) {
        return removeLine(importId, itemId);
    }

    @Override
    public Result<ImportOrder> receiveImport(String importId) {
        Result<ImportOrder> found = findTransaction(importId);
        if (!found.isSuccess()) {
            return found;
        }
        ImportOrder importOrder = found.getData();
        if (importOrder.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể nhận hàng cho phiếu nhập ở trạng thái nháp (DRAFT)");
        }
        if (!canImportStock(importOrder.getHandledBy())) {
            return Result.fail("Nhân viên phụ trách phiếu không còn quyền nhập kho");
        }

        ImportItem[] items = importOrder.getItems();
        if (items == null || items.length == 0) {
            return Result.fail("Không thể nhận phiếu nhập rỗng");
        }

        for (int i = 0; i < items.length; i++) {
            ImportItem line = items[i];
            if (line == null || line.getItem() == null
                    || !Validator.isNotBlank(line.getItem().getId())
                    || line.getQuantity() <= 0
                    || !Double.isFinite(line.getUnitAmount())
                    || line.getUnitAmount() <= 0) {
                return Result.fail("Phiếu nhập chứa dòng hàng không hợp lệ");
            }
        }

        ImportItem[] receivedItems = new ImportItem[items.length];
        int receivedCount = 0;
        for (int i = 0; i < items.length; i++) {
            ImportItem line = items[i];
            Result<StockEntry> receiveResult = inventoryService.receive(
                    line.getItem(), line.getQuantity(), importOrder.getId(), importOrder.getHandledBy());
            if (!receiveResult.isSuccess()) {
                rollbackReceivedStock(receivedItems, receivedCount, importOrder);
                return Result.fail("Không thể nhập kho: " + receiveResult.getMessage());
            }
            receivedItems[receivedCount++] = line;
        }

        if (!importOrder.confirm()) {
            rollbackReceivedStock(receivedItems, receivedCount, importOrder);
            return Result.fail("Không thể xác nhận phiếu nhập");
        }

        if (!getRepository().update(importOrder)) {
            importOrder.setStatus(TransactionStatus.DRAFT);
            rollbackReceivedStock(receivedItems, receivedCount, importOrder);
            return Result.fail("Không thể lưu trạng thái phiếu nhập");
        }
        return Result.ok(importOrder);
    }

    @Override
    public Result<ImportOrder> cancelImport(String importId) {
        return cancelDraft(importId);
    }

    private void rollbackReceivedStock(ImportItem[] items, int count, ImportOrder importOrder) {
        for (int i = count - 1; i >= 0; i--) {
            ImportItem line = items[i];
            if (line != null && line.getItem() != null) {
                int currentQty = inventoryService.getQuantity(line.getItem().getId());
                int restoredQty = Math.max(0, currentQty - line.getQuantity());
                inventoryService.adjust(line.getItem(), restoredQty, importOrder.getHandledBy());
            }
        }
    }

    private boolean canImportStock(Employee employee) {
        return employee != null
                && employee.isActive()
                && employee.getRole() != null
                && employee.getRole().can(Permission.IMPORT_STOCK);
    }
}