package com.marketing.system.mapper_two;

import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectTask;

import java.util.List;
import java.util.Map;

public interface ProjectTaskMapper {
    int deleteByPrimaryKey(Integer taskId);

    int insert(ProjectTask record);

    int insertSelective(ProjectTask record);

    ProjectTask selectByPrimaryKey(Integer taskId);

    int updateByPrimaryKeySelective(ProjectTask record);

    int updateByPrimaryKey(ProjectTask record);

    Integer getMyJoinProject(String name);

    int selectMaxProId();

    List<ProjectTask> getProjectTaskList(Integer proId);

    String selectDepartmentIdBySquadId(Integer squadId);

    String selectSquadBySquadId(Integer squadId);

    List<Map<String, Object>> getProjectTaskListMap1(Integer proId);

    List<Map<String,Object>> getProjectTaskListMap(Integer proId);

    List<Map<String,Object>> getproIdByTaskId(Map<String,Object> map);

    String selectDepartmentByDId(String departmentId);

}