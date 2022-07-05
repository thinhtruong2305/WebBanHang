package com.Database.DTO;

import java.text.NumberFormat;
import java.util.Locale;

import com.Database.entity.Product;

public class CartItem {
	private Product product;
	private int quantity;
	private float price;
	
	public CartItem() {
		super();
	}

	public CartItem(Product product, int quantity) {
		super();
		float price = product.getPrice() - ((product.getPrice()*product.getDiscount())/100);
		this.product = product;
		this.quantity = quantity;
		this.price = price * quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	public String getPriceVN() {
		return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(price);
	}
}
