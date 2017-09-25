package com.marketing.system.service;

import com.github.pagehelper.Page;
import com.marketing.system.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {

    Role getRoleByName(Map<String,Object> map);

    List<Role> getUpProjectList(Map<String,Object> map);

    int sumAll(Map<String, Object> map);

    int setPassOrReject(Map<String, Object> map);
}
