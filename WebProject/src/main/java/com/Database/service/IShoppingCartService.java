package com.Database.service;

import java.util.Collection;

import com.Database.DTO.CartItem;

public interface IShoppingCartService {

	double getAmount();

	int size();

	Collection<CartItem> getAll();

	void clear();

	void updateItem(long productId, int quantity);

	void removeItem(long productId);

	void addItem(CartItem item);

}
