package com.Database.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

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



@Entity
@Table(name = "Products")
public class Product implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@Column(name = "product_name",columnDefinition = "nvarchar(100) not null")
	private String name;

	@Column(columnDefinition="Decimal(10,2) default '100.00'")
	private float price;

	@Column(columnDefinition = "integer default 0")
	private int quantity;

	@Column(length = 100, columnDefinition = "nvarchar(50) not null")
	private String unit;

	@Column(length = 100, columnDefinition = "nvarchar(255) null")
	private String image;

	@Column(columnDefinition = "integer default 0")
	private int views;

	@Column(length = 100, columnDefinition = "nvarchar(max) null")
	private String description;

	@Column(columnDefinition = "integer default 0")
	private int discount;

	@Column(nullable = false)
	private Date creationDate;
	
	@ManyToOne
	@JoinColumn(name= "categoryId")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name= "brandId")
	private Brand brand;
	
	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL)
	private Set<OrderDetail> orderDetails;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	
	public Product() {
		super();
	}

	public Product(Long productId, String name, float price, int quantity, String unit, String image, int views,
			String description, int discount, Date creationDate, Category category, Brand brand,
			Set<OrderDetail> orderDetails) {
		super();
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.unit = unit;
		this.image = image;
		this.views = views;
		this.description = description;
		this.discount = discount;
		this.creationDate = creationDate;
		this.category = category;
		this.brand = brand;
		this.orderDetails = orderDetails;
	}
	
	@Transient
    public String getPhotosImagePath() {
        if (image == null) return null;
         
        return "/Client/img/product/images/" + image;
    }

}
