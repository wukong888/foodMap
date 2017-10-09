package com.marketing.system.mapper_two;

import com.marketing.system.entity.TaskLogRecord;

import java.util.List;

public interface TaskLogRecordMapper {
    int deleteByPrimaryKey(Integer tasklogrecordid);

    int insert(TaskLogRecord record);

    int insertSelective(TaskLogRecord record);

    TaskLogRecord selectByPrimaryKey(Integer tasklogrecordid);

    int updateByPrimaryKeySelective(TaskLogRecord record);

    int updateByPrimaryKey(TaskLogRecord record);

    List<TaskLogRecord> getTaskLogList(Integer taskId);
}