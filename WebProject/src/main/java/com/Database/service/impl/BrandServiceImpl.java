package com.Database.service.impl;

import java.util.List;
import java.util.Optional;

import com.Database.entity.Brand;
import com.Database.repository.BrandRepository;
import com.Database.service.BrandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class BrandServiceImpl implements BrandService{

	@Autowired
	BrandRepository brandRepository;

	@Override
	public List<Brand> findAll() {
		
		return brandRepository.findAll();
	}

	@Override
	public Optional<Brand> findAllById(long id) {
		
		return brandRepository.findById(id);
	}
	
}
