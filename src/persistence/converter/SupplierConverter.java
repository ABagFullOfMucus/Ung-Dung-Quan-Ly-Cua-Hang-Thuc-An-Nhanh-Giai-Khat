package persistence.converter;

import model.supplier.Supplier;
import persistence.converter.base.BaseTextConverter;

public class SupplierConverter extends BaseTextConverter<Supplier> {
    
    private static final int FIELD_COUNT = 6;

    /**
     * id|name|phoneNumber|address|companyName|email
     */
    
    @Override
    public String toLine(Supplier entity) {
        if (entity == null) {
            return "";
        }
        return join(entity.getId(), entity.getName(), entity.getPhoneNumber(), entity.getAddress(), entity.getCompanyName(), entity.getEmail());
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

        String id = parts[0].trim();
        if (id.isEmpty()) {
            return null;
        }
        return new Supplier(id, parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), parts[5].trim());
    }
}
