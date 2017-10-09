package com.marketing.system.mapper_two;

import com.marketing.system.entity.SubtaskDevelopLog;

import java.util.List;

public interface SubtaskDevelopLogMapper {
    int deleteByPrimaryKey(Integer subtaskdeveloplogid);

    int insert(SubtaskDevelopLog record);

    int insertSelective(SubtaskDevelopLog record);

    SubtaskDevelopLog selectByPrimaryKey(Integer subtaskdeveloplogid);

    int updateByPrimaryKeySelective(SubtaskDevelopLog record);

    int updateByPrimaryKey(SubtaskDevelopLog record);

    List<SubtaskDevelopLog> getSubTaskDevLogList(Integer subtaskId);
}