package com.Database.service;

import java.util.List;
import java.util.Optional;

import com.Database.entity.Brand;



public interface BrandService {

    List<Brand> findAll();
	
	Optional<Brand> findAllById(long id);
}
