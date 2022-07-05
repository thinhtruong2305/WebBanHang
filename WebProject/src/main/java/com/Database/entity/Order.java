package com.Database.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnore;





@Entity
@Table(name = "Orders")
public class Order implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	
	@Column(columnDefinition = "nvarchar(255) not null")
	private String address;
	
	@Column(length = 100)
	private String phone;
	
	@Column(columnDefinition = "nvarchar(100) not null")
	private String status;
	
	@Column(nullable = false)
	private Date creationDate;
	
	@Column(nullable = false)
	private Date deliveryDate;
	
	@OneToMany(mappedBy = "order" , cascade = CascadeType.ALL)
	private Collection<OrderDetail> orderDetails;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name= "userId")
	private User user;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Collection<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Collection<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
	public Order() {
		super();
	}

	public Order(Long orderId, String address, String phone, String status, Date creationDate, Date deliveryDate,
			Collection<OrderDetail> orderDetails, User user) {
		super();
		this.orderId = orderId;
		this.address = address;
		this.phone = phone;
		this.status = status;
		this.creationDate = creationDate;
		this.deliveryDate = deliveryDate;
		this.orderDetails = orderDetails;
		this.user = user;
	}
	
	
}
