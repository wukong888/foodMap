package com.marketing.system.mapper;

import com.marketing.system.entity.ProLogRecord;

public interface ProLogRecordMapper {
    int deleteByPrimaryKey(Integer prologrecordid);

    int insert(ProLogRecord record);

    int insertSelective(ProLogRecord record);

    ProLogRecord selectByPrimaryKey(Integer prologrecordid);

    int updateByPrimaryKeySelective(ProLogRecord record);

    int updateByPrimaryKey(ProLogRecord record);
}