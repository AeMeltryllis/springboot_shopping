package com.zhw.service.Impl;

import com.zhw.dao.*;
import com.zhw.pojo.*;
import com.zhw.service.ICommonService;
import com.zhw.util.ImageUtil;
import com.zhw.xxo.DataVO;
import com.zhw.xxo.ProductVO;
import org.apache.catalina.User;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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
    @Autowired IReviewDO iReviewDO;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 获取分类
     * 会包含商品信息
     * @return
     */
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
     * 商品分类去质器
     * @param cs
     */
    public void removeCategoryFromProduct(List<CategoryInfoPO> categorys) {
        for (CategoryInfoPO category : categorys) {
            removeCategoryFromProduct(category);
        }
    }

    /**
     * 该方法有大问题，修改实体类会触发事务管理，自动保存进数据库
     * @param category
     */
    public void removeCategoryFromProduct(CategoryInfoPO category) {

        Session session = entityManager.unwrap(org.hibernate.Session.class);
//        获取先前存入分类中的商品
        List<ProductPO> products =category.getProducts();
//        临时商品实体集合
        List<ProductPO> tProducts =new ArrayList<>();
//        临时商品实体
        if(null!=products) {
            for (ProductPO product : products) {
                ProductPO tProductPo = new ProductPO();
                BeanUtils.copyProperties(product,tProductPo);
//                将商品中的分类设置为null
//                CategoryInfoPO categoryInfoPOaaaaa = new CategoryInfoPO();
//                categoryInfoPOaaaaa.setId(1);
                tProductPo.setCategoryInfoPO(null);
//               核心方法，删除对象里的缓存，不会自动更新！！！
                session.evict(tProductPo);
                tProducts.add(tProductPo);
            }
//            将修改好的集合放回实体类
            category.setProducts(tProducts);
        }

        List<List<ProductPO>> productsByRow =category.getProductsByRow();
//        临时
        ProductPO tProduct = new ProductPO();
        //        临时商品实体集合
        if(null!=productsByRow) {
            for (List<ProductPO> ps : productsByRow) {
                for (ProductPO p: ps) {
                    p.setCategoryInfoPO(null);
                    session.evict(p);
                }
            }
        }
    }

    /**
     * 将数据设置进实体类中
     * @param product
     */
    public void setSaleAndReviewNumber(ProductPO product) {
        int saleCount = this.getSaleCount(product);
//        设置进去
        product.setSaleNumber(saleCount);
        int reviewCount = this.getProductReviewCount(product);
        product.setReviewNumber(reviewCount);

    }

    public void setSaleAndReviewNumber(List<ProductPO> products) {
        for (ProductPO product : products)
            this.setSaleAndReviewNumber(product);
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

    /**
     * 保存修改产品函数
     * @param productPO
     * @return
     */
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

    /**
     * 删除商品函数
     * @param id
     */
    @Override
    public void deleteProduct(int id) {
        ProductPO productPO = iProductDO.findById(id).get();
        if (productPO != null) {
            productPO.setDeleted(true);
            iProductDO.save(productPO);
        } else {
        }

    }

    /**
     * 商品去质函数
     * @param categorys
     */
    public void fillProduct(List<CategoryInfoPO> categorys) {
        for (CategoryInfoPO category : categorys) {
            fillProduct(category);
        }
    }
    public void fillProduct(CategoryInfoPO category) {
        List<ProductPO> products = iProductDO.findByCategoryInfoPO(category);
//        填充图片
        this.setFirstProdutImages(products);
        category.setProducts(products);
    }

    /**
     * 获取前台首页所有数据方法
     * @return
     */
    public List<CategoryInfoPO> homeCategory(){
//        list出来的新数据，被保存在session中
        List<CategoryInfoPO> categoryInfoPOS= this.listCategory();
//        向分类集合填充商品
        this.fillProduct(categoryInfoPOS);
        this.fillByRow(categoryInfoPOS);
        /**
         *复制集合
         */
        List<CategoryInfoPO> tlist = new ArrayList<>();
        for (CategoryInfoPO tt:categoryInfoPOS
             ) {
            CategoryInfoPO ttcategoryInfoPO = new CategoryInfoPO();
//            复制成新的实体类
            BeanUtils.copyProperties(tt,ttcategoryInfoPO);
//            将复制的实体类添加进新的集合
            tlist.add(ttcategoryInfoPO);
        }
//        将新集合去质
//        this.removeCategoryFromProduct(tlist);
//        返回前台集合
        return tlist;
    }


    /**
     * 百度方法，待试验
     * @param categorys
     */
    public void fillByRow(List<CategoryInfoPO> categorys) {
//        暂定为8
        int productNumberEachRow = 8;
        for (CategoryInfoPO category : categorys) {
            List<ProductPO> products =  category.getProducts();
            List<List<ProductPO>> productsByRow =  new ArrayList<>();
            for (int i = 0; i < products.size(); i+=productNumberEachRow) {
                int size = i+productNumberEachRow;
                size= size>products.size()?products.size():size;
                List<ProductPO> productsOfEachRow =products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }
    }


    /**
     * 前端获取产品信息函数
     * @param productImagePO
     * @param image
     * @throws Exception
     */
    @Override
    public DataVO getProductInfoInFore(int productId){
        ProductPO product = this.getProduct(productId);
        List<ProductImagePO> productSingleImages = this.listSingleProductImages(product);
        List<ProductImagePO> productDetailImages = this.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);
//      属性管理后来添加
//        List<> pvs = propertyValueService.list(product);
        List<ReviewPO> reviews = iReviewDO.findByProductPOOrderByIdDesc(product);
        this.setSaleAndReviewNumber(product);
        this.setFirstProdutImage(product);
//用map保存
        Map<String,Object> map= new HashMap<>();
        map.put("product", product);
//        map.put("pvs", pvs);
        map.put("reviews", reviews);

        return new DataVO(map);

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
        order.setTotal(totalPrice);
        order.setOrderItems(orderItems);
        order.setTotalNumber(totalNumber);
    }

    /**
     * 获取全部订单信息
     * @param order
     * @return
     */
    public List<OrderItemPO> listByOrder(OrderInfoPO item) {

//      spring2.2只能静态生成sort对象  Sort sort = new Sort(Sort.Direction.DESC, "id");
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return iorderItemDO.findAll(sort);
    }

    /**
     * 订单分页
     */
    @Override
    public Page pageOrder(int pageIndex, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        OrderInfoPO temporaryPO = new OrderInfoPO();
        Pageable page = PageRequest.of(pageIndex - 1, size, sort);
        Example<OrderInfoPO> example = Example.of(temporaryPO);
        Page<OrderInfoPO> all = iorderDO.findAll(page);

        this.fill(all.getContent());
        this.stopOrderLeap(all.getContent());
        this.changeOrderStatus(all.getContent());
        return all;
    }
public void changeOrderStatus(List<OrderInfoPO> orders){
    for (OrderInfoPO o:orders
         ) {
        String statusDesc = o.getOrderStatus();
//        if(null!=statusDesc)
//            return ;
        String desc ="未知状态";
        switch(statusDesc){
            case WAIT_PAY:
                desc="等待付款";
                break;
            case WAIT_DELIVERY:
                desc="等待发货";
                break;
            case WAIT_COMFIRM:
                desc="等待接收";
                break;
            case HAS_REVIEW:
                desc="已等评";
                break;
            case HAS_FINISH:
                desc="已完成";
                break;
            case HAS_DELETE:
                desc="已刪除";
                break;
            default:
                desc="未知状态";
        }
        o.setStatusDesc(desc);

    }

}


    /**
     * 停止订单循环的bug函数
     * @param orders
     */
    public void stopOrderLeap(List<OrderInfoPO> orders) {
        for (OrderInfoPO order : orders) {
            stopOrderLeap(order);
        }
    }
    private void stopOrderLeap(OrderInfoPO order) {
        List<OrderItemPO> orderItems= order.getOrderItems();
        for (OrderItemPO orderItem : orderItems) {
            orderItem.setOrderInfoPO(null);
        }
    }

    @Override
    public Integer saveOrUpdateOrder(OrderInfoPO orderInfoPO) {
        if (orderInfoPO.getId() != null) {
            OrderInfoPO pOrderInfoPO = iorderDO.getById(orderInfoPO.getId());
            if (pOrderInfoPO != null) {
                //进入这里代表数据库有这条数据
                pOrderInfoPO.setUpdateTime(new Date());
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
//获取已售数量
    public int getSaleCount(ProductPO product) {
        List<OrderItemPO> orderItemPOS =this.listOrderItemByProduct(product);
//        结果
        int result =0;
        for (OrderItemPO oi : orderItemPOS) {
            if(null!=oi.getOrderInfoPO())
//             循环计算总额
                if(null!= oi.getOrderInfoPO() && null!=oi.getOrderInfoPO().getPayDate())
                    result+=oi.getNumber();
        }
        return result;
    }
//    根据产品遍历
    public List<OrderItemPO> listOrderItemByProduct(ProductPO product) {
        return iorderItemDO.findByProductPO(product);
    }
//    根据订单遍历
    public List<OrderItemPO> listOrderItemByOrder(OrderInfoPO order) {
        return iorderItemDO.findByOrderInfoPOOrderByIdDesc(order);
    }

    public OrderItemPO getOrderItem(int id) {
        return iorderItemDO.getOne(id);
    }

    public void delete(int id) {
        iorderItemDO.deleteById(id);
    }

    /**
     * 添加修改订单项，老一套函数
     * @param orderItemPO
     * @return
     */
    public Integer saveOrUpdateOrderItem(OrderItemPO orderItemPO) {
        if (orderItemPO.getId() != null) {
            OrderItemPO orderItemPOP = iorderItemDO.getOne(orderItemPO.getId());
            if (orderItemPOP != null) {
                orderItemPOP.setUpdateTime(new Date());
//                porderInfoPO.setName(orderInfoPO.getName());
                OrderItemPO PO = iorderItemDO.save(orderItemPOP);
                return PO.getId();
            }
        } else {
//            如果没有就保存
            OrderItemPO PO = iorderItemDO.save(orderItemPO);
            return PO.getId();
        }
        return null;
    }





    /*写用户登录方法*/

    public UserPO getUserByName(String name) {
        return iUserDO.findByName(name);
    }
    public void addUser(UserPO user) {
        iUserDO.save(user);
    }

    /**
     * 用户祖册方法
     * @param userPO
     * @return
     */
    public DataVO checkUser(UserPO userPO){
        String name =  userPO.getName();
        String password = userPO.getPassWord();
//        hutool转义
//        name = HtmlUtils.htmlEscape(name);
        userPO.setName(name);
        boolean exist = this.getUserByName(name)!=null;
//        判断数据库中是否存在
        if(exist){
            String message ="用户名已经被使用,不能使用";
            DataVO dataVO = new DataVO();
            dataVO.setFailMsg(message);
            return dataVO;
    }
        //如果不存在，保存
        userPO.setPassWord(password);
        this.addUser(userPO);
        return new DataVO("ok");
}

    @Override
    public UserPO getUserByNameAndPassword(String name, String password) {
        return iUserDO.getByNameAndPassWord(name,password);
    }
/*评价模块*/

    /**
     * 保存评价函数
     * @param review
     */
    public void addReview(ReviewPO review) {
    iReviewDO.save(review);
}

    /**
     * 获取产品评价
     * @param product
     * @return
     */
    public List<ReviewPO> list(ProductPO product){
        List<ReviewPO> result =  iReviewDO.findByProductPOOrderByIdDesc(product);
        return result;
    }

    public int getProductReviewCount(ProductPO product) {
        return iReviewDO.countByProductPO(product);
    }

    /*搜索功能块*/
    @Override
    public List<ProductPO> searchProduct(String keyword){

        List<ProductPO> productPOS= this.searchProductByJpa(keyword,0,20);
//置入相片
        this.setFirstProdutImages(productPOS);
        this.setSaleAndReviewNumber(productPOS);
        return productPOS;
    }

    /**
     * 搜索模糊查询
     * @param keyword
     * @param start
     * @param size
     * @return
     */
    public List<ProductPO> searchProductByJpa(String keyword, int start, int size) {
//        jpa新版还能静态生成
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable page = PageRequest.of(start, size, sort);
//        模糊搜索
        List<ProductPO> products =iProductDO.findByNameLike("%"+keyword+"%",page);
        return products;
    }

    /*订单项模块*/



}







