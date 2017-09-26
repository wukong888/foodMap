package com.marketing.system.mapper;

import com.marketing.system.entity.ProDevelopLog;

public interface ProDevelopLogMapper {
    int deleteByPrimaryKey(Integer prodeveloplogid);

    int insert(ProDevelopLog record);

    int insertSelective(ProDevelopLog record);

    ProDevelopLog selectByPrimaryKey(Integer prodeveloplogid);

    int updateByPrimaryKeySelective(ProDevelopLog record);

    int updateByPrimaryKey(ProDevelopLog record);
}