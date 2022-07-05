package com.Controller.Client.ShoppingCart;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.Config.PaypalPaymentIntent;
import com.Config.PaypalPaymentMethod;
import com.Database.DTO.CartItem;
import com.Database.entity.Order;
import com.Database.entity.OrderDetail;
import com.Database.entity.Product;
import com.Database.entity.User;
import com.Database.service.IPaypalService;
import com.Database.service.IShoppingCartService;
import com.Database.service.IThinhProductService;
import com.Database.service.OrderDetailService;
import com.Database.service.OrderService;
import com.Database.service.UserService;
import com.Util.PaypalUtils;

@Controller
@RequestMapping("/Client/Payment")
public class PaymentController {
	public static final String URL_PAYPAL_SUCCESS = "Client/Payment/SuccessPaypal";
	public static final String URL_PAYPAL_CANCEL = "Client/Payment/CancelPay";
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private IPaypalService paypalService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private IShoppingCartService cartService;
	@Autowired
	private IThinhProductService productService;
	private Order order;
	@Autowired
	UserService userService;


	// PayByMoney
	@GetMapping("/PayCash")
	public String payByMoney(HttpServletRequest request) {
		if(request.getSession().getAttribute("cart") != null)
			return "Client/Payment/paycash";
		return "redirect:/Client/Product/Shop";
	}
	
	@GetMapping("/Recieve")
	public String recieveMoney(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user.getRole().equals("ROLE_ADMIN")) {
			saveAllToDatabase(request, "ĐÃ THANH TOÁN");
			return "redirect:/Client/Payment/Mail";
		}
		return "redirect:/Client/Payment/PayCash";
	}

	@GetMapping("/Delivery")
	public String delivery(HttpServletRequest request) {
		if(request.getSession().getAttribute("cart") != null) {
			saveAllToDatabase(request, "CHƯA THANH TOÁN VÀ CHƯA GIAO HÀNG");
			return "redirect:/Client/Payment/Mail";
		}
		return "redirect:/Client/Product/Shop";
	}

	// Paypal
	@GetMapping("/PayByPaypal")
	public String payByPaypal(HttpServletRequest request) {
		String cancelUrl = PaypalUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
		String successUrl = PaypalUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
		
		HttpSession session = request.getSession();
		if(session.getAttribute("cart") != null) {
			double price = (double) session.getAttribute("amount");
			try {
				Payment payment = paypalService.createPayment(
						PaypalUtils.chuyenDoiTienVietThanhDola(price), 
						"USD",
						PaypalPaymentMethod.paypal, 
						PaypalPaymentIntent.sale, 
						"payment description", 
						cancelUrl, successUrl);
				for (Links links : payment.getLinks()) {
					if (links.getRel().equals("approval_url")) {
						return "redirect:" + links.getHref();
					}
				}
			} catch (PayPalRESTException e) {
				log.error(e.getMessage());
			}
		}
		return "redirect:/Client/CheckOut/Show";
	}

	@GetMapping("/SuccessPaypal")
	public String successPaypal(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
			HttpServletRequest request) {
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				saveAllToDatabase(request, "ĐÃ THANH TOÁN");
				return "redirect:/Client/Payment/Mail";
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/Client/CheckOut/Show";
	}

	@GetMapping("/CancelPay")
	public String cancelPayMoney() {
		return "Client/Payment/cancelpay";
	}

	@GetMapping("/ContinueBuying")
	public String continueBuying(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("CheckLogin");
		return "redirect:/Client/Product/Shop";
	}
	
	@GetMapping("/Mail")
	public String mail() {
		try {
			List<OrderDetail> orderDetails = orderService.getByIdList(this.order.getOrderId());
			userService.sendThankyou(order.getUser(), this.order, orderDetails);
			return "Client/Payment/paysuccess";
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return "redirect:/Client/Product/Shop";
	}
	
	@GetMapping("/RemoveSession")
	public String removeSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		cartService.clear();
		session.removeAttribute("sizeCart");
		session.removeAttribute("amountVN");
		session.removeAttribute("amount");
		session.removeAttribute("phone");
		session.removeAttribute("address");
		session.removeAttribute("CheckLogin");
		session.removeAttribute("product");
		return "redirect:/Home";
	}
	
	@GetMapping("/Bill")
	public String bill(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("orderDetails", null);
		return "redirect:/Client/Payment/Bill/page/1";
	}
	
	@GetMapping("/Bill/page/{pageNumber}")
	public String showBillPage(HttpServletRequest request, @PathVariable int pageNumber, Model model) {
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("orderDetails");
		int pagesize = 5;
		List<OrderDetail> orderDetails = orderService.getByIdList(this.order.getOrderId());
		
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
		String baseUrl = "/Client/Payment/Bill/page/";
		
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("order", this.order);
		model.addAttribute("orderDetails", pages);
		return "Client/Payment/bill";
	}
	
	//Phần ngoài luồng
	public Collection<OrderDetail> addOrderDetail(Collection<CartItem> cartItem, Order order) {
		Collection<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		for (CartItem item : cartItem) {
			OrderDetail orderDetail = orderDetailService.saveOrderDetail(new OrderDetail(null, item.getQuantity(), item.getPrice(), order, item.getProduct()));
			orderDetails.add(orderDetail);
			editProduct(item.getQuantity(), item.getProduct().getProductId());
		}
		return orderDetails;
	}
	
	public void editProduct(int quantity, long id) {
		Product product = productService.getProductById(id);
		product.setQuantity(product.getQuantity() - quantity);
		productService.save(product);
	}

	@SuppressWarnings("unchecked")
	public void saveAllToDatabase(HttpServletRequest request, String status) {
		HttpSession session = request.getSession();
		if(status.equals("CHƯA THANH TOÁN VÀ CHƯA GIAO HÀNG")) {
			Collection<CartItem> cart = (Collection<CartItem>) session.getAttribute("cart");
			String phone = (String) session.getAttribute("phone");
			String address = (String) session.getAttribute("address");
			User user = (User) session.getAttribute("user");
			
			Order order = new Order(null, address, phone, status, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 500000000), null, user);
			Order orderSaved = orderService.saveOrder(order);
			this.order = orderSaved;
			addOrderDetail(cart, orderSaved);
			
		}else {
			Collection<CartItem> cart = (Collection<CartItem>) session.getAttribute("cart");
			String phone = (String) session.getAttribute("phone");
			String address = (String) session.getAttribute("address");
			User user = (User) session.getAttribute("user");
			
			Order order = new Order(null, address, phone, status, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), null, user);
			Order orderSaved = orderService.saveOrder(order);
			this.order = orderSaved;
			addOrderDetail(cart, orderSaved);
		}
	}
}
