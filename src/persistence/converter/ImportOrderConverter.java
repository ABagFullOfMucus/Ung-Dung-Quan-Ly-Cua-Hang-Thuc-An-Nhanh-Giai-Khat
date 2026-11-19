package persistence.converter;

import model.transaction.ImportOrder;
import persistence.converter.base.BaseTextConverter;

public class ImportOrderConverter extends BaseTextConverter<ImportOrder> {

    @Override
    public ImportOrder fromLine(String line) {
        return null;
    }

    @Override
    public String toLine(ImportOrder entity) {
        return null;
    }

}
