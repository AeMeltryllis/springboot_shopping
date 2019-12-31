package com.zhw.dao;

import com.zhw.pojo.CategoryInfoPO;
import com.zhw.pojo.PropertyPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPropertyDO extends JpaRepository<PropertyPO,Integer> {
    Page<PropertyPO> findByCategoryInfoPO(CategoryInfoPO categoryPO, Pageable pageable);

}
