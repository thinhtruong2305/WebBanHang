package com.Database.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.Database.entity.Order;
import com.Database.entity.OrderDetail;
import com.Database.entity.User;
import com.Database.repository.UserRepository;
import com.Database.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository ;

	@Autowired
    private PasswordEncoder passwordEncoder;
     
    @Autowired
    private JavaMailSender mailSender;

	@Override
	public Page<User> getListUser(Integer page,Integer size) {
		Pageable pageable = PageRequest.of(page,size);

		return userRepository.findAll(pageable);
	}

	@Override
	public User getUserByEmail(String email) {
		User user = userRepository.getUserByEmail(email);
		return user;
	}

	@Override
	public void register(User user, String url) throws UnsupportedEncodingException, MessagingException {
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			 
			String randomCode = RandomString.make(64);
			user.setOtpCode(randomCode);
			user.setEnable(false);
			 
			userRepository.save(user);
			 
			sendVerificationEmail(user, url);
		
	}

	@Override
	public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
			String toAddress = user.getEmail();
			String fromAddress = "shopjavaweb@gmail.com";
			String senderName = "Shop Java Web Welcome";
			String subject = "Please verify your registration";
			String content = "Dear [[name]],<br>"
					+ "Please click the link below to verify your registration:<br>"
					+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
					+ "Thank you,<br>"
					+ "Shop JAVA";
			 
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			 
			helper.setFrom(fromAddress, senderName);
			helper.setTo(toAddress);
			helper.setSubject(subject);
			 
			content = content.replace("[[name]]", user.getDisplayName());
			String verifyURL = siteURL + "/Authentication/verify?code=" + user.getOtpCode();
			 
			content = content.replace("[[URL]]", verifyURL);
			 
			helper.setText(content, true);
			 
			mailSender.send(message);
		}

	@Override
	public boolean verify(String verificationCode) {
		User user = userRepository.findByVerificationCode(verificationCode);
    	if (user == null || user.isEnable()) 
		{
        	return false;
    	} 
		else 
		{
        	user.setOtpCode(null);
        	user.setEnable(true);
			user.setRole("ROLE_USER");
        	userRepository.save(user);
         
        	return true;
    	}
	}

	@Override
	public User getUserById(long id) {
		User user = userRepository.getById(id);
		return user;
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	@Override
	public void updateUser(User user) {
		userRepository.save(user);
	}

	@Override
	public void sendThankyou(User user, Order order, Collection<OrderDetail> orderDetails)
			throws MessagingException, UnsupportedEncodingException {
				String toAddress = user.getEmail();
				String fromAddress = "shopjavaweb@gmail.com";
				String senderName = "Shop Java Web Thank you";
				String subject = "Thank you for your Order";
				String content = "Dear [[name]],<br>"
						+"Your order with id: "+order.getOrderId()+"<br>"
						+"With infor <br>"
						+"Address: "+order.getAddress()+"<br>"
						+"Phone: "+order.getPhone()+"<br>"
						 + "<h2>List of Order detail</h2>"
						 + "<table border = 1>"
						 +"<thead bgcolor=#b9c9fe >"
							+"<th>Tên sản phẩm</th>"
							+"<th>Số lượng</th>"
							+"<th>Tổng tiền</th>"
						+"</thead>"
						+"<tbody>";
				for(OrderDetail orderDetail : orderDetails){
						content+= "<tr><td>"+orderDetail.getProduct().getName()+"</td>"
								  +"<td>"+orderDetail.getQuantity()+"</td>"
								  +"<td>"+orderDetail.getTotal()+"</td></tr>";
				}
				 content+="</tbody>"
						+"</table>"
						+ "Thank you,<br>"
						+ "Shop JAVA";
						
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);
				 
				helper.setFrom(fromAddress, senderName);
				helper.setTo(toAddress);
				helper.setSubject(subject);
				 
				content = content.replace("[[name]]", user.getDisplayName());
				 
				helper.setText(content, true);
				 
				mailSender.send(message);
		
	}	
}
