package com.zhw.dao;

import com.zhw.pojo.ProductPO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IProductDO extends JpaRepository<ProductPO, Integer>, JpaSpecificationExecutor<ProductPO> {
    /**
     * 带条件分页实体
     * @param example
     * @param pageable
     * @param <S>
     * @return
     */
    @Override
    <S extends ProductPO> Page<S> findAll(Example<S> example, Pageable pageable);
}
