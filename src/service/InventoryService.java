package service;

import common.Result;
import model.employee.Employee;
import model.inventory.StockChangeType;
import model.inventory.StockEntry;
import model.inventory.StockLog;
import model.menu.MenuItem;
import persistence.Repository;
import utils.IdGenerator;

import java.time.LocalDateTime;

public class InventoryService {
    private final Repository<StockEntry> stockRepository;
    private final Repository<StockLog> stockLogRepository;

    public InventoryService(Repository<StockEntry> stockRepository, Repository<StockLog> stockLogRepository) {
        this.stockRepository = stockRepository;
        this.stockLogRepository = stockLogRepository;
    }

    public InventoryService(Repository<StockEntry> stockRepository) {
        this(stockRepository, null);
    }

    public Result<StockEntry> receive(MenuItem item, int qty, String ref, Employee by) {
        if (item == null || item.getId() == null) {
            return Result.fail("Món không hợp lệ");
        }
        if (qty <= 0) {
            return Result.fail("Số lượng nhập phải lớn hơn 0");
        }

        StockEntry entry = stockRepository.findById(item.getId());
        int before = 0;
        if (entry == null) {
            entry = new StockEntry(item, qty);
            before = 0;
            if (!stockRepository.add(entry)) {
                return Result.fail("Không thể lưu tồn kho");
            }
        } else {
            before = entry.getQuantity();
            entry.increase(qty);
            if (!stockRepository.update(entry)) {
                return Result.fail("Không thể cập nhật tồn kho");
            }
        }

        appendLog(item.getId(), StockChangeType.IMPORT, qty, before, entry.getQuantity(), ref);
        return Result.ok(entry);
    }

    public Result<StockEntry> issue(MenuItem item, int qty, String ref, Employee by) {
        if (item == null || item.getId() == null) {
            return Result.fail("Món không hợp lệ");
        }
        if (qty <= 0) {
            return Result.fail("Số lượng xuất phải lớn hơn 0");
        }

        StockEntry entry = stockRepository.findById(item.getId());
        if (entry == null) {
            return Result.fail("Chưa có dữ liệu tồn kho cho món " + item.getId());
        }

        int before = entry.getQuantity();
        if (!entry.decrease(qty)) {
            return Result.fail("Không đủ tồn kho cho món " + item.getName());
        }
        if (!stockRepository.update(entry)) {
            return Result.fail("Không thể cập nhật tồn kho");
        }

        appendLog(item.getId(), StockChangeType.SALE, qty, before, entry.getQuantity(), ref);
        return Result.ok(entry);
    }

    public Result<StockEntry> adjust(MenuItem item, int newQty, Employee by) {
        if (item == null || item.getId() == null) {
            return Result.fail("Món không hợp lệ");
        }
        if (newQty < 0) {
            return Result.fail("Số lượng tồn kho không được âm");
        }

        StockEntry entry = stockRepository.findById(item.getId());
        int before = 0;
        if (entry == null) {
            entry = new StockEntry(item, newQty);
            if (!stockRepository.add(entry)) {
                return Result.fail("Không thể lưu tồn kho");
            }
        } else {
            before = entry.getQuantity();
            entry = new StockEntry(item, newQty);
            if (!stockRepository.update(entry)) {
                return Result.fail("Không thể cập nhật tồn kho");
            }
        }

        int delta = Math.abs(newQty - before);
        appendLog(item.getId(), StockChangeType.ADJUSTMENT, delta, before, newQty, null);
        return Result.ok(entry);
    }

    public int getQuantity(String itemId) {
        if (itemId == null) {
            return 0;
        }
        StockEntry entry = stockRepository.findById(itemId);
        return entry == null ? 0 : entry.getQuantity();
    }

    public StockLog[] getHistory(String itemId) {
        if (stockLogRepository == null || itemId == null) {
            return new StockLog[0];
        }
        StockLog[] all = getLogsSafe();
        StockLog[] temp = new StockLog[all.length];
        int count = 0;
        for (StockLog log : all) {
            if (log != null && itemId.equals(log.getMenuItemId())) {
                temp[count++] = log;
            }
        }
        StockLog[] result = new StockLog[count];
        for (int i = 0; i < count; i++) {
            result[i] = temp[i];
        }
        return result;
    }

    private void appendLog(String menuItemId, StockChangeType type, int quantity,
                           int before, int after, String refId) {
        if (stockLogRepository == null) {
            return;
        }
        String[] ids = collectLogIds();
        String id = IdGenerator.next("SL", ids);
        StockLog log = new StockLog(id, menuItemId, LocalDateTime.now(), type, quantity, before, after, refId);
        stockLogRepository.add(log);
    }

    private String[] collectLogIds() {
        StockLog[] logs = getLogsSafe();
        String[] ids = new String[logs.length];
        for (int i = 0; i < logs.length; i++) {
            ids[i] = logs[i] != null ? logs[i].getId() : null;
        }
        return ids;
    }

    private StockLog[] getLogsSafe() {
        if (stockLogRepository == null) {
            return new StockLog[0];
        }
        Object[] all = stockLogRepository.findAll();
        if (all == null) {
            return new StockLog[0];
        }
        StockLog[] result = new StockLog[all.length];
        for (int i = 0; i < all.length; i++) {
            result[i] = (StockLog) all[i];
        }
        return result;
    }
}
