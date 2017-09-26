package com.marketing.system.service;

import com.marketing.system.entity.ProjectInfo;

import java.util.List;
import java.util.Map;

public interface OnlineProService {

    //模糊查询所有待审批的项目
    Map<String, Object> selectOnPro(Integer current, Integer pageSize, String createsquadid, String creater, String createdate1, String createdate2, String plansdate1, String plansdate2, String protype, String param);

}