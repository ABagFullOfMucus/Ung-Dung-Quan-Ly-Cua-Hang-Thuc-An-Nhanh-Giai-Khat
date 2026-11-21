package persistence.converter;

import model.menu.Drink;
import model.menu.FastFood;
import model.menu.MenuItem;
import model.menu.MenuItemFactory;
import persistence.converter.base.BaseTextConverter;

public class MenuItemConverter extends BaseTextConverter<MenuItem> {
    private static final String DRINK = "DRINK";
    private static final String FASTFOOD = "FASTFOOD";
    private static final int FIELD_COUNT = 7;

    /**
     * id|name|price|description|DRINK|size|active
     * id|name|price|description|FASTFOOD|calories|active
     */
    @Override
    public String toLine(MenuItem entity) {
        if (entity == null) {
            return "";
        }
        if (entity instanceof Drink) {
            Drink drink = (Drink) entity;
            return join(drink.getId(), drink.getName(), drink.getPrice(), drink.getDescription(),
                    DRINK, drink.getSize(), drink.isActive());
        }
        if (entity instanceof FastFood) {
            FastFood fastFood = (FastFood) entity;
            return join(fastFood.getId(), fastFood.getName(), fastFood.getPrice(), fastFood.getDescription(),
                    FASTFOOD, fastFood.getCalories(), fastFood.isActive());
        }
        return "";
    }

    @Override
    public MenuItem fromLine(String line) {
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

        String type = parts[4].trim().toUpperCase();
        if (!DRINK.equals(type) && !FASTFOOD.equals(type)) {
            return null;
        }

        String activeStr = parts[6].trim();
        if (!"true".equalsIgnoreCase(activeStr) && !"false".equalsIgnoreCase(activeStr)) {
            return null;
        }
        boolean active = Boolean.parseBoolean(activeStr);

        String[] fields = { id, parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[5].trim() };
        MenuItem item = MenuItemFactory.create(type, fields);
        if (item != null && !active) {
            item.deactivate();
        }
        return item;
    }
}