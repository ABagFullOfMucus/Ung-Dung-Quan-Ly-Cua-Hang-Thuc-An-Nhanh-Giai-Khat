package persistence.converter;

import model.supplier.Supplier;
import persistence.converter.base.BaseTextConverter;

public class SupplierConverter extends BaseTextConverter<Supplier> {
    private static final int FIELD_COUNT = 5;

    @Override
    public String toLine(Supplier entity) {
        if (entity == null)
            return null;

        return join(entity.getId(), entity.getName(), entity.getPhoneNumber(), entity.getAddress(), entity.getEmail());
    }

    @Override
    public Supplier fromLine(String line) {
              if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = split(line);
        if (parts == null || parts.length != FIELD_COUNT) {
            return null;
        }
        
        if()


    }

}
