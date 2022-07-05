package com.Controller.Authentication;


import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import javax.servlet.http.HttpServletRequest;


import com.Database.entity.User;
import com.Database.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Authentication")
public class AuthenticationController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/Login")
    public String Login(){

        return "Authentication/login";
    }

    @GetMapping("/Register")
    public String Register(Model model){
        model.addAttribute("user", new User());
        return "Authentication/register";
    }

    @PostMapping("/Register")
    public String processRegister(User user, HttpServletRequest request, RedirectAttributes redirAttrs )
            throws UnsupportedEncodingException, MessagingException {
        User userExits = userService.getUserByEmail(user.getEmail());
        if(userExits != null){
            redirAttrs.addFlashAttribute("email_exits","The Email was registered");
            return "redirect:/Authentication/Register";
        }
        String passwordRepeat=(String) request.getParameter("password_repeat");
        if(!passwordRepeat.equals(user.getPassword())){
            redirAttrs.addFlashAttribute("password_error","Password not the same");
            return "redirect:/Authentication/Register";
        }
        userService.register(user, getSiteURL(request));       
        return "Authentication/register_success";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    } 

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code,Model model) {
        if (userService.verify(code)) {
            model.addAttribute("verify", "success");
        }
        return "Authentication/verify";
    }

    @GetMapping("/error403")
	public String error403(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("User", authentication);
		return "Authentication/403";
	}

    @GetMapping("/RequestOTP")
    public String RequestOTP(){

        return "Authentication/requestotp";
    }
}
