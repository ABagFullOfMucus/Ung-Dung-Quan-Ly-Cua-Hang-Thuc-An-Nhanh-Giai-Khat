package model.menu;

public class MenuItemFactory {
    public static MenuItem create(String type, String[] fields) {
        if (type == null || fields == null || fields.length < 5) {
            return null;
        }
        String id = fields[0];
        String name = fields[1];
        double price = Double.parseDouble(fields[2]);
        String description = fields[3];

        if ("DRINK".equalsIgnoreCase(type)) {
            DrinkSize size = DrinkSize.valueOf(fields[4].toUpperCase());
            return new Drink(id, name, price, description, size);
        } else if ("FASTFOOD".equalsIgnoreCase(type)) {
            double calories = Double.parseDouble(fields[4]);
            return new FastFood(id, name, price, description, calories);
        }
        
        return null; 
    }
}
