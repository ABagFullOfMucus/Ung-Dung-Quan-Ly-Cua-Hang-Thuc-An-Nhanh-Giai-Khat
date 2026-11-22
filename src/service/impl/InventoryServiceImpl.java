package service.impl;

import common.Result;
import java.time.LocalDateTime;
import model.employee.Employee;
import model.employee.Permission;
import model.inventory.StockChangeType;
import model.inventory.StockEntry;
import model.inventory.StockLog;
import model.menu.MenuItem;
import persistence.Repository;
import service.interfaces.IInventoryService;
import utils.ArrayUtils;
import utils.IdGenerator;
import utils.Validator;


public class InventoryServiceImpl implements IInventoryService {
    private final Repository<StockEntry> stockRepository;
    private final Repository<StockLog> logRepository;

    public InventoryServiceImpl(Repository<StockEntry> stockRepository,
            Repository<StockLog> logRepository) {
        if (stockRepository == null) {
            throw new IllegalArgumentException("stockRepository không được null");
        }
        if (logRepository == null) {
            throw new IllegalArgumentException("logRepository không được null");
        }
        this.stockRepository = stockRepository;
        this.logRepository = logRepository;
    }

    @Override
    public Result<StockEntry> receive(MenuItem item, int qty, String ref, Employee by) {
        if (!hasPermission(by, Permission.IMPORT_STOCK)) {
            return Result.fail("Nhân viên không có quyền nhập kho");
        }
        return changeStock(item, qty, StockChangeType.IMPORT, ref, true);
    }

    @Override
    public Result<StockEntry> issue(MenuItem item, int qty, String ref, Employee by) {
        if (!hasPermission(by, Permission.CREATE_ORDER)) {
            return Result.fail("Nhân viên không có quyền xuất kho");
        }
        return changeStock(item, qty, StockChangeType.SALE, ref, false);
    }

    @Override
    public Result<StockEntry> returnStock(MenuItem item, int qty, String ref, Employee by) {
        if (!hasPermission(by, Permission.CREATE_ORDER) && !hasPermission(by, Permission.IMPORT_STOCK)) {
            return Result.fail("Nhân viên không có quyền hoàn kho");
        }
        return changeStock(item, qty, StockChangeType.RETURN, ref, true);
    }

    @Override
    public Result<StockEntry> adjust(MenuItem item, int newQty, Employee by) {
        if (!hasPermission(by, Permission.IMPORT_STOCK)) {
            return Result.fail("Nhân viên không có quyền điều chỉnh tồn kho");
        }
        if (item == null || !Validator.isNotBlank(item.getId())) {
            return Result.fail("Món hàng không hợp lệ");
        }
        if (newQty < 0) {
            return Result.fail("Số lượng tồn kho mới không được âm");
        }

        String itemId = item.getId().trim();
        StockEntry existing = stockRepository.findById(itemId);
        boolean isNew = (existing == null);
        int before = isNew ? 0 : existing.getQuantity();
        int delta = newQty - before;

        StockEntry entry;
        boolean saved;
        if (isNew) {
            entry = new StockEntry(item, newQty);
            entry.setId(itemId);
            saved = stockRepository.add(entry);
        } else {
            entry = existing;
            if (delta > 0) {
                entry.increase(delta);
            } else if (delta < 0) {
                entry.decrease(-delta);
            }
            saved = stockRepository.update(entry);
            if (!saved) {
                restoreQuantity(entry, before);
            }
        }

        if (!saved) {
            return Result.fail("Không thể lưu điều chỉnh tồn kho");
        }

        StockLog log = new StockLog(
                nextLogId(),
                itemId,
                LocalDateTime.now(),
                StockChangeType.ADJUSTMENT,
                delta,
                before,
                newQty,
                "ADJUST");

        if (!logRepository.add(log)) {
            restoreQuantity(entry, before);
            stockRepository.update(entry);
            return Result.fail("Không thể ghi nhật ký điều chỉnh tồn kho");
        }
        return Result.ok(entry);
    }

    @Override
    public int getQuantity(String itemId) {
        if (!Validator.isNotBlank(itemId)) {
            return 0;
        }
        StockEntry entry = stockRepository.findById(itemId.trim());
        return entry == null ? 0 : entry.getQuantity();
    }

