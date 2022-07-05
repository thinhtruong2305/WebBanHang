package com.Controller.Client.ShoppingCart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Database.entity.User;
import com.Database.service.MyUserDetail;

@Controller
@RequestMapping("/Client/CheckOut")
public class CheckOutController {
	private String payway;
	
	@GetMapping("/Show")
	public String index(HttpServletRequest request) {
		if(request.getSession().getAttribute("cart") != null)
			return "Client/ShoppingCart/checkout";
		return "redirect:/Client/Product/Shop";
	}
	
	@GetMapping("/CheckLogin")
	public String getCheckLogin() {
		return "redirect:/Home";
	}
	
	@PostMapping("/CheckLogin")
	public String postCheckLogin(@AuthenticationPrincipal MyUserDetail userDetails,@ModelAttribute("address")String address, @ModelAttribute("phone")String phone, HttpServletRequest request) {
		User user = null;
		payway = (String)  request.getParameter("payment");
		HttpSession session = request.getSession();
		
		session.setAttribute("phone", phone);
		session.setAttribute("address", address);
		
		if(userDetails !=null){
			user = userDetails.getUser();
			session.setAttribute("user", user);
		}
		if(user == null){
			session.setAttribute("CheckLogin", "yes");
			return "redirect:/Authentication/Login";
		}
		return "redirect:/Client/CheckOut/ConfirmTheWayToPay";
	}
	
	@GetMapping("/ConfirmTheWayToPay")
	public String redirectPayment(HttpServletRequest request) {
		try {
			if(payway.equals("paypal"))
				return "redirect:/Client/Payment/PayByPaypal";
			if(payway.equals("paycash"))
				return "redirect:/Client/Payment/PayCash";
		}catch(Exception e) {
			
		}
		return "redirect:/Client/CheckOut/Show";
	}
}
