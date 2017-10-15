package com.marketing.system.mapper_two;

import com.marketing.system.entity.ProjectSubtask;

import java.util.List;
import java.util.Map;

public interface ProjectSubtaskMapper {
    int deleteByPrimaryKey(Integer subtaskId);

    int insert(ProjectSubtask record);

    int insertSelective(ProjectSubtask record);

    ProjectSubtask selectByPrimaryKey(Integer subtaskId);

    int updateByPrimaryKeySelective(ProjectSubtask record);

    int updateByPrimaryKey(ProjectSubtask record);

    Integer getMyJoinProject(String name);

    List<Map<String,Object>> getSubTaskIdByHander(String subtaskhandler);

    List<Map<String,Object>> getSubTaskListMap(Map<String,Object> map);

    int deleteSubTaskById(Integer taskId);

    List<ProjectSubtask> getProjectSubtaskList(Integer taskId);

    List<Map<String, Object>> selectProSubtaskByProId(Integer proId);

}