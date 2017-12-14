package com.marketing.system.mapper;

import com.marketing.system.entity.RoleMenus;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleMenusMapper {
    int deleteByPrimaryKey(RoleMenus key);

    int insert(RoleMenus record);

    int insertSelective(RoleMenus record);

    List<Map<String,Object>> getRoleSysMenu(int roleId);

   // List<Map<String,Object>> getMenuParentId(@Param(value="menuLeafIds") String[] menuLeafIds,int SystemId);
    List<Map<String,Object>> getMenuParentId(Map<String,Object> map);

   // List<Map<String,Object>> getMenuByParentIds(@Param(value="ids") String[] ids,int SystemId);
    List<Map<String,Object>> getMenuByParentIds(Map<String,Object> map);

    List<Map<String,Object>> fetchAllMenu(int SystemId);

    int addMenu(Map<String,Object> map);

    int updateMenu(Map<String,Object> map);

    List<Map<String,Object>> getButton();

    List<Map<String,Object>> getHomePageModule();



}