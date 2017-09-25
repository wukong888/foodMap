package com.marketing.system.service.impl;

import com.marketing.system.entity.Permission;
import com.marketing.system.mapper.PermissionMapper;
import com.marketing.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PermissionServiceImpl implements PermissionService{
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> selectByMap(Map<String, Object> map) {
        return permissionMapper.selectByMap(map);
    }
}
