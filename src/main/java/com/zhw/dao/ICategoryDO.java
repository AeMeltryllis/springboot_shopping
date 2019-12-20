package com.zhw.dao;

import com.zhw.pojo.CategoryInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryDO extends JpaRepository<CategoryInfoPO,Integer> {
    CategoryInfoPO getById(int id);


}
