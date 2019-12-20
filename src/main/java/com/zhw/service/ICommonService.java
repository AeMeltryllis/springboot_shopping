package com.zhw.service;

import com.zhw.pojo.CategoryInfoPO;
import com.zhw.util.DataVO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ICommonService  {
    List<CategoryInfoPO> listCategory();
    CategoryInfoPO getCategory(int id);
    Page  pageCategory();
}
