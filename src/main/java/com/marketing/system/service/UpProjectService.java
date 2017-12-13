package com.marketing.system.service;

import com.marketing.system.entity.ProLogRecord;
import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectTask;

import java.util.List;
import java.util.Map;

public interface UpProjectService {

    List<ProjectInfo> getProjectInfoList(Map<String,Object> map);

    int sumAll(Map<String, Object> map);

    int setPassOrReject(Map<String, Object> map);

    int setPassOrRejectTwo(Map<String, Object> map);

    ProjectInfo selectByPrimaryKey(int id);

    List<ProjectTask> getProjectTaskList(int proId);

    List<Map<String,Object>> getProjectTaskListMap(int proId);

    List<Map<String, Object>> getProjectTaskListMap1(int proId);

    String selectDepartmentIdBySquadId(int squadId);

    String selectSquadBySquadId(int squadId);

    int insertProLogRecord(ProLogRecord proLogRecord);

    List<ProLogRecord> getProLogRecordList(int proId);

    String selectDepartmentByDId(String departmentId);

    List<Map<String,Object>> getProjectTaskListMapByZuZhang(Map<String,Object> map);

}
