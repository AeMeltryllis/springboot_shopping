package com.zhw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;

@Controller
public class PageController {

    @GetMapping("/admin")
    public String admin(){
        return "redirect:/listCategory";
    }

    @GetMapping("/listCategory")
    public String listCategory(){
        return "admin/category/listCategory";
    }

    @GetMapping("/editCategory")
    public String editCategory(Model model,@RequestParam("id") int id ){
        model.addAttribute("id",id);
        //return "redirect:admin/listCategory";
        return "admin/category/editPage";
    }
    //转跳到属性页
    @GetMapping("/propertyList")
    public String goProperty(Model model,@RequestParam("categoryId") int categoryId ){
        model.addAttribute("id",categoryId);
        return "admin/property/listProperty";
    }



}
