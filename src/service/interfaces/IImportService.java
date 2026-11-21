package service.interfaces;

import common.Result;
import model.employee.Employee;
import model.transaction.ImportOrder;

public interface IImportService extends IReadService<ImportOrder> {
    Result<ImportOrder> createImport(String supplierId, Employee employee);

    Result<ImportOrder> addItem(String importId, String itemId, int qty, double cost);

    Result<ImportOrder> removeItem(String importId, String itemId);

    Result<ImportOrder> receiveImport(String importId);

    Result<ImportOrder> cancelImport(String importId);
}