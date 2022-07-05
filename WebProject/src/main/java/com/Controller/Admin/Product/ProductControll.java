package com.Controller.Admin.Product;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Database.entity.Brand;
import com.Database.entity.Category;
import com.Database.entity.Product;
import com.Database.service.BrandService;
import com.Database.service.CategoryService;
import com.Database.service.ProductService;

@Controller
@RequestMapping("Admin/Product")
public class ProductControll implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	BrandService brandService;

	
	@GetMapping("/")
	public String index(Model model,HttpServletRequest request,RedirectAttributes redirect) {
		request.getSession().setAttribute("products", null);
		if(model.asMap().get("success") != null)
			redirect.addFlashAttribute("success",model.asMap().get("success").toString());
		return "redirect:page/1";
	}
	
	@GetMapping("page/{pageNumber}")
	public String showProductPage(HttpServletRequest request,
			@PathVariable int pageNumber, Model model) {
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("products");
		int pagesize = 5;
		List<Product> list = productService.getListProduct();
		System.out.println(list.size());	
		if (pages == null) {
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pagesize);
		} else {
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}
		request.getSession().setAttribute("products", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/Admin/Product/page/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("listproduct", pages);
		return "Admin/products/list";
	}
	

	@GetMapping("/Add")
	public String add(ModelMap model) {
		model.addAttribute("product", new Product());
		List<Category> list = categoryService.findAll();
		List<Brand> listbran = brandService.findAll();
		model.addAttribute("brands",listbran);
		model.addAttribute("categorys",list);
		return "Admin/products/addOrEdit";
	}

	@GetMapping("/Edit/{productId}")
	public String edit(@PathVariable Long productId, Model model) {
		Optional<Product> product = productService.findById(productId);
		model.addAttribute("categorys",categoryService.findAll());
		model.addAttribute("brands",brandService.findAll());
		if(product.isPresent()) {
			model.addAttribute("product",product.get());
			
		}else {
			model.addAttribute("product",new Product());
		}
		return "Admin/products/addOrEdit";
	}

	@PostMapping("/saveProduct")
	public String saveProduct(ModelMap model,@Validated @ModelAttribute("product") Product product, BindingResult result,
			@RequestParam("image") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
		    if(product.getProductId()!=null){
					Product productUpdate = productService.getProductById(product.getProductId());
			        product.setImage(productUpdate.getImage());
			}
			String id = request.getParameter("categorySelect");
			String idbran = request.getParameter("brandSelect");
		Optional<Category> categorySelect = categoryService.findAllById(Long.parseLong(id));
		if(categorySelect.isPresent())
			product.setCategory(categorySelect.get());
		Optional<Brand> brandSelect = brandService.findAllById(Long.parseLong(idbran));
		if(brandSelect.isPresent())
			product.setBrand(brandSelect.get());
		if(!multipartFile.isEmpty()){
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				product.setImage(fileName);
				Path uploadPath = Paths.get(System.getProperty("user.dir"),"/src/main/resources/static/Client/img/product/images/");
			if (Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try (InputStream inputStream = multipartFile.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				System.out.println(filePath.toFile().getAbsolutePath());
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				return "Admin/products/addOrEdit";
			}
		}
		productService.saveProduct(product);
		return "redirect:/Admin/Product/";
	}	
	
	@GetMapping("/Delete/{productId}")
	public String delete(@PathVariable("productId") Long productId,Model model) throws IOException {
		Optional<Product> product = productService.findById(productId);
		if(product.isPresent()) {
			if(product.get().getOrderDetails().size()>0){
				model.addAttribute("message","product has orders!");
				return "redirect:/Admin/Product/";
			}else {
				if(!product.get().getImage().isEmpty()){
					String curren = System.getProperty("user.dir");
					Path uploadPath = Paths.get(curren,product.get().getPhotosImagePath());
					Files.delete(uploadPath);
				}
				productService.deleteById(productId);
			}
			return "redirect:/Admin/Product/";	
		}
		return "redirect:/Admin/Product/";
	}

	@GetMapping("/Search/{categoryId}")
	public String search() {
		return "redirect:/Admin/products/search";
	}

}
