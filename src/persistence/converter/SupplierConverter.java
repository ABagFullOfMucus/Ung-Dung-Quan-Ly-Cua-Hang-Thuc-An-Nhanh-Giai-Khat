package persistence.converter;

import model.supplier.Supplier;
import persistence.converter.base.BaseTextConverter;

public class SupplierConverter extends BaseTextConverter<Supplier> {
    @Override
    public String toLine(Supplier entity) {
        if (entity == null) {
            return "";
        }
        return join(entity.getId(), entity.getName(), entity.getPhoneNumber(),
                entity.getAddress(), entity.getCompanyName(), entity.getEmail(), entity.isActive());
    }

    @Override
    public Supplier fromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = split(line);
        if (parts.length < 6) {
            return null;
        }
        String id = parts[0].trim();
        if (id.isEmpty()) {
            return null;
        }
        Supplier supplier = new Supplier(id, parts[1], parts[2], parts[3], parts[4], parts[5]);
        if (parts.length >= 7 && "false".equalsIgnoreCase(parts[6].trim())) {
            supplier.deactivate();
        }
        return supplier;
    }
}
