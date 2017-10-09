package com.marketing.system.mapper_two;

import com.marketing.system.entity.TaskDevelopLog;

import java.util.List;

public interface TaskDevelopLogMapper {
    int deleteByPrimaryKey(Integer taskdeveloplogid);

    int insert(TaskDevelopLog record);

    int insertSelective(TaskDevelopLog record);

    TaskDevelopLog selectByPrimaryKey(Integer taskdeveloplogid);

    int updateByPrimaryKeySelective(TaskDevelopLog record);

    int updateByPrimaryKey(TaskDevelopLog record);

    List<TaskDevelopLog> getTaskDeveLogList(Integer taskId);
}