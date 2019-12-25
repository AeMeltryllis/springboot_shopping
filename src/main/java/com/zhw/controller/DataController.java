package com.zhw.controller;

import com.zhw.pojo.CategoryInfoPO;
import com.zhw.service.ICommonService;
import com.zhw.xxo.DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class DataController {
    @Autowired(required = true)
    ICommonService iCommonService;

    @GetMapping(value = "/category")
    public DataVO getCategory(int id){
        return new DataVO(iCommonService.getCategory(id));
    }

    @PostMapping(value = "/category")
    public DataVO saveCategory(CategoryInfoPO bean, @RequestParam("image") MultipartFile image,HttpServletRequest request){
        try {
            bean.setCreateTime(new Date());
            Integer POId = iCommonService.addCategory(bean);
            iCommonService.saveImage(POId,image,request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataVO("Ok");
    }

    @GetMapping(value = "/categoryList")
    public DataVO getCategoryList( ){
        return new DataVO(iCommonService.listCategory());
    }

    @GetMapping(value = "/categoryPage")
    public DataVO getCategoryPage(int pageIndex,int size){
        return new DataVO(iCommonService.pageCategory(pageIndex,size));
    }

    @RequestMapping  (value = "/category/{id}" ,method = RequestMethod.DELETE)
    public DataVO deleteCategory(@PathVariable("id") int id){
        try {
            iCommonService.deleteCategory(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataVO("删除成功");

    }





}
