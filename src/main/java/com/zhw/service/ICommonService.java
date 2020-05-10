package com.zhw.service;

import com.zhw.pojo.*;
import com.zhw.xxo.DataVO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ICommonService {

    List<CategoryInfoPO> listCategory();

    CategoryInfoPO getCategory(int id);

    Page pageCategory(int pageIndex, int size);

    Integer saveOrUpdateCategory(CategoryInfoPO category);

    void saveImage(Integer categoryId, MultipartFile image) throws IOException;

    void deleteCategory(int id);

    /**
     * 分页分类属性
     *
     * @param categoryId
     * @param pageIndex
     * @param size
     * @return
     */
    Page<PropertyPO> pageProperty(int categoryId, int pageIndex, int size);

    Integer saveAndUpdateProperty(PropertyPO propertyPO);

    void deleteProperty(PropertyPO propertyPO);

    PropertyPO getProperty(int id);

    Integer saveAndUpdateProduct(ProductPO productPO);

    ProductPO getProduct(int id);

    Page pageProduct(int categoryId, int pageIndex, int size);
//前台获取分类方法
    List<CategoryInfoPO> homeCategory();
    void removeCategoryFromProduct(List<CategoryInfoPO> list);

    void deleteProduct(int id);

    List<ProductImagePO> listProductImages(ProductPO productPO, String type);

    ProductImagePO getProductImage(int id);
    void addProductImage(ProductImagePO productImagePO,MultipartFile image) throws Exception;
    void deleteProductImage(int id);
//    用户分页
    Page pageUser( int pageIndex, int size);
//    订单分页
    Page pageOrder(int pageIndex, int size);
//    保存更新订单接口
    Integer saveOrUpdateOrder(OrderInfoPO orderInfoPO);
//    获取单个id
    OrderInfoPO getAOrder(int id);
//用户检测
DataVO checkUser(UserPO userPO);
//获取单个用户信息
    UserPO getUserByNameAndPassword(String name, String password);
//    前端获取商品信息函数
    DataVO getProductInfoInFore(int productId);
//    商品搜索
List<ProductPO> searchProduct(String keyword);






}
