package com.zhw.service.Impl;

import com.zhw.dao.ICategoryDO;
import com.zhw.pojo.CategoryInfoPO;
import com.zhw.service.ICommonService;
import com.zhw.util.DataVO;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonService implements ICommonService {
    @Autowired
    ICategoryDO iCategoryDO;

    @Override
    public List<CategoryInfoPO> listCategory() {
        return iCategoryDO.findAll();
    }

    @Override
    public CategoryInfoPO getCategory(int id) {
        return iCategoryDO.getById(id);
    }

    @Override
    public Page pageCategory() {
        return null;
    }

}
