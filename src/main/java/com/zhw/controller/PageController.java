package com.zhw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    //转跳到属性编辑页
    @GetMapping("/editProperty")
    public String goPropertyEdit(Model model,@RequestParam("id") int propertyId ){
        model.addAttribute("id",propertyId);
        return "admin/property/editPage";
    }

    //转跳到产品页
    @GetMapping("/productList")
    public String goProductList(Model model,@RequestParam("categoryId") int categoryId){
        model.addAttribute("id",categoryId);
        return "admin/product/listProduct";
    }
    //转跳到产品编辑页
    @GetMapping("/editProduct")
    public String goEditProductPage(Model model,@RequestParam("id") int productId){
        model.addAttribute("id",productId);
        return "admin/product/editPage";
    }

    //转跳到产品图片页
    @GetMapping("/productImageList")
    public String goProductImageList(Model model,@RequestParam("id") int productId){
        model.addAttribute("id",productId);
        return "admin/product/listProductImage";
    }



}
