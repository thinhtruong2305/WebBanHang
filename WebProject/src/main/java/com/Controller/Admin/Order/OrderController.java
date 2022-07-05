package com.Controller.Admin.Order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Database.entity.Order;
import com.Database.service.OrderService;

@Controller
@RequestMapping("/Admin/Order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	private Sort sort = Sort.by("deliveryDate").ascending();
	
	@GetMapping("/Show")
	public String showOrder(Model model,HttpServletRequest request
			,RedirectAttributes redirect) {
		request.getSession().setAttribute("orders", null);
		if(model.asMap().get("success") != null)
			redirect.addFlashAttribute("success",model.asMap().get("success").toString());
		return "redirect:/Admin/Order/Show/page/1";
	}
	
	@GetMapping("Show/page/{pageNumber}")
	public String showOrderPage(HttpServletRequest request, @PathVariable int pageNumber, Model model) {
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("orders");
		int pagesize = 10;
		List<Order> list = orderService.getAllSort(sort);
		
		if (pages == null) {
			//Khởi tạo pageListHolder và set page size
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pagesize);
		} else {
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}
		request.getSession().setAttribute("orders", pages);
		
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/Admin/Order/Show/page/";
		
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("employees", pages);
		return "Admin/Order/showorders";
	}
	
	@PostMapping("/Save")
	public String saveOrder(@ModelAttribute("order")Order order, 
			RedirectAttributes redirect, BindingResult result, HttpServletRequest request) {
		if (result.hasErrors()) {
			long id = (long) request.getSession().getAttribute("id");
			return "redirect:/Admin/OrderDetail/Show/" + String.valueOf(id);
		}
		redirect.addFlashAttribute("success", "Saved order successfully!");
		orderService.saveOrder(order);
		return "redirect:/Admin/Order/Show";
	}
	
	@GetMapping("/ASC")
	public String ascList() {
		sort = Sort.by("deliveryDate").ascending();
		return "redirect:/Admin/Order/Show";
	}
	
	@GetMapping("/DES")
	public String desList() {
		sort = Sort.by("deliveryDate").descending();
		return "redirect:/Admin/Order/Show";
	}
}
