package com.marketing.system.mapper_two;

import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectSubtask;

import java.util.List;
import java.util.Map;

public interface ProjectInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectInfo record);

    int insertSelective(ProjectInfo record);

    ProjectInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectInfo record);

    int updateByPrimaryKeySelectiveState(ProjectInfo record);

    int updateByPrimaryKey(ProjectInfo record);

    Integer getMyApplyProject(String name);

    Integer getUpApplyProject(String prostate);

    List<ProjectInfo> getProjectInfoList(String name);
    List<ProjectInfo> getProjectInfoListNull();

    List<Map<String, Object>> getProjectTaskList(String name);
    List<Map<String, Object>> getProjectTaskListNull();

    List<ProjectSubtask> getProjectSubTaskList(String name);
    List<ProjectSubtask> getProjectSubTaskListNull();

    List<ProjectInfo> getProjectInfoListAll(Map<String,Object> map);

    Integer sumAll(Map<String,Object> map);

    Integer setPassOrReject(Map<String,Object> map);

    Integer setPassOrRejectTwo(Map<String,Object> map);

    List<String> getCreaterByName(String creater);

    List<ProjectInfo> getMyProjectInfoList(Map<String,Object> map);

    List<ProjectInfo> getProjectByHanderMap(Map<String,Object> map);

    List<ProjectInfo> getProjectByHanderMapFinish(Map<String,Object> map);

    Integer getMyJoinProject(String name);

    Integer getAllProjectSize();

    List<Map<String,Object>> getProjectByProId(Map<String,Object> map);

    List<ProjectInfo> getgetMyProjectInfoListByProId(Map<String,Object> map);

    List<ProjectInfo> getProjectInfoByZuzhang(Map<String,Object> map);

    ProjectInfo getProjectInfoByProId(Integer proId);
}