package com.marketing.system.mapper;

import com.marketing.system.entity.SubtaskLogRecord;

public interface SubtaskLogRecordMapper {
    int deleteByPrimaryKey(Integer subtasklogrecordid);

    int insert(SubtaskLogRecord record);

    int insertSelective(SubtaskLogRecord record);

    SubtaskLogRecord selectByPrimaryKey(Integer subtasklogrecordid);

    int updateByPrimaryKeySelective(SubtaskLogRecord record);

    int updateByPrimaryKey(SubtaskLogRecord record);
}