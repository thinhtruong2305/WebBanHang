package com.Database.service;

import java.util.List;

import com.Database.entity.Product;

public interface IThinhProductService {

	List<Product> getAll();

	Product getProductById(long id);

	Product save(Product product);

}
