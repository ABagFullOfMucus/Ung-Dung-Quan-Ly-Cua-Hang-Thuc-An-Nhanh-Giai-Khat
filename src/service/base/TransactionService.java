package service.base;

import common.Result;
import model.transaction.LineItem;
import model.transaction.Transaction;
import model.transaction.TransactionStatus;
import persistence.Repository;
import service.interfaces.IReadService;
import utils.ArrayUtils;
import utils.IdGenerator;
import utils.Validator;

public abstract class TransactionService<T extends Transaction<I>, I extends LineItem>
        implements IReadService<T> {
    private final Repository<T> repository;

    protected TransactionService(Repository<T> repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository không được null");
        }
        this.repository = repository;
    }

    protected final Repository<T> getRepository() {
        return repository;
    }

    protected abstract String idPrefix();

    protected abstract T[] newArray(int size);

    protected final String nextId() {
        String[] existingIds = ArrayUtils.extractIds(repository.findAll());
        return IdGenerator.next(idPrefix(), existingIds);
    }

    protected final Result<T> findTransaction(String id) {
        if (!Validator.isNotBlank(id)) {
            return Result.fail("Mã giao dịch không hợp lệ");
        }
        String cleanId = id.trim();
        T tx = repository.findById(cleanId);
        if (tx == null) {
            return Result.fail("Không tìm thấy giao dịch có mã " + cleanId);
        }
        return Result.ok(tx);
    }

    protected final Result<T> persistNew(T tx) {
        if (tx == null) {
            return Result.fail("Giao dịch không hợp lệ");
        }
        if (!repository.add(tx)) {
            return Result.fail("Không thể lưu giao dịch mới");
        }
        return Result.ok(tx);
    }

    protected final Result<T> addLine(String txId, I lineItem) {
        Result<T> found = findTransaction(txId);
        if (!found.isSuccess()) {
            return found;
        }
        T tx = found.getData();
        if (tx.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể thêm món khi đơn đang ở trạng thái nháp (DRAFT)");
        }
        if (lineItem == null
                || lineItem.getItem() == null
                || !Validator.isNotBlank(lineItem.getItem().getId())
                || lineItem.getQuantity() <= 0
                || !Double.isFinite(lineItem.getUnitAmount())
                || lineItem.getUnitAmount() < 0) {
            return Result.fail("Thông tin dòng hàng không hợp lệ");
        }

        String menuItemId = lineItem.getItem().getId().trim();
        I existingLine = findLineByItemId(tx, menuItemId);
        int oldQty = 0;

        if (existingLine != null) {
            oldQty = existingLine.getQuantity();
            if (Double.compare(existingLine.getUnitAmount(), lineItem.getUnitAmount()) != 0) {
                return Result
                        .fail("Món đã tồn tại trong đơn với đơn giá khác, vui lòng xóa dòng cũ trước khi thêm lại");
            }
            if (lineItem.getQuantity() > Integer.MAX_VALUE - oldQty) {
                return Result.fail("Tổng số lượng của món vượt quá giới hạn cho phép");
            }
        }

        if (!tx.addItem(lineItem)) {
            return Result.fail("Không thể thêm món vào đơn");
        }

        if (!repository.update(tx)) {
            if (existingLine != null) {
                existingLine.changeQuantity(oldQty);
            } else {
                tx.removeItem(menuItemId);
            }
            return Result.fail("Không thể lưu đơn sau khi thêm món");
        }
        return Result.ok(tx);
    }

    protected final Result<T> removeLine(String txId, String itemId) {
        Result<T> found = findTransaction(txId);
        if (!found.isSuccess()) {
            return found;
        }
        T tx = found.getData();
        if (tx.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể xóa món khi đơn đang ở trạng thái nháp (DRAFT)");
        }
        if (!Validator.isNotBlank(itemId)) {
            return Result.fail("Mã món không hợp lệ");
        }

        String cleanItemId = itemId.trim();
        I removedLine = findLineByItemId(tx, cleanItemId);
        if (removedLine == null) {
            return Result.fail("Không tìm thấy món có mã " + cleanItemId + " trong đơn");
        }
        if (!tx.removeItem(cleanItemId)) {
            return Result.fail("Không thể xóa món khỏi đơn");
        }

        if (!repository.update(tx)) {
            tx.addItem(removedLine);
            return Result.fail("Không thể lưu đơn sau khi xóa món");
        }
        return Result.ok(tx);
    }

    protected final Result<T> cancelDraft(String txId) {
        Result<T> found = findTransaction(txId);
        if (!found.isSuccess()) {
            return found;
        }
        T tx = found.getData();
        if (tx.getStatus() != TransactionStatus.DRAFT) {
            return Result.fail("Chỉ có thể hủy đơn đang ở trạng thái nháp (DRAFT)");
        }
        if (!tx.cancel()) {
            return Result.fail("Không thể hủy đơn");
        }
        if (!repository.update(tx)) {
            tx.setStatus(TransactionStatus.DRAFT);
            return Result.fail("Không thể lưu trạng thái hủy đơn");
        }
        return Result.ok(tx);
    }

    private I findLineByItemId(T tx, String itemId) {
        I[] lines = tx.getItems();
        if (lines == null) {
            return null;
        }
        for (int i = 0; i < lines.length; i++) {
            I line = lines[i];
            if (line != null && line.getItem() != null && itemId.equals(line.getItem().getId())) {
                return line;
            }
        }
        return null;
    }

    @Override
    public Result<T> getById(String id) {
        return findTransaction(id);
    }

    @Override
    public T[] getAll() {
        T[] all = repository.findAll();
        return all == null ? newArray(0) : all;
    }
}