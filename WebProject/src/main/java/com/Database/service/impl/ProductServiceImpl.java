package com.Database.service.impl;

import java.util.List;
import java.util.Optional;

import com.Database.entity.Product;
import com.Database.repository.ProductRepository;
import com.Database.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	ProductRepository productRepository;

	@Override
	public Page<Product> getListProduct(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return productRepository.findAll(pageable);
	}

	@Override
	public Product getProductbyId(long id) {
		
		return productRepository.getById(id);
	}

	@Override
	public Page<Product> getListDiscount() {
		Pageable pageable = PageRequest.of(0, 6,Sort.by(Sort.Direction.DESC, "discount"));
		return productRepository.findAll(pageable);
	}

	@Override
	public List<Product> getListProduct() { 
		return productRepository.findAll(); 
		}
	

   

   @Override
   public Optional<Product> findById(Long id) {
	   return productRepository.findById(id);
   }



   @Override
   public long count() {
	   return productRepository.count();
   }

   

   @Override
   public void delete(Product entity) {
	   productRepository.delete(entity);
   }



   @Override
   public void deleteById(Long id) {
	   productRepository.deleteById(id);
   }



   @Override
   public Product saveProduct(Product product) {
	   return productRepository.save(product);
   }


   @Override
   public Product getProductById(Long listproduct) {
	   return productRepository.findById(listproduct).get();
   }
   

   @Override
   public Product updateProduct(Product product) {
	   return productRepository.save(product);
   }


	
}
