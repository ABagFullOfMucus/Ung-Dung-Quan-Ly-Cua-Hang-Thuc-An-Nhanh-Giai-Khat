package persistence.converter;

import java.time.LocalDateTime;

import model.inventory.StockChangeType;
import model.inventory.StockLog;
import persistence.converter.base.BaseTextConverter;

public class StockLogConverter extends BaseTextConverter<StockLog> {
    private static final int FIELD_COUNT = 8;

    /**
     * id|menuItemId|logTime|changeType|quantity|quantityBefore|quantityAfter|refId
     */
    @Override
    public String toLine(StockLog entity) {
        if (entity == null) {
            return "";
        }
        String logTime = entity.getLogTime() == null ? "" : entity.getLogTime().toString();
        return join(entity.getId(), entity.getMenuItemId(), logTime, entity.getChangeType(), entity.getQuantity(), entity.getQuantityBefore(), entity.getQuantityAfter(), entity.getRefId());
    }

    @Override
    public StockLog fromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = split(line);
        if (parts == null || parts.length != FIELD_COUNT) {
            return null;
        }

        String id = parts[0].trim();
        String menuItemId = parts[1].trim();
        if (id.isEmpty() || menuItemId.isEmpty()) {
            return null;
        }

        try {
            LocalDateTime logTime = LocalDateTime.parse(parts[2].trim());
            StockChangeType changeType = StockChangeType.valueOf(parts[3].trim().toUpperCase());
            int quantity = Integer.parseInt(parts[4].trim());
            int quantityBefore = Integer.parseInt(parts[5].trim());
            int quantityAfter = Integer.parseInt(parts[6].trim());
            String refId = parts[7].trim();

            if (quantityBefore < 0 || quantityAfter < 0) {
                return null;
            }
            return new StockLog(id, menuItemId, logTime, changeType,
                    quantity, quantityBefore, quantityAfter, refId);
        } catch (Exception e) {
            return null;
        }
    }
}
