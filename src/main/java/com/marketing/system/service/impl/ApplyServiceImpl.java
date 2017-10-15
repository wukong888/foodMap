package com.marketing.system.service.impl;

import com.marketing.system.entity.*;
import com.marketing.system.mapper_two.*;
import com.marketing.system.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApplyServiceImpl implements ApplyService{

    @Autowired
    private ProjectTaskMapper projectTaskMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private ProLogRecordMapper proLogRecordMapper;

    @Autowired
    private ProjectSubtaskMapper projectSubtaskMapper;

    @Autowired
    private MembersMapper membersMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

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

    @Override
    public List<Map<String, Object>> selectProSubtaskByProId(int proId) {

        List<Map<String, Object>> list = projectSubtaskMapper.selectProSubtaskByProId(proId);

        return list;
    }

    @Override
    public List<Map<String, Object>> getSquadIdList(Map<String, Object> map) {

        List<Map<String, Object>> list = membersMapper.getSquadIdList(map);

        return list;
    }

    @Override
    public List<Map<String, Object>> getSquadList(Map<String, Object> map) {

        List<Map<String, Object>> list = groupMapper.getSquadList(map);

        return list;
    }

    @Override
    public List<Map<String, Object>> getDepartmentList(Map<String, Object> map) {

        List<Map<String, Object>> list = departmentMapper.getDepartmentList(map);

        return list;
    }

    @Override
    public Members selectSquadIdByMember(String member) {

        return membersMapper.selectSquadIdByMember(member);
    }
}
