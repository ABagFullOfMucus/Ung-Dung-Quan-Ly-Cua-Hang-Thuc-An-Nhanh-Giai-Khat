package model.menu;

public class FastFood extends MenuItem {
    private double calories;

    public FastFood() {
        super();
    }

    public FastFood(String id, String name, double price, String description, double calories) {
        super(id, name, price, description);
        this.calories = calories;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
}
