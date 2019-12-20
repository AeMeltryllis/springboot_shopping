package com.zhw.controller;

import com.zhw.service.ICommonService;
import com.zhw.util.DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;

@RestController
public class DataController {
    @Autowired(required = false)
    ICommonService iCommonService;

    @ResponseBody
    @GetMapping(value = "/category")
    public DataVO getCategory(int id){
        return new DataVO(iCommonService.getCategory(id));
    }
    @ResponseBody
    @GetMapping(value = "/categoryList")
    public DataVO getCategoryList( ){
        return new DataVO(iCommonService.listCategory());
    }


}