    @Override
    public StockLog[] getHistory(String itemId) {
        if (!Validator.isNotBlank(itemId)) {
            return new StockLog[0];
        }
        String targetId = itemId.trim();
        StockLog[] allLogs = logRepository.findAll();
        if (allLogs == null || allLogs.length == 0) {
            return new StockLog[0];
        }

        StockLog[] buffer = new StockLog[allLogs.length];
        for (int i = 0; i < allLogs.length; i++) {
            if (allLogs[i] != null && targetId.equals(allLogs[i].getMenuItemId())) {
                buffer[i] = allLogs[i];
            }
        }
        int matchedCount = ArrayUtils.countNonNull(buffer);
        return ArrayUtils.compact(buffer, new StockLog[matchedCount]);
    }

    private Result<StockEntry> changeStock(MenuItem item, int qty, StockChangeType type,
            String ref, boolean isIncrease) {
        if (item == null || !Validator.isNotBlank(item.getId())) {
            return Result.fail("Món hàng không hợp lệ");
        }
        if (qty <= 0) {
            return Result.fail("Số lượng thay đổi tồn kho phải lớn hơn 0");
        }
        if (!Validator.isSafeText(ref)) {
            return Result.fail("Mã tham chiếu chứa ký tự không hợp lệ (| hoặc xuống dòng)");
        }

        String itemId = item.getId().trim();
        String itemName = Validator.isNotBlank(item.getName()) ? item.getName() : itemId;
        String safeRef = ref == null ? "" : ref.trim();
        StockEntry existing = stockRepository.findById(itemId);

        if (!isIncrease) {
            int available = existing == null ? 0 : existing.getQuantity();
            if (existing == null || available < qty) {
                return Result.fail("Không đủ tồn kho cho món " + itemName
                        + " (hiện có: " + available + ", cần: " + qty + ")");
            }
            int before = available;
            if (!existing.decrease(qty)) {
                return Result.fail("Không thể trừ tồn kho cho món " + itemName);
            }
            int after = existing.getQuantity();

            if (!stockRepository.update(existing)) {
                existing.increase(qty);
                return Result.fail("Không thể lưu cập nhật tồn kho");
            }

            StockLog log = new StockLog(
                    nextLogId(),
                    itemId,
                    LocalDateTime.now(),
                    type,
                    qty,
                    before,
                    after,
                    safeRef);
            if (!logRepository.add(log)) {
                existing.increase(qty);
                stockRepository.update(existing);
                return Result.fail("Không thể ghi nhật ký xuất kho");
            }
            return Result.ok(existing);
        }

        boolean isNew = (existing == null);
        int before = isNew ? 0 : existing.getQuantity();
        if (qty > Integer.MAX_VALUE - before) {
            return Result.fail("Số lượng tồn kho vượt quá giới hạn cho phép");
        }

        StockEntry entry;
        if (isNew) {
            entry = new StockEntry(item, qty);
            entry.setId(itemId);
        } else {
            entry = existing;
            entry.increase(qty);
        }
        int after = entry.getQuantity();

        boolean saved = isNew ? stockRepository.add(entry) : stockRepository.update(entry);
        if (!saved) {
            if (!isNew) {
                entry.decrease(qty);
            }
            return Result.fail("Không thể lưu cập nhật tồn kho");
        }

        StockLog log = new StockLog(
                nextLogId(),
                itemId,
                LocalDateTime.now(),
                type,
                qty,
                before,
                after,
                safeRef);
        if (!logRepository.add(log)) {
            entry.decrease(qty);
            stockRepository.update(entry);
            return Result.fail("Không thể ghi nhật ký nhập/hoàn kho");
        }
        return Result.ok(entry);
    }

    private void restoreQuantity(StockEntry entry, int targetQty) {
        int current = entry.getQuantity();
        if (current < targetQty) {
            entry.increase(targetQty - current);
        } else if (current > targetQty) {
            entry.decrease(current - targetQty);
        }
    }

    private boolean hasPermission(Employee by, Permission permission) {
        return by != null && by.isActive() && by.getRole() != null && by.getRole().can(permission);
    }

    private String nextLogId() {
        String[] existingIds = ArrayUtils.extractIds(logRepository.findAll());
        return IdGenerator.next("SL", existingIds);
    }
}