package model;

public class Drink extends MenuItem {
    private DrinkSize size;

    public Drink(String id, String name, double price, String description, DrinkSize size) {
        super(id, name, price, description);
        this.size = size;
    }

    public DrinkSize getSize() {
        return size;
    }

    public void setSize(DrinkSize size) {
        this.size = size;
    }
}
