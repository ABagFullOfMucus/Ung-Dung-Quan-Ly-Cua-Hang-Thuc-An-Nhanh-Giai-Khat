package persistence.converter;

import model.inventory.StockEntry;
import model.menu.MenuItem;
import persistence.Repository;
import persistence.converter.base.BaseTextConverter;

public class StockEntryConverter extends BaseTextConverter<StockEntry> {
    private static final int FIELD_COUNT = 3;

    private final Repository<MenuItem> menuRepository;

    public StockEntryConverter(Repository<MenuItem> menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * id|menuItemId|quantity
     */
    @Override
    public String toLine(StockEntry entity) {
        if (entity == null) {
            return "";
        }
        String itemId = entity.getItem() == null ? "" : entity.getItem().getId();
        return join(entity.getId(), itemId, entity.getQuantity());
    }

    @Override
    public StockEntry fromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = split(line);
        if (parts == null || parts.length != FIELD_COUNT) {
            return null;
        }

        String id = parts[0].trim();
        String itemId = parts[1].trim();
        if (id.isEmpty() || itemId.isEmpty()) {
            return null;
        }

        MenuItem item = menuRepository == null ? null : menuRepository.findById(itemId);
        if (item == null) {
            return null;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        if (quantity < 0) {
            return null;
        }

        StockEntry entry = new StockEntry(item, quantity);
        entry.setId(id);
        return entry;
    }
}
