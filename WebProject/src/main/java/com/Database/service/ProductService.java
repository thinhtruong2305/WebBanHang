package com.Database.service;

import java.util.List;
import java.util.Optional;

import com.Database.entity.Product;

import org.springframework.data.domain.Page;


public interface ProductService {

    Page<Product> getListProduct(Integer page,Integer size);

    Page<Product> getListDiscount();

    Product getProductbyId(long id);

	Product updateProduct(Product product);

	long count();

	Product saveProduct(Product product);

	Product getProductById(Long id);

	Optional<Product> findById(Long id);

	void deleteById(Long id);

	void delete(Product entity);

	List<Product> getListProduct();
}
