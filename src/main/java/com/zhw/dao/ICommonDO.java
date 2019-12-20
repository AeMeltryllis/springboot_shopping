package com.zhw.dao;

import com.zhw.pojo.CategoryInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommonDO extends JpaRepository<CategoryInfoPO,Integer> {
}
