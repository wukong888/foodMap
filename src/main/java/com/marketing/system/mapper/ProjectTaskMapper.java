package com.marketing.system.mapper;

import com.marketing.system.entity.ProjectTask;

public interface ProjectTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectTask record);

    int insertSelective(ProjectTask record);

    ProjectTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectTask record);

    int updateByPrimaryKey(ProjectTask record);
}