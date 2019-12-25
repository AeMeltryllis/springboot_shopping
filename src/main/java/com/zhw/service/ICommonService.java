package com.zhw.service;

import com.zhw.pojo.CategoryInfoPO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Service
public interface ICommonService  {
    List<CategoryInfoPO> listCategory();
    CategoryInfoPO getCategory(int id);
    Page  pageCategory(int pageIndex,int size);
    Integer addCategory(CategoryInfoPO category);
    void saveImage(Integer categoryId ,MultipartFile image, HttpServletRequest request) throws IOException;
    void deleteCategory(int id);
}
