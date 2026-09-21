package service;

import model.Drink;
import model.FastFood;
import model.MenuItem;
import utils.FileHandler;

public class MenuManager {
	private MenuItem[] items;
	
	private int count;
	
	// constructor
	public MenuManager() {
		this.items = new MenuItem[1];
		this.count = 0;
	}
	
	private void resize() {
		int l = items.length * 2;
		MenuItem[] tmp = new MenuItem[l];
		
		for (int i = 0; i < count; i++) {
			tmp[i] = items[i];
		}
		
		items = tmp;
	}
	
	public boolean addItem(MenuItem item) {
		if (findItem(item.getItemID()) != null) {
			return false;
		}
		
		if (count >= items.length) {
			resize();
		}
		
		items[count] = item;
		count += 1;
		return true;
	}
	
	public MenuItem findItem(String id) {
		for (int i = 0; i < count; i++) {
			if (items[i].getItemID().equals(id)) return items[i];
		}
		
		return null;
	}
	
	public boolean removeItem(String id) {
		int f = -1;
		for (int i = 0; i < count; i++) {
			if (items[i].getItemID().equals(id)) {
				f = i;
				break;
			}
		}
		
		if (f == -1) return false;
		
		for (int i = f; i < count - 1; i++) {
			items[i] = items[i + 1];
		}
		
		items[count - 1] = null;
		count -= 1;
		
		return true;
	}
	
	public void displayAllItems() {
		if (count == 0) {
			System.out.println("Danh sách trống!");
			return;
		}
		
		System.out.println("Danh sách sản phẩm: ");
		for (int i = 0; i < count; i++) {
			items[i].displayInfo();
		}
	}
	
	public MenuItem findMostExpensiveItem() {
		if (count == 0) {
			return null;
		}
		
		MenuItem f = items[0];
		for (int i = 1; i < count; i++) {
			if (items[i].getPrice() > f.getPrice()) {
				f = items[i];
			}
		}
		return f;
	}
	
	// Lưu / đọc file
	public void saveToFile(String fileName) {
		FileHandler.saveToFile(fileName, items, count);
	}
	
	public void loadFromFile(String fileName) {
		String[] lines = FileHandler.readLines(fileName);
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			String type = line.split(",", 2)[0];
			
			MenuItem item = null;
			if (type.equals("FastFood")) {
				item = new FastFood();
			} else if (type.equals("Drink")) {
				item = new Drink();
			}
			
			if (item != null) {
				item.fromFileString(line);
				addItem(item);
			}
		}
	}
	
	// getters
	public int getCount() {
		return this.count;
	}
	
	public MenuItem[] getItems() {
		return this.items;
	}
}
