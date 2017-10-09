package com.marketing.system.service;

import com.marketing.system.entity.ProDevelopLog;
import com.marketing.system.entity.ProLogRecord;
import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectTask;

import java.util.List;
import java.util.Map;

public interface RecycleProService {

    //模糊查询所有待审批的项目
    Map<String, Object> selectRecPro(Integer current, Integer pageSize, String creatersquadid, String creater, String createdate1, String createdate2, String plansdate1, String plansdate2, String protype, String param);

    //查看上线待审批项目的基本信息和项目信息
    ProjectInfo selectRecProInfo(Integer id,Integer proId);

    //查看上线待审批项目的日志记录
    List<ProLogRecord> selectRecProLogRecord(Integer proId);


    //查看上线待审批项目的参与组
    List<ProjectTask> selectRecTask(Integer proId);
}
