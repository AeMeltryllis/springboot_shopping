package com.zhw.dao;

import com.zhw.pojo.ProductImagePO;

import com.zhw.pojo.ProductPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductImageDO extends JpaRepository<ProductImagePO,Integer> {
        List<ProductImagePO> findByProductPOAndTypeOrderByIdDesc(ProductPO productPO,String type);

}
