package com.marketing.system.service.impl;

import com.marketing.system.entity.ProLogRecord;
import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectTask;
import com.marketing.system.mapper_two.ProLogRecordMapper;
import com.marketing.system.mapper_two.ProjectInfoMapper;
import com.marketing.system.mapper_two.ProjectTaskMapper;
import com.marketing.system.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyServiceImpl implements ApplyService{

    @Autowired
    private ProjectTaskMapper projectTaskMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private ProLogRecordMapper proLogRecordMapper;


    @Override
    public int insertApplyProject(ProjectInfo projectInfo) {

        return projectInfoMapper.insertSelective(projectInfo);
    }

    @Override
    public int selectMaxProId() {

        return projectTaskMapper.selectMaxProId();
    }

    @Override
    public int insertSelective(ProjectTask task) {

        return projectTaskMapper.insertSelective(task);
    }

    @Override
    public int insertProLogRecord(ProLogRecord proLogRecord) {

        return proLogRecordMapper.insertSelective(proLogRecord);
    }

    @Override
    public List<String> getCreaterByName(String creater) {

        List<String> list = projectInfoMapper.getCreaterByName(creater);

        return list;
    }
}
