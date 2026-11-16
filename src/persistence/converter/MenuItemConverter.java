package persistence.converter;

import model.menu.Drink;
import model.menu.DrinkSize;
import model.menu.FastFood;
import model.menu.MenuItem;
import persistence.converter.base.BaseTextConverter;

public class MenuItemConverter extends BaseTextConverter<MenuItem> {
    @Override
    public String toLine(MenuItem entity) {
        if (entity == null) {
            return "";
        }
        if (entity instanceof Drink) {
            Drink d = (Drink) entity;
            return join("DRINK", d.getId(), d.getName(), d.getPrice(), d.getDescription(), d.getSize(), d.isActive());
        }
        if (entity instanceof FastFood) {
            FastFood f = (FastFood) entity;
            return join("FOOD", f.getId(), f.getName(), f.getPrice(), f.getDescription(), f.getCalories(), f.isActive());
        }
        return "";
    }

    @Override
    public MenuItem fromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = split(line);
        if (parts.length < 6) {
            return null;
        }
        try {
            String type = parts[0].trim().toUpperCase();
            String id = parts[1].trim();
            String name = parts[2];
            double price = Double.parseDouble(parts[3].trim());
            String desc = parts[4];
            MenuItem item = null;
            if ("DRINK".equals(type)) {
                DrinkSize size = DrinkSize.valueOf(parts[5].trim().toUpperCase());
                item = new Drink(id, name, price, desc, size);
                if (parts.length >= 7 && "false".equalsIgnoreCase(parts[6].trim())) item.deactivate();
            } else if ("FOOD".equals(type)) {
                double calories = Double.parseDouble(parts[5].trim());
                item = new FastFood(id, name, price, desc, calories);
                if (parts.length >= 7 && "false".equalsIgnoreCase(parts[6].trim())) item.deactivate();
            }
            return item;
        } catch (Exception e) {
            return null;
        }
    }
}
