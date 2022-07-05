package com.Controller.Client.Product;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.Database.DTO.CartItem;
import com.Database.entity.Product;
import com.Database.service.IShoppingCartService;
import com.Database.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Client/Product")
public class ProductController {
	@Autowired
	IShoppingCartService shoppingCartService;
    @Autowired
    ProductService productService;

    @GetMapping("/Shop")
    public String shop(@RequestParam(name="page",required = false,defaultValue = "0") Integer page,
    		@RequestParam(name="size",required = false,defaultValue = "12") Integer size,Model model){
        Page<Product> list = productService.getListProduct(page, size);
        Page<Product> listDiscount = productService.getListDiscount();
        model.addAttribute("list",list);
        model.addAttribute("listDiscount",listDiscount);
        int totalPages = list.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "Client/shop";
    }

    @GetMapping("/Shopdetail")
    public String shopDetail(@RequestParam long id, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
    	Product product = productService.getProductbyId(id);
        CartItem item = new CartItem(product, 1);
        model.addAttribute("item", item);
        model.addAttribute("imgUrl",product.getPhotosImagePath());
        session.setAttribute("product", product);
        return "Client/shopdetail";
    }
}
