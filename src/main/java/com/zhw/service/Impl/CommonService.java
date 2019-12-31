package com.zhw.service.Impl;

import com.zhw.dao.ICategoryDO;
import com.zhw.dao.IProductDO;
import com.zhw.dao.IPropertyDO;
import com.zhw.pojo.CategoryInfoPO;
import com.zhw.pojo.ProductPO;
import com.zhw.pojo.PropertyPO;
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
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommonService implements ICommonService {

    final static int PAGE_SIZE = 10;


    @Autowired
    ICategoryDO iCategoryDO;
    @Autowired
    IPropertyDO iPropertyDO;
    @Autowired
    IProductDO iProductDO;

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
        //Page类中的下标是从0开始，所以穿进来的pageIndex要-1
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

}
