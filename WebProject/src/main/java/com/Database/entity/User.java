package com.Database.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;



@Entity
@Table(name = "Users")
public class User implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotFound(action = NotFoundAction.IGNORE)
	private Long userId;
	
	@Column(columnDefinition = "nvarchar(255) not null")
	private String email;
	
	@Column(columnDefinition = "nvarchar(255) not null")
	private String password;
	
	@Column(columnDefinition = "nvarchar(255) not null")
	private String displayName;
	
	@Column(columnDefinition = "varchar(50) not null default 'ROLE_USER'")
	private String role;
	
	@Column(columnDefinition = "boolean default false")
	private boolean enable;
	
	@Column(nullable = true)
	private String otpCode;
	
	@Column(nullable = true)
	private Date requestOtp;

	@JsonIgnore
	@OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
	private Set<Order> orders;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getOtpCode() {
		return otpCode;
	}

	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

	public Date getRequestOtp() {
		return requestOtp;
	}

	public void setRequestOtp(Date requestOtp) {
		this.requestOtp = requestOtp;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	

	public User() {
		super();
	}

	public User(Long userId, String email, String password, String displayName, String role, boolean enable,
			String otpCode, Date requestOtp, Set<Order> orders) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.displayName = displayName;
		this.role = role;
		this.enable = enable;
		this.otpCode = otpCode;
		this.requestOtp = requestOtp;
		this.orders = orders;
	}
	
	
	
}
