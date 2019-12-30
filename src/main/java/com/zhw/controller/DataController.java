package com.zhw.controller;

import com.zhw.pojo.CategoryInfoPO;
import com.zhw.pojo.PropertyPO;
import com.zhw.service.ICommonService;
import com.zhw.xxo.DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    public void init(){
        System.out.println("热部署完成");
    }

    /**
     * 因为使用和图片一起上传，前端属性要一个个fromdata置入
     * @param name
     * @param categoryBean
     * @param image
     * @param request
     * @return
     */
    @PostMapping(value = "/category")
    public DataVO saveCategory(CategoryInfoPO categoryBean, @RequestParam("image") MultipartFile image,HttpServletRequest request){
        try {
            categoryBean.setCreateTime(new Date());
            Integer POId = iCommonService.saveOrUpdateCategory(categoryBean);
            iCommonService.saveImage(POId,image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataVO("Ok");
    }

    @GetMapping(value = "/categoryList")
    public DataVO getCategoryList( ){
        return new DataVO(iCommonService.listCategory());
    }

    @GetMapping( "/categoryPage")
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
    @GetMapping("/propertyPage")
    public DataVO  listProperty(int id,int pageIndex,int size){
        return new DataVO(iCommonService.pageProperty(id,pageIndex,size));
    }

    /**
     * 没有了图片属性就可以直接用bean置入了
     * @return
     */
    @PostMapping("/property")
    public DataVO  saveProperty( PropertyPO property,@RequestParam("categoryId") Integer categoryId){
        CategoryInfoPO categoryInfoPO = new CategoryInfoPO();
        categoryInfoPO.setId(categoryId);
        property.setCategoryInfoPO(categoryInfoPO);
        return new DataVO(iCommonService.saveProperty(property));
    }
    @DeleteMapping("/property")
    public DataVO  deleteProperty( PropertyPO property){
        iCommonService.deleteProperty(property);
        return new DataVO("删除成功");
    }

}
