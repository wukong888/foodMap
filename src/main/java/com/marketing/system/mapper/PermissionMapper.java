package com.marketing.system.mapper;

import com.marketing.system.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionMapper {
    int insert(Permission record);

    int insertSelective(Permission record);

    List<Permission> selectByMap(Map<String,Object> map);
}