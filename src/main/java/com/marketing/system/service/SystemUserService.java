package com.marketing.system.service;

import com.marketing.system.entity.SystemUser;

import java.util.List;
import java.util.Map;

public interface SystemUserService {

    List<SystemUser> selectByMap(Map<String,Object> map);

    boolean updateById(SystemUser userInfo);

    SystemUser selectByPrimaryKey(int id);

    List<Map<String, Object>> selectManagerBydepartment(Map<String,Object> map);

    List<Map<String, Object>> selectUserGroupBydepartment(Map<String,Object> map);

    SystemUser selectIdByName(String UserName);
}
