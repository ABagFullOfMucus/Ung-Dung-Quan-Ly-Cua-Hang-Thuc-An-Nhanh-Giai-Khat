package model.menu;

public class MenuItemFactory {
    public static MenuItem create(String type, String[] fields) {
        if (type == null || fields == null || fields.length < 5) {
            return null;
        }
        String id = fields[0];
        String name = fields[1];
        String description = fields[3];
        String extra = fields[4];

        if (!isNumber(fields[2]) || extra == null) {
            return null;
        }
        double price = Double.parseDouble(fields[2].trim());
        if (price <= 0) {
            return null;
        }

        if (type.equalsIgnoreCase("DRINK")) {
            DrinkSize size = findSize(extra);
            if (size == null) {
                return null;
            }
            return new Drink(id, name, price, description, size);
        }

        if (type.equalsIgnoreCase("FASTFOOD")) {
            if (!isNumber(extra)) {
                return null;
            }
            double calories = Double.parseDouble(extra.trim());
            return new FastFood(id, name, price, description, calories);
        }
        return null;
    }

    private boolean isNumber(String s) {
        return s != null && s.trim().matches("\\d+(\\.\\d+)?");
    }

    private DrinkSize findSize(String s) {
        for (DrinkSize size : DrinkSize.values()) {
            if (size.name().equalsIgnoreCase(s.trim())) {
                return size;
            }
        }
        return null;
    }
}
