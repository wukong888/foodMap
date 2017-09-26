package com.marketing.system.mapper;

import com.marketing.system.entity.TaskLogRecord;

public interface TaskLogRecordMapper {
    int deleteByPrimaryKey(Integer tasklogrecordid);

    int insert(TaskLogRecord record);

    int insertSelective(TaskLogRecord record);

    TaskLogRecord selectByPrimaryKey(Integer tasklogrecordid);

    int updateByPrimaryKeySelective(TaskLogRecord record);

    int updateByPrimaryKey(TaskLogRecord record);
}