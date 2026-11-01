package model.menu;

import java.util.List;

public class MenuItemFactory {
    public static MenuItem create(String type, List<String> fields) {
        if (type == null || fields == null) {
            return null;
        }

        try {
            switch (type.toUpperCase()) {
                case "DRINK":
                    if (fields.size() < 6) return null;
                    
                    String drinkId = fields.get(0);
                    String drinkName = fields.get(1);
                    double drinkPrice = Double.parseDouble(fields.get(2));
                    String drinkDesc = fields.get(3);
                    DrinkSize size = DrinkSize.valueOf(fields.get(4).toUpperCase());
                    boolean isIced = Boolean.parseBoolean(fields.get(5));
                    
                    return new Drink(drinkId, drinkName, drinkPrice, drinkDesc, size, isIced);

                case "FASTFOOD":
                    if (fields.size() < 5) return null;
                    
                    String foodId = fields.get(0);
                    String foodName = fields.get(1);
                    double foodPrice = Double.parseDouble(fields.get(2));
                    String foodDesc = fields.get(3);
                    int calories = Integer.parseInt(fields.get(4)); // calories là int
                    
                    return new FastFood(foodId, foodName, foodPrice, foodDesc, calories);

                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
