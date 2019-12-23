package com.zhw.service.Impl;

import com.zhw.dao.ICategoryDO;
import com.zhw.pojo.CategoryInfoPO;
import com.zhw.service.ICommonService;
import com.zhw.util.DataVO;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
        Pageable  page=  PageRequest.of(pageIndex-1, size,sort);
        return iCategoryDO.findAll(page);
    }

    @Override
    public void addCategory(CategoryInfoPO category) {
        iCategoryDO.save(category);
    }

    @Override
    public void saveImage(MultipartFile image, HttpServletRequest request) {


    }


}
