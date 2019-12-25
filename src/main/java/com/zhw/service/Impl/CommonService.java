package com.zhw.service.Impl;

import com.zhw.dao.ICategoryDO;
import com.zhw.pojo.CategoryInfoPO;
import com.zhw.service.ICommonService;
import com.zhw.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class CommonService implements ICommonService {

    final  static int PAGE_SIZE=10;


    @Autowired
    ICategoryDO iCategoryDO;

    @Override
    public List<CategoryInfoPO> listCategory() {
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        return iCategoryDO.findAll(sort);
    }

    @Override
    public CategoryInfoPO getCategory(int id) {
        return iCategoryDO.getById(id);
    }


    @Override
    public Page pageCategory(int pageIndex,int size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        //Page类中的下标是从0开始，所以穿进来的pageIndex要-1
        CategoryInfoPO temporaryPO = new CategoryInfoPO();
        temporaryPO.setDeleted(false);
        Pageable  page=  PageRequest.of(pageIndex-1, size,sort);
        Example<CategoryInfoPO> example = Example.of(temporaryPO);
        return iCategoryDO.findAll(example,page);
    }

    @Override
    public Integer addCategory(CategoryInfoPO category) {
        CategoryInfoPO PO = iCategoryDO.save(category);
        return PO.getId();

    }

    @Override
    public void saveImage(Integer categoryId ,MultipartFile image, HttpServletRequest request) throws IOException {
       String rootPath   =  request.getServletContext().getRealPath("img/category");
        File imagePath = new File(rootPath);
        File file = new File(imagePath,categoryId+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        //转换成文件流
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        //将流写入
        ImageIO.write(img,".jpg",file);
    }

    /**
     * 虽然叫删除，但是使用的是逻辑删除
     * @param id
     */
    @Override
    public void deleteCategory(int id) {
        //t = temporary
        CategoryInfoPO tCategoryPO = iCategoryDO.getById(id);
        if (tCategoryPO!=null){
            tCategoryPO.setDeleted(true);
            iCategoryDO.save(tCategoryPO);
        }else {
            System.out.println("哦哦");
        }

    }
}
