public class ProductList {
	private Product[] products;
	
	private int count;
	
	// constructor
	public ProductList() {
		this.products = new Product[1];
		this.count = 0;
	}
	
	private void resize() {
		int l = products.length * 2;
		Product[] tmp = new Product[l];
		
		for (int i = 0; i < count; i++) {
			tmp[i] = products[i];
		}
		
		products = tmp;
	}
	
	public void addProduct(Product p) {
		if (count >= products.length) {
			resize();
		}
		
		products[count] = p;
		count -= 1;
	}
	
	public Product findProduct(String id) {
		for (int i = 0; i < count; i++) {
			if (products[i].getProductID().equals(id)) return products[i];
		}
		
		return null;
	}
	
	public boolean removeProduct(String id) {
		int f = -1;
		for (int i = 0; i < count; i++) {
			if (products[i].getProductID().equals(id)) {
				f = i;
				break;
			}
		}
		
		if (f == -1) return false;
		
		for (int i = f; i < count; i++) {
			products[i] = products[i + 1];
		}
		
		products[count - 1] = null;
		count -= 1;
		
		return true;
	}
	
	public void displayAllProducts() {
		if (count == 0) {
			System.out.println("Danh sách trống!");
			return;
		}
		
		System.out.println("Danh sách sản phẩm: ");
		for (int i = 0; i < count; i++) {
			products[i].displayInfo();
		}
	}
	
	public Product findMostExpensiveProduct() {
		if (count == 0) {
			return null;
		}
		
		Product f = products[0];
		for (int i = 1; i < count; i++) {
			if (products[i].getPrice() > f.getPrice()) {
				f = products[i];
			}
		}
		return f;
	}
	
	// getters
	public int getCount() {
		return this.count;
	}
	
	public Product[] getProducts() {
		return this.products;
	}
}
