package com.marketing.system.mapper_two;

import com.marketing.system.entity.SubtaskLogRecord;

import java.util.List;

public interface SubtaskLogRecordMapper {
    int deleteByPrimaryKey(Integer subtasklogrecordid);

    int insert(SubtaskLogRecord record);

    int insertSelective(SubtaskLogRecord record);

    SubtaskLogRecord selectByPrimaryKey(Integer subtasklogrecordid);

    int updateByPrimaryKeySelective(SubtaskLogRecord record);

    int updateByPrimaryKey(SubtaskLogRecord record);

    List<SubtaskLogRecord> getSubtaskLogList(int subtaskId);
}