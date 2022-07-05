package com.Database.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.Database.DTO.CartItem;

import com.Database.service.IShoppingCartService;

@Service
@SessionScope
public class ShoppingCartService implements IShoppingCartService{
	private Map<Long, CartItem> cart = new HashMap<Long, CartItem>();
	
	@Override
	public void addItem(CartItem item) {
		CartItem existedItem = cart.get(item.getProduct().getProductId());
		if(existedItem == null)
			cart.put(item.getProduct().getProductId(), item);
		else {
			int quantity = existedItem.getQuantity() + item.getQuantity();
			existedItem.setQuantity(quantity);
			existedItem.setPrice(existedItem.getPrice() * quantity);
		}
	}
	
	@Override
	public void removeItem(long productId) {
		cart.remove(productId);
	}
	
	@Override
	public void updateItem(long productId, int quantity) {
		CartItem item = cart.get(productId);
		item.setQuantity(quantity);
		item.setPrice(item.getPrice() * quantity);
		
		if(item.getQuantity() <= 0) {
			removeItem(productId);
		}
	}
	
	@Override
	public void clear() {
		cart.clear();
	}
	
	@Override
	public Collection<CartItem> getAll(){
		return cart.values();
	}
	
	@Override
	public int size() {
		return cart.values().size();
	}
	
	@Override
	public double getAmount() {
		return cart.values().stream().mapToDouble(item -> item.getPrice()).sum();
	}
}
