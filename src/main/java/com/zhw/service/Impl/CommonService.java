package com.zhw.service.Impl;

import com.zhw.dao.*;
import com.zhw.pojo.*;
import com.zhw.service.ICommonService;
import com.zhw.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommonService implements ICommonService {


    public static final int PAGE_SIZE = 10;
    public static final String IMAGE_TYPE_SINGLE = "single";
    public static final String IMAGE_TYPE_DETAIL = "detail";

    public static final String WAIT_PAY = "waitPay";
    public static final String WAIT_DELIVERY = "waitDelivery";
    public static final String WAIT_COMFIRM = "waitConfirm";
    public static final String HAS_REVIEW = "waitReview";
    public static final String HAS_FINISH = "finish";
    public static final String HAS_DELETE = "delete";


    @Autowired
    ICategoryDO iCategoryDO;
    @Autowired
    IPropertyDO iPropertyDO;
    @Autowired
    IProductDO iProductDO;
    @Autowired
    IProductImageDO iProductImageDO;
    @Autowired
    IUserDO iUserDO;
    @Autowired IOrderItemDO iorderItemDO;
    @Autowired IOrderDO iorderDO;

    @Override
    public List<CategoryInfoPO> listCategory() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return iCategoryDO.findAll(sort);
    }

    @Override
    public CategoryInfoPO getCategory(int id) {
        return iCategoryDO.getById(id);
    }

    /**
     * 分类页的分类方法
     *
     * @param pageIndex
     * @param size
     * @return
     */
    @Override
    public Page pageCategory(int pageIndex, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        //Page类中的下标是从0开始，所以传进来的pageIndex要-1
        CategoryInfoPO temporaryPO = new CategoryInfoPO();
        temporaryPO.setDeleted(false);
        Pageable page = PageRequest.of(pageIndex - 1, size, sort);
        Example<CategoryInfoPO> example = Example.of(temporaryPO);
        return iCategoryDO.findAll(example, page);
    }

    /**
     * 保存分类
     *
     * @param category
     * @return
     */
    @Override
    public Integer saveOrUpdateCategory(CategoryInfoPO category) {
        if (category.getId() != null) {
            CategoryInfoPO pCategoryPO = iCategoryDO.getById(category.getId());
            if (pCategoryPO != null) {
                //进入这里代表数据库有这条数据
                pCategoryPO.setUpdateTime(new Date());
                pCategoryPO.setName(category.getName());
                CategoryInfoPO PO = iCategoryDO.save(pCategoryPO);
                return PO.getId();
            }
        } else {
            category.setDeleted(false);
            CategoryInfoPO PO = iCategoryDO.save(category);
            return PO.getId();
        }
        return null;
    }

    @Override
    public void saveImage(Integer categoryId, MultipartFile image) throws IOException {
        //这里获取的是targe
        String rootPath = ResourceUtils.getURL("classpath:static").getPath().replace("%20", " ").replace('/', '\\');
        File imagePath = new File(rootPath, "img/category");
        File file = new File(imagePath.getAbsolutePath(), categoryId + ".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        //转换成文件流
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        //将流写入
        ImageIO.write(img, ".jpg", file);
    }

    /**
     * 虽然叫删除，但是使用的是逻辑删除
     *
     * @param id
     */
    @Override
    public void deleteCategory(int id) {
        //t = temporary
        CategoryInfoPO tCategoryPO = iCategoryDO.getById(id);
        if (tCategoryPO != null) {
            tCategoryPO.setDeleted(true);
            iCategoryDO.save(tCategoryPO);
        } else {
        }

    }

    /**
     * 根据分类id获取分类属性列表
     *
     * @return
     */
    @Override
    public Page<PropertyPO> pageProperty(int categoryId, int pageIndex, int size) {
        CategoryInfoPO categoryPO = iCategoryDO.getById(categoryId);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        //Page类中的下标是从0开始，所以穿进来的pageIndex要-1
        Pageable page = PageRequest.of(pageIndex - 1, size, sort);
        Page<PropertyPO> byCategoryInfoPO = iPropertyDO.findByCategoryInfoPO(categoryPO, page);
        return byCategoryInfoPO;
    }

    /**
     * 保存编辑属性
     *
     * @param propertyPO
     * @return
     */
    @Override
    public Integer saveAndUpdateProperty(PropertyPO propertyPO) {
        if (propertyPO.getId() != null) {
            //如果id不为空则是编辑上传
            propertyPO.setUpdateTime(new Date());
        } else {
            propertyPO.setCreateTime(new Date());
        }
        //save 和 update同个函数
        PropertyPO save = iPropertyDO.save(propertyPO);
        return save.getId();
    }

    /**
     * 删除属性
     *
     * @param propertyPO
     */
    @Override
    public void deleteProperty(PropertyPO propertyPO) {
        iPropertyDO.delete(propertyPO);
    }

    /**
     * 获取单个属性
     *
     * @param id
     * @return
     */
    @Override
    public PropertyPO getProperty(int id) {
        return iPropertyDO.findById(id).get();
    }

    @Override
    public Integer saveAndUpdateProduct(ProductPO productPO) {
        if (productPO.getId() != null) {
            //如果id不为空则是编辑上传
            productPO.setUpdateTime(new Date());
        } else {
            productPO.setCreateTime(new Date());
        }
        ProductPO save = iProductDO.save(productPO);
        return save.getId();
    }

    @Override
    public ProductPO getProduct(int id) {
        return iProductDO.findById(id).get();
    }

    /**
     * 产品页分页代码
     *
     * @param categoryId
     * @param pageIndex
     * @param size
     * @return
     */
    @Override
    public Page pageProduct(int categoryId, int pageIndex, int size) {
        CategoryInfoPO categoryPO = iCategoryDO.getById(categoryId);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        ProductPO temporaryPO = new ProductPO();
        temporaryPO.setCategoryInfoPO(categoryPO);
        temporaryPO.setDeleted(false);
        Pageable page = PageRequest.of(pageIndex - 1, size, sort);
        Example<ProductPO> example = Example.of(temporaryPO);
        return iProductDO.findAll(example, page);
    }

    @Override
    public void deleteProduct(int id) {
        ProductPO productPO = iProductDO.findById(id).get();
        if (productPO != null) {
            productPO.setDeleted(true);
            iProductDO.save(productPO);
        } else {
        }

    }
    /*产品图片crud一套*/
    //添加图片
    public void addProductImage(ProductImagePO productImagePO,MultipartFile image) throws Exception{
        iProductImageDO.save(productImagePO);
            //String folderPath = "img/";
            String folderPath = ResourceUtils.getURL("classpath:static").getPath().replace("%20", " ").replace('/', '\\');
            //判断类型保存
            if(IMAGE_TYPE_SINGLE.equals(productImagePO.getType())){
                folderPath +="productImage/single";
            }
            else{
                folderPath +="productImage/detail";
            }
            File  imageFolderFile= new File(folderPath);
            //new 一个指定路径文件对象
            File file = new File(imageFolderFile,productImagePO.getId()+".jpg");
            String fileName = file.getName();
            if(!file.getParentFile().exists())
            {file.getParentFile().mkdirs();}
            //进行转换
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
            //如果是单个图片，转换成不同像素保存
        if(IMAGE_TYPE_SINGLE.equals(productImagePO.getType())){
            String imageFolder_small=folderPath+ "/productSingle_small";
            String imageFolder_middle=folderPath+ "/productSingle_middle";
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.getParentFile().mkdirs();
            f_middle.getParentFile().mkdirs();
            ImageUtil.resizeImage(file, 56, 56, f_small);
            ImageUtil.resizeImage(file, 217, 190, f_middle);
        }
    }

    /**
     * 删除图片照片数据
     * 并且删除实体图片
     * @param id
     */
    @Override
    public void deleteProductImage(int id) {
        iProductImageDO.deleteById(id);

        //String folder = "img/";
        //if(ProductImageService.type_single.equals(bean.getType()))
        //    folder +="productSingle";
        //else
        //    folder +="productDetail";
        //
        //File  imageFolder= new File(request.getServletContext().getRealPath(folder));
        //File file = new File(imageFolder,bean.getId()+".jpg");
        //String fileName = file.getName();
        //file.delete();
        //if(ProductImageService.type_single.equals(bean.getType())){
        //    String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
        //    String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
        //    File f_small = new File(imageFolder_small, fileName);
        //    File f_middle = new File(imageFolder_middle, fileName);
        //    f_small.delete();
        //    f_middle.delete();
        //}

    }

    @Override
    public ProductImagePO getProductImage(int id) {
        return iProductImageDO.findById(id).get();
    }
    public List<ProductImagePO> listSingleProductImages(ProductPO productPO) {
        return iProductImageDO.findByProductPOAndTypeOrderByIdDesc(productPO, IMAGE_TYPE_SINGLE);
    }
    public List<ProductImagePO> listDetailProductImages(ProductPO productPO) {
        return iProductImageDO.findByProductPOAndTypeOrderByIdDesc(productPO, IMAGE_TYPE_DETAIL);
    }

    /**
     * 为产品设置封面
     * @param productPO
     */
    public void setFirstProdutImage(ProductPO productPO) {
        List<ProductImagePO> singleImages = this.listSingleProductImages(productPO);
        //如果有图片
        if(!singleImages.isEmpty())
            productPO.setFirstImage(singleImages.get(0));
        else
            productPO.setFirstImage(new ProductImagePO()); //这样做是考虑到产品还没有来得及设置图片，但是在订单后台管理里查看订单项的对应产品图片。
    }

    /**
     * 遍历为产品设置封面
     * @param products
     */
    public void setFirstProdutImages(List<ProductPO> products) {
        for (ProductPO product : products)
            setFirstProdutImage(product);
    }

    /**
     * 判断返回不同类型的图片
     * @param productPO
     * @param type
     * @return
     */
    @Override
    public List<ProductImagePO> listProductImages(ProductPO productPO,String type){
        if (IMAGE_TYPE_DETAIL.equals(type)){
            return this.listDetailProductImages(productPO);
        }else if(IMAGE_TYPE_SINGLE.equals(type)){
            return this.listSingleProductImages(productPO);
        }
        else return new ArrayList<>();
    }
    @Override
    public Page pageUser( int pageIndex, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        UserPO temporaryPO = new UserPO();
//        temporaryPO.setDeleted(false);
        Pageable page = PageRequest.of(pageIndex - 1, size, sort);
        Example<UserPO> example = Example.of(temporaryPO);
        return iUserDO.findAll(example, page);
    }

//订单模块
public void fill(List<OrderInfoPO> orders) {
    for (OrderInfoPO order : orders)
        fill(order);
}
    public void fill(OrderInfoPO order) {
//        查询出所有
        List<OrderItemPO> orderItems = listByOrder(order);
//        总价格
        float totalPrice = 0;
//        总数
        int totalNumber = 0;
        for (OrderItemPO orderItem :orderItems) {
            totalPrice+=orderItem.getNumber()*orderItem.getProductPO().getPromotePrice();
            totalNumber+=orderItem.getNumber();
            this.setFirstProdutImage(orderItem.getProductPO());
        }
//        统合订单数据
//        order.(total);
//        order.setOrderItems(orderItems);
//        order.setTotalNumber(totalNumber);
    }

    /**
     * 获取全部订单信息
     * @param order
     * @return
     */
    public List<OrderItemPO> listByOrder(OrderInfoPO item) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return IOrderItemDO.findAll(sort);


    }

    /**
     * 订单分页
     */
    @Override
    public Page pageOrder(int pageIndex, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        OrderInfoPO temporaryPO = new OrderInfoPO();
//        temporaryPO.setDeleted(false);
        Pageable page = PageRequest.of(pageIndex - 1, size, sort);
        Example<OrderInfoPO> example = Example.of(temporaryPO);
        return iorderDO.findAll(page);
    }

    /**
     * 停止订单循环的bug
     * @param orders
     */
//    public void stopOrderLeap(List<OrderInfoPO> orders) {
//        for (OrderInfoPO order : orders) {
//            stopOrderLeap(order);
//        }
//    }
//    private void stopOrderLeap(OrderInfoPO order) {
//        List<OrderItemPO> orderItems= order.getOrderItems();
//        for (OrderItem orderItem : orderItems) {
//            orderItem.setOrder(null);
//        }
//    }

    @Override
    public Integer saveOrUpdateOrder(OrderInfoPO orderInfoPO) {
        if (orderInfoPO.getId() != null) {
            OrderInfoPO porderInfoPO = iorderDO.getById(orderInfoPO.getId());
            if (porderInfoPO != null) {
                //进入这里代表数据库有这条数据
                porderInfoPO.setUpdateTime(new Date());
//                porderInfoPO.setName(orderInfoPO.getName());
//                返回保存后的订单数据
                OrderInfoPO PO = iorderDO.save(orderInfoPO);
                return PO.getId();
            }
        } else {
            OrderInfoPO PO = iorderDO.save(orderInfoPO);
            return PO.getId();
        }
        return null;
    }
    @Override
    public OrderInfoPO getAOrder(int id) {
        return iorderDO.getById(id);
    }


}
