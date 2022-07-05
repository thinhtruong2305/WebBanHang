package com.Database.service;

import java.util.List;

import com.Database.entity.OrderDetail;

public interface OrderDetailService {

	OrderDetail saveOrderDetail(OrderDetail orderDetail);

	List<OrderDetail> getAll();

}
