package com.Database.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Database.entity.Product;
import com.Database.repository.ProductRepository;
import com.Database.service.IThinhProductService;

@Service
public class ThinhProductService implements IThinhProductService{
	@Autowired
	ProductRepository productRepository;
	
	@Override
	public List<Product> getAll(){
		return productRepository.findAll();
	}
	
	@Override
	public Product getProductById(long id) {
		Optional<Product> product = productRepository.findById(id);
		return product.get();
	}
	
	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}
}
