package com.Database.service;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.mail.MessagingException;

import com.Database.entity.Order;
import com.Database.entity.OrderDetail;
import com.Database.entity.User;

import org.springframework.data.domain.Page;

public interface UserService {

	Page<User> getListUser(Integer page,Integer size);

	User getUserByEmail(String email);

	User getUserById(long id);

	void deleteUser(User user);

	void updateUser(User user);

	void register(User user, String url) throws UnsupportedEncodingException, MessagingException;

	void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

	boolean verify(String verificationCode);

	void sendThankyou(User user,Order order,Collection<OrderDetail> orderDetails) throws MessagingException, UnsupportedEncodingException; 
}
