package com.Controller.Client.ShoppingCart;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Database.DTO.CartItem;
import com.Database.entity.Product;
import com.Database.service.IShoppingCartService;
import com.Database.service.IThinhProductService;

@Controller
@RequestMapping("/Client/ShoppingCart")
public class ShoppingCartController {
	@Autowired
	IShoppingCartService shoppingCartService;
	@Autowired
	IThinhProductService productService;
	
	@GetMapping("/Show")
	public String showShoppingCart(HttpServletRequest request) {
		session(request);
		return "Client/ShoppingCart/shopping-cart";
	}
	
	@GetMapping("/Add/{id}")
	public String addItem(@PathVariable("id")Long id, HttpServletRequest request) {
		Product product = productService.getProductById(id);
		CartItem item = new CartItem(product, 1);
		shoppingCartService.addItem(item);
		session(request);
		return "redirect:/Client/Product/Shop";
	}
	
	@PostMapping("/AddDetail")
	public String addDetailItem(@ModelAttribute("item")CartItem item, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Product product = (Product) session.getAttribute("product");
		CartItem itemAdd = new CartItem(product, item.getQuantity());
		shoppingCartService.addItem(itemAdd);
		session(request);
		return "redirect:/Client/Product/Shopdetail?id="+product.getProductId().toString();
	}
	
	@GetMapping("/Delete/{id}")
	public String deleteItem(@PathVariable("id")Long id) {
		shoppingCartService.removeItem(id);
		return "redirect:/Client/ShoppingCart/Show";
	}
	
	@PostMapping("/Update")
	public String updateCart(@RequestParam("id")Long id, @RequestParam("quantity")Integer quantity) {
		shoppingCartService.updateItem(id, quantity);
		return "redirect:/Client/ShoppingCart/Show";
	}
	
	public void session(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Collection<CartItem> cart = shoppingCartService.getAll();
		int sizeCart = shoppingCartService.size();
		double amount = shoppingCartService.getAmount();
		session.setAttribute("cart", cart);
		session.setAttribute("sizeCart", sizeCart);
		session.setAttribute("amountVN", NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(amount));
		session.setAttribute("amount", amount);
	}
}
