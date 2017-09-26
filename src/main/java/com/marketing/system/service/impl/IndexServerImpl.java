package com.marketing.system.service.impl;

import com.marketing.system.mapper_two.ProjectSubtaskMapper;
import com.marketing.system.mapper_two.ProjectTaskMapper;
import com.marketing.system.mapper_two.ProjectInfoMapper;
import com.marketing.system.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexServerImpl implements IndexService{

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private ProjectTaskMapper projectTaskMapper;

    @Autowired
    private ProjectSubtaskMapper projectSubtaskMapper;

    @Override
    public Integer getMyApplyProject(String name) {

        return projectInfoMapper.getMyApplyProject(name);
    }

    @Override
    public Integer getMyJoinProject(String name) {

        //项目任务
        Integer i = projectTaskMapper.getMyJoinProject(name);

        //子任务
        Integer j = projectSubtaskMapper.getMyJoinProject(name);

        return i + j;
    }

    @Override
    public int getUpApplyProject(String proType) {

        return projectInfoMapper.getUpApplyProject(proType);
    }
}
