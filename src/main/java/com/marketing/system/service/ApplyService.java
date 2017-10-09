package com.marketing.system.service;

import com.marketing.system.entity.ProLogRecord;
import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectTask;

import java.util.List;

public interface ApplyService {

    int insertApplyProject(ProjectInfo projectInfo);

    int selectMaxProId();

    int insertSelective(ProjectTask task);

    int insertProLogRecord(ProLogRecord proLogRecord);

    List<String> getCreaterByName(String creater);
}
