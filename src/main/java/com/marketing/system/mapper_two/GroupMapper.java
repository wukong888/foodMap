package com.marketing.system.mapper_two;

import com.marketing.system.entity.Group;

import java.util.List;
import java.util.Map;

public interface GroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);

    List<Group> getGroup(Integer departmentid);

    List<Group> getGroupNo();

    Group getGroupBySquadId(Integer SquadId);

    List<Map<String,Object>> getSquadId(String departmentid);
}