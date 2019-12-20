package com.zhw.controller;

import com.zhw.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DataController {
    @Autowired(required = false)
    ICommonService iCommonService;

    @ResponseBody
    @GetMapping(value = "/test")
    public void test(){
        //iCommonService.testDAO();
        System.out.println("进入了");
    }
}
