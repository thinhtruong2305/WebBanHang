package com.Controller.Admin.OrderDetail;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Database.entity.Order;
import com.Database.entity.OrderDetail;
import com.Database.service.OrderService;

@Controller
@RequestMapping("/Admin/OrderDetail")
public class OrderDetailController {
	private Order order = new Order();
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/Show/{id}")
	public String showOrderDetail(HttpServletRequest request, @PathVariable("id")long id) {
		HttpSession session = request.getSession();
		order = orderService.getById(id);
		session.setAttribute("id", id);
		session.setAttribute("order", order);
		session.setAttribute("orderDetails", null);
		return "redirect:/Admin/OrderDetail/Show/page/1";
	}
	
	@GetMapping("Show/page/{pageNumber}")
	public String showOrderDetailPage(HttpServletRequest request, @PathVariable int pageNumber, Model model) {
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("orderDetails");
		int pagesize = 5;
		List<OrderDetail> orderDetails = orderService.getByIdList(order.getOrderId());
		
		if (pages == null) {
			//Khởi tạo pageListHolder và set page size
			pages = new PagedListHolder<>(orderDetails);
			pages.setPageSize(pagesize);
		} else {
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}
		request.getSession().setAttribute("orderDetails", pages);
		
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - orderDetails.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/Admin/OrderDetail/Show/page/";
		
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("statuses", getListStatus());
		model.addAttribute("order", order);
		model.addAttribute("orderDetails", pages);
		return "Admin/OrderDetail/showorderdetail";
	}
	
	public List<String> getListStatus() {
		List<String> listStatus = new ArrayList<String>();
		listStatus.add("CHƯA THANH TOÁN VÀ CHƯA GIAO HÀNG");
		listStatus.add("ĐÃ THANH TOÁN");
		listStatus.add("ĐANG GIAO HÀNG");
		listStatus.add("ĐÃ GIAO HÀNG THÀNH CÔNG");
		return listStatus;
	}
}
