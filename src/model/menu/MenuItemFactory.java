package model.menu;

import java.util.List;

public class MenuItemFactory {
    public static MenuItem create(String type, List<String> fields) {
        if (type == null || fields == null) {
            return null;
        }
        String id = fields.get(0);
        String name = fields.get(1);
        double price = Double.parseDouble(fields.get(2));
        String description = fields.get(3);

        if ("DRINK".equalsIgnoreCase(type)) {
            DrinkSize size = DrinkSize.valueOf(fields.get(4).toUpperCase());
            return new Drink(id, name, price, description, size);
        } else if ("FASTFOOD".equalsIgnoreCase(type)) {
            double calories = Double.parseDouble(fields.get(4));
            return new FastFood(id, name, price, description, calories);
        }

        return null; 
    }
}
