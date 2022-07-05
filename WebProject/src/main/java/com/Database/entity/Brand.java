package com.Database.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;




@Entity
@Table(name = "Brands")
public class Brand implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long brandId;

	@Column(name = "brand_name", columnDefinition = "nvarchar(100) not null")
	private String name;

	@Column(columnDefinition = "nvarchar(255) null")
	private String logo;
	
	@OneToMany(mappedBy = "brand",cascade = CascadeType.ALL)
	private Set<Product> products;

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	

	public Brand() {
		super();
	}

	public Brand(Long brandId, String name, String logo, Set<Product> products) {
		super();
		this.brandId = brandId;
		this.name = name;
		this.logo = logo;
		this.products = products;
	}
	
	
}
