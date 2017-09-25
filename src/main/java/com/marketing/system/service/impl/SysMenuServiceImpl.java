package com.marketing.system.service.impl;

import com.marketing.system.mapper.RoleMenusMapper;
import com.marketing.system.service.SysMenuService;
import com.marketing.system.util.ListUtil;
import com.marketing.system.util.MapUtil;
import com.marketing.system.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private RoleMenusMapper roleMenusMapper;

    @Override
    public List<Map<String, Object>> fetchRoleMenus(int roleId,int SystemId) {

        //叶菜单
        List<Map<String, Object>> leafMenus = roleMenusMapper.getRoleSysMenu(roleId);

        String menuLeafIds = StringUtil.toString(MapUtil.collectProperty(leafMenus, "Mid"));

        String ids = StringUtil.toString(queryTreeNodeIds(menuLeafIds,SystemId));

        String[] idsArray = ids.split(",");

        Map<String,Object> map = new HashMap<>();

        map.put("ids",idsArray);
        map.put("SystemId",SystemId);

        List<Map<String, Object>> menuList = roleMenusMapper.getMenuByParentIds(map);

        menuList = ListUtil.list2Tree(menuList, "value", "ParentId");
        return menuList;

    }

    @Override
    public List<Map<String, Object>> fetchAllMenu(int SystemId) {
        return roleMenusMapper.fetchAllMenu(SystemId);
    }

    @Override
    public int addMenu(Map<String, Object> map) {

        return roleMenusMapper.addMenu(map);
    }

    @Override
    public int updateMenu(Map<String, Object> map) {

        return roleMenusMapper.updateMenu(map);
    }


    public List queryTreeNodeIds(String leafIds,int SystemId) {
        List<Map<String, Object>> parents;
        String menuLeafIds = leafIds;

        String[] Ids = menuLeafIds.split(",");

        List rIds = new ArrayList();

        Map<String,Object> map = new HashMap<>();
        do {
            map.put("menuLeafIds",Ids);
            map.put("SystemId",SystemId);
            parents = roleMenusMapper.getMenuParentId(map);
            rIds.addAll(parents);
            menuLeafIds = StringUtil.toString(MapUtil.collectProperty(parents, "id", false));
            Ids = menuLeafIds.split(",");
        } while (!parents.isEmpty());

        List rlist = MapUtil.collectProperty(rIds, "id", false);

        for (String id : leafIds.split(",")) {
            rlist.add(id);
        }

        return rlist;
    }


}
