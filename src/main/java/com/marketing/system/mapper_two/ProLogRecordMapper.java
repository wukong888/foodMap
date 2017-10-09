package com.marketing.system.mapper_two;

import com.marketing.system.entity.ProLogRecord;

import java.util.List;

public interface ProLogRecordMapper {
    int deleteByPrimaryKey(Integer prologrecordid);

    int insert(ProLogRecord record);

    int insertSelective(ProLogRecord record);

    ProLogRecord selectByPrimaryKey(Integer prologrecordid);

    int updateByPrimaryKeySelective(ProLogRecord record);

    int updateByPrimaryKey(ProLogRecord record);

    List<ProLogRecord> getProLogRecordList(Integer proId);
}