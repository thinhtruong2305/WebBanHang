package com.Controller.Admin;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/Admin")
public class IndexController {
    
    @GetMapping("/Index")
    public String Index(){
        
        return "Admin/index";
    }
  
    

}
