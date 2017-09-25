package com.marketing.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.marketing.system.entity.Role;
import com.marketing.system.mapper.RoleMapper;
import com.marketing.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role getRoleByName(Map<String,Object> map) {

        return roleMapper.getRoleByName(map);
    }

    @Override
    public List<Role> getUpProjectList(Map<String,Object> map) {
        //PageHelper.startPage(current, pageSize);

        List<Role> page = roleMapper.getUpProjectList(map);
        return page;
    }


    @Override
    public int sumAll(Map<String, Object> map) {
        int i = roleMapper.sumAll(map);
        return i;
    }

    @Override
    public int setPassOrReject(Map<String, Object> map) {
        int i = roleMapper.setPassOrReject(map);
        return i;
    }
}
