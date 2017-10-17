package com.marketing.system.service.impl;

import com.marketing.system.entity.ProLogRecord;
import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectTask;
import com.marketing.system.mapper_two.ProLogRecordMapper;
import com.marketing.system.mapper_two.ProjectInfoMapper;
import com.marketing.system.mapper_two.ProjectTaskMapper;
import com.marketing.system.service.UpProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UpProjectServiceImpl implements UpProjectService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private ProjectTaskMapper projectTaskMapper;

    @Autowired
    private ProLogRecordMapper proLogRecordMapper;

    @Override
    public List<ProjectInfo> getProjectInfoList(Map<String, Object> map) {

        List<ProjectInfo> list = projectInfoMapper.getProjectInfoListAll(map);

        return list;
    }

    @Override
    public int sumAll(Map<String, Object> map) {
        return projectInfoMapper.sumAll(map);
    }

    @Override
    public int setPassOrReject(Map<String, Object> map) {

        return projectInfoMapper.setPassOrReject(map);
    }

    @Override
    public int setPassOrRejectTwo(Map<String, Object> map) {

        return projectInfoMapper.setPassOrRejectTwo(map);
    }

    @Override
    public ProjectInfo selectByPrimaryKey(int id) {

        return projectInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ProjectTask> getProjectTaskList(int proId) {

        List<ProjectTask> taskList = projectTaskMapper.getProjectTaskList(proId);

        return taskList;
    }

    @Override
    public List<Map<String, Object>> getProjectTaskListMap1(int proId) {

        List<Map<String, Object>> taskList = projectTaskMapper.getProjectTaskListMap1(proId);

        return taskList;
    }

    @Override
    public String selectDepartmentIdBySquadId(int squadId){
        String departmentId=projectTaskMapper.selectDepartmentIdBySquadId(squadId);
        return departmentId;
    }

    @Override
    public String selectSquadBySquadId(int squadId) {

        String departmentId=projectTaskMapper.selectSquadBySquadId(squadId);

        return departmentId;
    }

    @Override
    public List<Map<String, Object>> getProjectTaskListMap(int proId) {

        List<Map<String, Object>> mapList = projectTaskMapper.getProjectTaskListMap(proId);

        return mapList;
    }

    @Override
    public int insertProLogRecord(ProLogRecord proLogRecord) {

        return proLogRecordMapper.insertSelective(proLogRecord);
    }

    @Override
    public List<ProLogRecord> getProLogRecordList(int proId) {

        List<ProLogRecord> logRecordList = proLogRecordMapper.getProLogRecordList(proId);

        return logRecordList;
    }

    @Override
    public String selectDepartmentByDId(String departmentId) {

        String department = projectTaskMapper.selectDepartmentByDId(departmentId);

        return department;
    }
}
