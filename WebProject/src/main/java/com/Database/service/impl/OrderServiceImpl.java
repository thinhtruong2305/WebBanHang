package com.Database.service.impl;

import com.Database.entity.Order;
import com.Database.entity.OrderDetail;
import com.Database.repository.OrderRepository;
import com.Database.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderRepository orderRepository;
	
	@Override
	public List<Order> getAll(){
		return orderRepository.findAll();
	}
	
	@Override
	public Page<Order> getAllPage(Pageable pageable){
		return orderRepository.findAll(pageable);
	}
	
	@Override
	public List<Order> getAllSort(Sort sort){
		return orderRepository.findAll(sort);
	}
	
	@Override
	public Order getById(long id){
		Optional<Order> order = orderRepository.findById(id);
		if(order.isPresent())
			return order.get();
		return null;
	}
	
	@Override
	public List<OrderDetail> getByIdList(long id){
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		Order order = getById(id);
		for(OrderDetail orderDetail : order.getOrderDetails()) {
			if(orderDetail != null)
				orderDetails.add(orderDetail);
		}
		return orderDetails;
	}
	
	@Override
	public Order saveOrder(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public void deleteOrder(long id) {
		orderRepository.delete(orderRepository.getById(id));
	}
}
