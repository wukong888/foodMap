package com.marketing.system.service;

import com.marketing.system.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService {

    List<Permission> selectByMap(Map<String,Object> map);
}
