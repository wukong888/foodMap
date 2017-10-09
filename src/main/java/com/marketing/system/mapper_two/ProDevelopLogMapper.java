package com.marketing.system.mapper_two;

import com.marketing.system.entity.ProDevelopLog;

import java.util.List;

public interface ProDevelopLogMapper {
    int deleteByPrimaryKey(Integer prodeveloplogid);

    int insert(ProDevelopLog record);

    int insertSelective(ProDevelopLog record);

    ProDevelopLog selectByPrimaryKey(Integer prodeveloplogid);

    int updateByPrimaryKeySelective(ProDevelopLog record);

    int updateByPrimaryKey(ProDevelopLog record);

    List<ProDevelopLog> getProDevelopLogList(Integer proId);


}