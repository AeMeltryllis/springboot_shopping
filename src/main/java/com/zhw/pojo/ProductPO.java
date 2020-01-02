package com.zhw.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Table(name = "product_info")
@DynamicInsert
@DynamicUpdate


public class ProductPO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
    @Column(name = "name")
    private String name;
    @Column(name = "sub_title")
    private String subTitle;

    /*原始价格*/
    @Column(name = "original_price")
    private Double originalPrice;

    /*优惠价格*/
    @Column(name = "promote_price")
    private Double promotePrice;

    /*库存*/
    @Column(name = "stock")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryInfoPO categoryInfoPO;

    /*逻辑删除标记*/
    @Column(name = "is_deleted")
    private Boolean Deleted;

    //缩略图，后端置入，不需要关联
    //@Transient添加不进行序列化
    @Transient
    private ProductImagePO firstImage;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(Double promotePrice) {
        this.promotePrice = promotePrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public CategoryInfoPO getCategoryInfoPO() {
        return categoryInfoPO;
    }

    public void setCategoryInfoPO(CategoryInfoPO categoryInfoPO) {
        this.categoryInfoPO = categoryInfoPO;
    }

    public Boolean getDeleted() {
        return Deleted;
    }

    public void setDeleted(Boolean deleted) {
        Deleted = deleted;
    }

    public ProductImagePO getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(ProductImagePO firstImage) {
        this.firstImage = firstImage;
    }
}
