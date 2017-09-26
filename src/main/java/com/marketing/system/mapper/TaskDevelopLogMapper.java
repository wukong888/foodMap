package com.marketing.system.mapper;

import com.marketing.system.entity.TaskDevelopLog;

public interface TaskDevelopLogMapper {
    int deleteByPrimaryKey(Integer taskdeveloplogid);

    int insert(TaskDevelopLog record);

    int insertSelective(TaskDevelopLog record);

    TaskDevelopLog selectByPrimaryKey(Integer taskdeveloplogid);

    int updateByPrimaryKeySelective(TaskDevelopLog record);

    int updateByPrimaryKey(TaskDevelopLog record);
}