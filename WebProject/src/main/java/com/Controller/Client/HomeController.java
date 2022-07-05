package com.Controller.Client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import com.Database.entity.User;
import com.Database.service.MyUserDetail;

@Controller
public class HomeController {


	@GetMapping("/")
	public String Index() {

		return "redirect:/Home";
	}

	@GetMapping("/Home")
	public String hello(@AuthenticationPrincipal MyUserDetail userDetails, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String checkLogin = (String) session.getAttribute("CheckLogin");
		if(checkLogin == "yes") {
			User user = null;
			if(userDetails !=null){
				user = userDetails.getUser();
				session.setAttribute("user", user);
				return "redirect:/Client/CheckOut/ConfirmTheWayToPay";
			}
		}
		return "Client/home";
	}
	
	

}
