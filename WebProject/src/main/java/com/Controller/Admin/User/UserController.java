package com.Controller.Admin.User;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;


import com.Database.entity.User;
import com.Database.service.MyUserDetail;
import com.Database.service.OrderService;
import com.Database.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/Admin/User")
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	OrderService orderService;

	@GetMapping("/List")
	public String listUser(@RequestParam Integer page,@RequestParam Integer size,Model model) {
		Page<User> list = userService.getListUser(page, size);
		model.addAttribute("list",list);
		int totalPages = list.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		return "fragments/Admin/usertbody::usertbody";
	}

	@GetMapping("/ViewUser")
	public String viewUser(){
		return "Admin/User/user";
	}

	@GetMapping("/Update")
	public String updateUser(@RequestParam("id") long id,ModelMap model,RedirectAttributes redirectAttributes) {
		try{
			User user = userService.getUserById(id);
			if(user.getEmail()!=null){
				model.addAttribute("user",user);
				return "Admin/User/userupdate";
			}
			redirectAttributes.addFlashAttribute("action","Warning!");
			redirectAttributes.addFlashAttribute("message"," user not exits");
			return "redirect:/Admin/User/ViewUser";
		}
		catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("action","Warning!");
			redirectAttributes.addFlashAttribute("message"," user not exits");
			return "redirect:/Admin/User/ViewUser";
		}
	}

	@PostMapping("/Update")
	public String updateUser(User user,RedirectAttributes redirectAttributes) {
		try{
			User userUpdate=userService.getUserById(user.getUserId());
			if(userUpdate.getDisplayName()!=null){
				userUpdate.setDisplayName(user.getDisplayName());
				userUpdate.setRole(user.getRole());
				userUpdate.setEnable(user.isEnable());
				userService.updateUser(userUpdate);
				redirectAttributes.addFlashAttribute("action","Success");
				redirectAttributes.addFlashAttribute("message"," username: "+user.getEmail() +" updated");
				return "redirect:/Admin/User/ViewUser";
			}
			redirectAttributes.addFlashAttribute("action","Error!");
			redirectAttributes.addFlashAttribute("message"," failure");
			return "redirect:/Admin/User/ViewUser";
		}catch(EntityNotFoundException e){
			redirectAttributes.addFlashAttribute("action","Error!");
			redirectAttributes.addFlashAttribute("message"," failure");
			return "redirect:/Admin/User/ViewUser";
		}
		
	}
	@GetMapping("/DeleteOrder")
	public String deleteOrder(@RequestParam("id") long id,HttpServletRequest request){
		orderService.deleteOrder(id);
		return getPreviousPageByRequest(request).orElse("/");
	}
	protected Optional<String> getPreviousPageByRequest(HttpServletRequest request)
	{
   		return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
	}			

	@GetMapping("/Delete")
	public String deleteUser(@RequestParam("id") long id,ModelMap model,@AuthenticationPrincipal MyUserDetail userDetail,RedirectAttributes redirectAttributes) {
		try {
			User user = userService.getUserById(id);
			if(user.getEmail()!=null){
			if(userDetail.getUser().getEmail().equals(user.getEmail())){
				redirectAttributes.addFlashAttribute("action","Warning!");
				redirectAttributes.addFlashAttribute("message"," not delete yourself");
				return "redirect:/Admin/User/ViewUser";
			}
			if(user.getOrders().size() > 0){
				redirectAttributes.addFlashAttribute("action","Error!");
				redirectAttributes.addFlashAttribute("message",user.getEmail()+" ordered");
				return "redirect:/Admin/User/ViewUser";
			}
				redirectAttributes.addFlashAttribute("action","Success");
				redirectAttributes.addFlashAttribute("message",user.getEmail()+" deleted");
			userService.deleteUser(user);
			return "redirect:/Admin/User/ViewUser";
		}
			redirectAttributes.addFlashAttribute("action","Warning!");
			redirectAttributes.addFlashAttribute("message"," user not exits");
			return "redirect:/Admin/User/ViewUser";
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("action","Warning!");
			redirectAttributes.addFlashAttribute("message"," user not exits");
			return "redirect:/Admin/User/ViewUser";
		}
	}
}
