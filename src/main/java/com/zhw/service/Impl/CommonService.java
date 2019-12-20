package com.zhw.service.Impl;

import com.zhw.dao.ICommonDO;
import com.zhw.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonService implements ICommonService {
    @Autowired
    ICommonDO iCommonDO;
    @Override
    public void testDAO() {

    }
}
