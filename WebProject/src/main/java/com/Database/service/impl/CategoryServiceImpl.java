package com.Database.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import com.Database.repository.CategoryRepository;
import com.Database.service.CategoryService;
import com.Database.entity.Category;
import com.Database.entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Product> getAllProductByIdCategory(long id) {
        List<Product> products = new ArrayList<Product>();
        Optional<Category> category = getById(id);
        if(category.isPresent()){
            for(Product product : category.get().getProducts()){
                if(product != null)
                    products.add(product);
            }
        }
        return products;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void remove(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public void removeById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
   
  
	@Override
	public Optional<Category> getById(long id) {
		
		Optional<Category> category = categoryRepository.findById(id);
        return category;
    }

	
	@Override
	public Optional<Category> findAllById(long id) {
		return categoryRepository.findById(id);
	}

}
