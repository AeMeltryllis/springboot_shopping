package com.zhw.controller;

import com.zhw.pojo.CategoryInfoPO;
import com.zhw.pojo.ProductImagePO;
import com.zhw.pojo.ProductPO;
import com.zhw.pojo.PropertyPO;
import com.zhw.service.ICommonService;
import com.zhw.xxo.DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class DataController {
    @Autowired(required = true)
    ICommonService iCommonService;

    @GetMapping(value = "/category")
    public DataVO getCategory(int id) {
        return new DataVO(iCommonService.getCategory(id));
    }

    @PostConstruct
    public void init() {
        System.out.println("热部署完成");
    }

    /**
     * 因为使用和图片一起上传，前端属性要一个个fromdata置入
     *
     * @param name
     * @param categoryBean
     * @param image
     * @param request
     * @return
     */
    @PostMapping(value = "/category")
    public DataVO saveCategory(CategoryInfoPO categoryBean, @RequestParam("image") MultipartFile image, HttpServletRequest request) {
        try {
            categoryBean.setCreateTime(new Date());
            Integer POId = iCommonService.saveOrUpdateCategory(categoryBean);
            iCommonService.saveImage(POId, image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataVO("Ok");
    }

    @GetMapping(value = "/categoryList")
    public DataVO getCategoryList() {
        return new DataVO(iCommonService.listCategory());
    }

    @GetMapping("/categoryPage")
    public DataVO getCategoryPage(int pageIndex, int size) {
        return new DataVO(iCommonService.pageCategory(pageIndex, size));
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
    public DataVO deleteCategory(@PathVariable("id") int id) {
        try {
            iCommonService.deleteCategory(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataVO("删除成功");

    }

    @GetMapping("/propertyPage")
    public DataVO listProperty(int id, int pageIndex, int size) {
        return new DataVO(iCommonService.pageProperty(id, pageIndex, size));
    }

    /**
     * 没有了图片属性就可以直接用bean置入了
     *
     * @return
     */
    @PostMapping("/property")
    public DataVO saveAndUpdateProperty(PropertyPO property, @RequestParam("categoryId") int categoryId) {
        CategoryInfoPO categoryInfoPO = new CategoryInfoPO();
        categoryInfoPO.setId(categoryId);
        property.setCategoryInfoPO(categoryInfoPO);
        return new DataVO(iCommonService.saveAndUpdateProperty(property));
    }

    //@DeleteMapping("/property/{id}")  DeleteMapping无法接受ajax类型的参数
    @RequestMapping(value = "/property/{id}", method = RequestMethod.DELETE)
    public DataVO deleteProperty(@PathVariable("id") int id) {
        PropertyPO pPO = iCommonService.getProperty(id);
        iCommonService.deleteProperty(pPO);
        return new DataVO("删除成功");
    }

    @GetMapping("/property")
    public DataVO getProperty(int id) {
        PropertyPO property = iCommonService.getProperty(id);
        return new DataVO(property, "获取成功");
    }


    @GetMapping("/productPage")
    public DataVO pageProduct(@RequestParam("id") int categoryId, int pageIndex, int size) {
        Page page = iCommonService.pageProduct(categoryId, pageIndex, size);
        //CategoryInfoPO categoryPO = iCommonService.getCategory(categoryId);
        //Map map = new HashMap();
        //map.put("categoryId",categoryPO.getId());
        //map.put("categoryName",categoryPO.getName());
        return new DataVO(page, "分页获取成功");
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public DataVO deletdProduct(@PathVariable("id") int id) {
        //ProductPO product = iCommonService.getProduct(id);= iCommonService.getProduct(id);
        iCommonService.deleteProduct(id);
        return new DataVO("删除成功");
    }

    @PostMapping(value = "/product")
    public DataVO saveOrUpdateProduct(ProductPO productPO, @RequestParam("categoryId") int categoryId) {
        CategoryInfoPO category = new CategoryInfoPO();
        category.setId(categoryId);
        productPO.setCategoryInfoPO(category);
        Integer integer = iCommonService.saveAndUpdateProduct(productPO);
        return new DataVO("保存成功");
    }

    @GetMapping("/product")
    public DataVO getProduct(int id) {
        ProductPO product = iCommonService.getProduct(id);
        return new DataVO(product);
    }

    /**
     * 根据产品id和类型取出图片
     * @param type
     * @param productId
     * @return
     * @throws Exception
     */
    @GetMapping("/productImages/{productId}")
    public DataVO list(@RequestParam("type") String type, @PathVariable("productId") int productId) throws Exception {
        ProductPO pProductPO = iCommonService.getProduct(productId);
        List<ProductImagePO> productImagePOs = iCommonService.listProductImages(pProductPO, type);
        return new DataVO(productImagePOs);
    }

    /**
     * 删除图片
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    //@DeleteMapping("/productImage/{id}")
    @RequestMapping(value = "/productImage/{id}",method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") int id)  throws Exception {
        //先获取实体类
        ProductImagePO productImagePO = iCommonService.getProductImage(id);
        iCommonService.deleteProductImage(id);
        return null;
    }

    /**
     * 保存图片
     * @param pid
     * @param type
     * @param image
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/productImage")
    public DataVO addProductImage(@RequestParam("productId") int productId, @RequestParam("type") String type, MultipartFile image) throws Exception {
       //生成实体类
        ProductImagePO productImagePO = new ProductImagePO();
        ProductPO productPO = iCommonService.getProduct(productId);
        productImagePO.setProductPO(productPO);
        productImagePO.setType(type);
        //保存
        try {
            iCommonService.addProductImage(productImagePO,image);
        } catch (Exception e) {
            e.printStackTrace();
            return new DataVO("保存失败");
        }
        return new DataVO("保存成功");
    }




}
