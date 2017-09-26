package com.marketing.system.mapper;

import com.marketing.system.entity.SubtaskDevelopLog;

public interface SubtaskDevelopLogMapper {
    int deleteByPrimaryKey(Integer subtaskdeveloplogid);

    int insert(SubtaskDevelopLog record);

    int insertSelective(SubtaskDevelopLog record);

    SubtaskDevelopLog selectByPrimaryKey(Integer subtaskdeveloplogid);

    int updateByPrimaryKeySelective(SubtaskDevelopLog record);

    int updateByPrimaryKey(SubtaskDevelopLog record);
}