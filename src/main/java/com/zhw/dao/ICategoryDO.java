package com.zhw.dao;

import com.zhw.pojo.CategoryInfoPO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 接口可以实现多继承
 */
public interface ICategoryDO extends JpaRepository<CategoryInfoPO,Integer>, JpaSpecificationExecutor<CategoryInfoPO> {
    /**
     * 根据id获取单个实体
     * @param id
     * @return
     */
    CategoryInfoPO getById(int id);

    /**
     * 无条件分页实体
     * @param pageable
     * @return
     */
    @Override
    Page<CategoryInfoPO> findAll(Pageable pageable);

    /**
     * 带条件分页实体
     * @param example
     * @param pageable
     * @param <S>
     * @return
     */
    @Override
    <S extends CategoryInfoPO> Page<S> findAll(Example<S> example, Pageable pageable);
}
