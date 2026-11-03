package persistence.converter;

import model.customer.Customer;
import persistence.converter.base.BaseTextConverter;

public class CustomerConverter extends BaseTextConverter<Customer> {
    private static final int FIELD_COUNT = 5;

    /**
     * (non-Javadoc)
     * định dạng của nó là String
     * id|name|phoneNumber|address|loyaltyPoints
     * 
     * @see interfaces.TextConverter#toLine(java.lang.Object)
     */

    @Override
    public String toLine(Customer entity) {
        if (entity == null) {
            return "";
        }
        return join(
                entity.getId(),
                entity.getName(),
                entity.getPhoneNumber(),
                entity.getAddress(),
                entity.getLoyaltyPoints());
    }

    @Override
    public Customer fromLine(String line) {
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

        int points;
        try {
            points = Integer.parseInt(parts[4].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        if (points < 0)
            return null;
        return new Customer(id, parts[1].trim(), parts[2].trim(), parts[3].trim(), points);
    }
}