package model.menu;

import model.base.BaseEntity;

public abstract class MenuItem extends BaseEntity {
    private String name;
    private double price;
    private String description;

    public MenuItem() {
        super();
    }

    public MenuItem(String id, String name, double price, String description) {
        super(id);
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void changePrice(double newPrice) {
        if (newPrice > 0) {
            this.price = newPrice;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}
