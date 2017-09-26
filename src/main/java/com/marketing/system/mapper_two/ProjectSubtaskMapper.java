package com.marketing.system.mapper;

import com.marketing.system.entity.ProjectSubtask;

public interface ProjectSubtaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectSubtask record);

    int insertSelective(ProjectSubtask record);

    ProjectSubtask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectSubtask record);

    int updateByPrimaryKey(ProjectSubtask record);
}