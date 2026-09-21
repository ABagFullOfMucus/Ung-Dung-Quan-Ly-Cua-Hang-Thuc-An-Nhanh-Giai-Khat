package model;

import interfaces.IDiscountable;
import interfaces.IStorable;

public abstract class MenuItem implements IStorable, IDiscountable {
	private String itemID;
	private String itemName;
	
	private double price;
	
	private int stock;
	
	private double discountRate;
	
	// constructor 
	
	public MenuItem() {
		
	}
	
	public MenuItem(String itemID, String itemName, double price, int stock) {
		this.itemID = itemID;
		this.itemName = itemName;
		this.price = price;
		this.stock = stock;
		this.discountRate = 0.0;
	}
	
	
	public abstract void displayInfo();
	
	// setter 
	
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	@Override
	public void setDiscountRate(double discountRate) {
		if (discountRate < 0.0) {
			discountRate = 0.0;
		} else if (discountRate > 1.0) {
			discountRate = 1.0;
		}
		
		this.discountRate = discountRate;
	}
	
	
	// getter
	
	public String getItemID() {
		return this.itemID;
	}
	
	public String getItemName() {
		return this.itemName;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public int getStock() {
		return this.stock;
	}
	
	@Override
	public double getDiscountRate() {
		return this.discountRate;
	}
	
	@Override
	public double getDiscountedPrice() {
		return this.price * (1 - this.discountRate);
	}
}
