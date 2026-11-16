package persistence.converter;

import model.inventory.StockEntry;
import model.menu.Drink;
import model.menu.DrinkSize;
import model.menu.MenuItem;
import persistence.converter.base.BaseTextConverter;

public class StockEntryConverter extends BaseTextConverter<StockEntry> {
    @Override
    public String toLine(StockEntry entity) {
        if (entity == null) {
            return "";
        }
        String itemId = entity.getItem() != null ? entity.getItem().getId() : entity.getId();
        return join(itemId, entity.getQuantity(), entity.isActive());
    }

    @Override
    public StockEntry fromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = split(line);
        if (parts.length < 2) {
            return null;
        }
        try {
            String itemId = parts[0].trim();
            int quantity = Integer.parseInt(parts[1].trim());
            if (itemId.isEmpty() || quantity < 0) {
                return null;
            }
            MenuItem placeholder = new Drink(itemId, itemId, 0, "", DrinkSize.M);
            StockEntry entry = new StockEntry(placeholder, quantity);
            if (parts.length >= 3 && "false".equalsIgnoreCase(parts[2].trim())) {
                entry.deactivate();
            }
            return entry;
        } catch (Exception e) {
            return null;
        }
    }
}
