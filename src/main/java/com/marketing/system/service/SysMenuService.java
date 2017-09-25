package com.marketing.system.service;

import java.util.List;
import java.util.Map;

public interface SysMenuService {

    List<Map<String, Object>> fetchRoleMenus(int roleId,int SystemId);

    List<Map<String, Object>> fetchAllMenu(int SystemId);

    int addMenu(Map<String,Object> map);

    int updateMenu(Map<String,Object> map);
}
