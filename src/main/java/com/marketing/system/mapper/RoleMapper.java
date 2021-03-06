package com.marketing.system.mapper;

import com.github.pagehelper.Page;
import com.marketing.system.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    Role getRoleByName(Map<String,Object> map);

    List<Role> getUpProjectList(Map<String,Object> map);

    int sumAll(Map<String,Object> map);

    int setPassOrReject(Map<String,Object> map);
}