package persistence.converter;

import model.inventory.StockChangeType;
import model.inventory.StockLog;
import persistence.converter.base.BaseTextConverter;

import java.time.LocalDateTime;

public class StockLogConverter extends BaseTextConverter<StockLog> {
    @Override
    public String toLine(StockLog entity) {
        if (entity == null) {
            return "";
        }
        return join(entity.getId(), entity.getMenuItemId(), entity.getLogTime(),
                entity.getChangeType(), entity.getQuantity(), entity.getQuantityBefore(),
                entity.getQuantityAfter(), entity.getRefId());
    }

    @Override
    public StockLog fromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = split(line);
        if (parts.length < 8) {
            return null;
        }
        try {
            String id = parts[0];
            String menuItemId = parts[1];
            LocalDateTime time = LocalDateTime.parse(parts[2]);
            StockChangeType type = StockChangeType.valueOf(parts[3]);
            int quantity = Integer.parseInt(parts[4]);
            int before = Integer.parseInt(parts[5]);
            int after = Integer.parseInt(parts[6]);
            String refId = parts[7].isEmpty() ? null : parts[7];
            return new StockLog(id, menuItemId, time, type, quantity, before, after, refId);
        } catch (Exception e) {
            return null;
        }
    }
}
