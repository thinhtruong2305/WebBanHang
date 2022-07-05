package com.Controller.Admin.Categories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import com.Database.service.CategoryService;


import java.io.IOException;

import java.util.List;

import java.util.Optional;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.Database.entity.Category;
import com.Database.entity.Product;


@Controller
@RequestMapping("/Admin/Categories")
public class CategoriesController {
    @Autowired
    CategoryService categoryService;
    private Optional<Category> category;
    
    @GetMapping("/Add")
        public String getCatAdd(Model model){
        model.addAttribute("categories", new Category());
        return "Admin/categoriesAdd";
    }
    @PostMapping("/Save")
        public String saveCat(@ModelAttribute("categories") Category categories){
        categoryService.save(categories);
        return "redirect:/Admin/Categories/List";
        // return "Admin/categories/categoriesAdd";
    }
    @GetMapping ("/Delete/{id}")
    public String deleteCat(@PathVariable("id") Long id, ModelMap model) throws IOException{
        Optional<Category> category = categoryService.getById(id);
        List<Product> product = categoryService.getAllProductByIdCategory(id);
        if(category.isPresent()){
            //Nếu danh sách không rỗng thì không xóa
            if(!product.isEmpty()){
                
            }
            //Danh sách product rỗng thì xóa
            else{
                categoryService.removeById(id);
                
            }
            
        }
        return "redirect:/Admin/Categories/List";
  
        
    }

    @GetMapping ("/Update/{id}")
    public String updateCat(HttpServletRequest request, @PathVariable("id") Long id, Model model){
        HttpSession session = request.getSession();
        category = categoryService.getById(id);
        if(category.isPresent()){
            session.setAttribute("products", null);
            return "redirect:/Admin/Categories/Update/page/1";
        }else
            return "404";
    }

    @GetMapping("/Update/page/{pageNumber}")
	public String showCategoryUpdatePage(HttpServletRequest request, @PathVariable int pageNumber, Model model){
        PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("products");
        int pagesize = 5;
        List<Product> list = categoryService.getAllProductByIdCategory(category.get().getCategoryId());
        if (pages == null){
            pages = new PagedListHolder<>(list);
            pages.setPageSize(pagesize);
        }else{
            final int goToPage = pageNumber - 1;
            if (goToPage <= pages.getPageCount() && goToPage >= 0)
                pages.setPage(goToPage);
        }
        request.getSession().setAttribute("products", pages);
        int current = pages.getPage() + 1;
        int begin = Math.max(1, current - list.size());
        int end = Math.min(begin + 5, pages.getPageCount());
        int totalPageCount = pages.getPageCount();
        String baseUrl = "/Admin/Categories/Update/page/";
            
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("categories", category.get());
        model.addAttribute("productList", list);
        return "Admin/updateCate"; 
	}

	@GetMapping("/List")
	public String index(Model model,HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("categories", null);
        return "redirect:/Admin/Categories/List/page/1";
    } 
		
    @GetMapping("/List/page/{pageNumber}")
	public String showCategoryPage(HttpServletRequest request, @PathVariable int pageNumber, Model model){
        PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("categories");
        int pagesize = 3;
        List<Category> list =(List<Category>) categoryService.findAll();
        if (pages == null){
            pages = new PagedListHolder<>(list);
            pages.setPageSize(pagesize);
        }else{
            final int goToPage = pageNumber - 1;
            if (goToPage <= pages.getPageCount() && goToPage >= 0)
                pages.setPage(goToPage);
        }
        request.getSession().setAttribute("categories", pages);
        int current = pages.getPage() + 1;
        int begin = Math.max(1, current - list.size());
        int end = Math.min(begin + 5, pages.getPageCount());
        int totalPageCount = pages.getPageCount();
        String baseUrl = "/Admin/Categories/List/page/";
            
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("categories", pages);
        return "Admin/categories"; 
	}	
}
