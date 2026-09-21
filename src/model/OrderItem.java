package model;

import interfaces.IStorable;

public class OrderItem implements IStorable {
	private MenuItem item;
	
	private int quantity;
	
	
	// constructor
	public OrderItem(MenuItem item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}
	

	public double getSubtotal() {
		if (this.item == null) {
			return 0.0;
		}
		
		return this.item.getDiscountedPrice() * this.quantity;
	}
	
	public void displayItem() {
        System.out.println(item.getItemName() + " x " + quantity + " = VNĐ" + getSubtotal());
    }
	
	@Override
    public String toFileString() {
		// Định dạng: <itemID>:<quantity>
        return item.getItemID() + ":" + quantity;
    }
    
    @Override
    public void fromFileString(String line) {
		// "<itemID>:<quantity>" — item sẽ được ánh xạ lại bởi OrderManager
		String[] st = line.split(":");
		this.quantity = Integer.parseInt(st[1]);
    }
	
	
	// setter
	public void setItem(MenuItem item) {
		this.item = item;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	// getter 
	public MenuItem getItem() {
		return this.item;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
}
