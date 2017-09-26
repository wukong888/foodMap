package com.marketing.system.mapper_two;

import com.marketing.system.entity.ProjectInfo;

public interface ProjectInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectInfo record);

    int insertSelective(ProjectInfo record);

    ProjectInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectInfo record);

    int updateByPrimaryKey(ProjectInfo record);

    Integer getMyApplyProject(String name);

    int getUpApplyProject(String proType);
}