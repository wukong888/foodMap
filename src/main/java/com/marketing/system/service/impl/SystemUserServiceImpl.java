package com.marketing.system.service.impl;

import com.marketing.system.entity.SystemUser;
import com.marketing.system.mapper.SystemUserMapper;
import com.marketing.system.service.SystemUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SystemUserServiceImpl implements SystemUserService{

    @Resource
    private SystemUserMapper systemUserMapper;

    @Override
    public List<SystemUser> selectByMap(Map<String, Object> map) {

        return systemUserMapper.selectByMap(map);
    }

    @Override
    public boolean updateById(SystemUser userInfo) {
        return false;
    }

    @Override
    public SystemUser selectByPrimaryKey(int id) {
        return systemUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Map<String, Object>> selectManagerBydepartment(Map<String,Object> map) {

        List<Map<String, Object>> list = systemUserMapper.selectManagerBydepartment(map);

        return list;
    }

    @Override
    public List<Map<String, Object>> selectUserGroupBydepartment(Map<String, Object> map) {

        List<Map<String, Object>> list = systemUserMapper.selectUserGroupBydepartment(map);

        return list;
    }
}
