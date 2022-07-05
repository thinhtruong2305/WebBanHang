package com.Database.service.impl;

import com.Database.entity.OrderDetail;
import com.Database.repository.OrderDetailRepository;
import com.Database.service.OrderDetailService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements OrderDetailService{
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Override
	public List<OrderDetail> getAll(){
		return orderDetailRepository.findAll();
	}
	
	@Override
	public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
		return orderDetailRepository.save(orderDetail);
	}
}
