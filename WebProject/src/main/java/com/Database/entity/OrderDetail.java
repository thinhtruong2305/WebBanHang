package com.Database.entity;

import java.io.Serializable;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Orderdetails")
public class OrderDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderDetailId;
	
	@Column(nullable = false)
	private int quantity;
	
	@Column(nullable = true)
	private float total;
	
	@ManyToOne
	@JoinColumn(name= "orderId")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name= "productId")
	private Product product;

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	

	public OrderDetail() {
		super();
	}

	public OrderDetail(Long orderDetailId, int quantity, float total, Order order, Product product) {
		super();
		this.orderDetailId = orderDetailId;
		this.quantity = quantity;
		this.total = total;
		this.order = order;
		this.product = product;
	}
	
	
	
}
