package com.marketing.system.mapper;

import com.marketing.system.entity.UserRoles;

import java.util.List;
import java.util.Map;

public interface UserRolesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRoles record);

    int insertSelective(UserRoles record);

    UserRoles selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRoles record);

    int updateByPrimaryKey(UserRoles record);

    List<UserRoles> getRoleByAddName(Map<String,Object> map);

}