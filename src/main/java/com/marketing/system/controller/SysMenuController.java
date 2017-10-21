package com.marketing.system.controller;

import com.marketing.system.entity.Role;
import com.marketing.system.entity.SystemUser;
import com.marketing.system.service.ApplyService;
import com.marketing.system.service.RoleService;
import com.marketing.system.service.SysMenuService;
import com.marketing.system.service.SystemUserService;
import com.marketing.system.util.ApiResult;
import com.marketing.system.util.Constant;
import com.marketing.system.util.ListUtil;
import com.marketing.system.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "用户菜单接口", value = "用户菜单接口")
@Scope("prototype")
@RestController
//@Controller
@EnableAutoConfiguration
public class SysMenuController {

    private Logger logger = LoggerFactory.getLogger(SysMenuController.class);

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private SystemUserService systemUserService;

    /**
     * 根据角色查询菜单
     *
     * @return
     */
    @ApiOperation(value = "根据角色查询菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "SystemId", value = "系统Id,3:项目管理系统", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "登录用户id", required = true, dataType = "int")
    })
    @RequestMapping(value = "/findRoleMenu", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> fetchRoleMenu(
            @RequestParam(value = "SystemId", required = true) int SystemId,
            @RequestParam(value = "id", required = true) int id) {

        ApiResult<List<Map<String, Object>>> result = null;

        SystemUser user = systemUserService.selectByPrimaryKey(id);

        Map<String, Object> map = new HashMap<>();

        //职位，当用户申请项目成功后，该用户成为项目发起人
        String name = user.getDuty();

        List<String> list = applyService.getCreaterByName(user.getUserName());
        if (!StringUtil.isEmpty(name) && name != "") {
            if (list.size() > 0 && !user.getDuty().equals("CEO")) {
                name = "项目发起人";
            } else {
                if (name.contains("组长") || name.contains("经理")) {
                    name = "经理/组长";
                }else if(name.contains("CEO")){
                    name = "CEO";
                } else {
                    name = "组员";
                }
            }
        } else {
            name = "组员";
        }

        map.put("Name", name);
        map.put("SystemId", SystemId);

        Role role = roleService.getRoleByName(map);

        if (role != null) {
            Map<String, Object> mapMenu = new HashMap<>();
            Map<String, Object> mapCreater = new HashMap<>();

            List<Map<String, Object>> listNew = new ArrayList<Map<String, Object>>();

            mapMenu.put("roleId", role.getId());
            mapMenu.put("SystemId", SystemId);

            List<Map<String, Object>> menus = sysMenuService.fetchRoleMenus(role.getId(), SystemId);

            mapCreater.put("duty",name);
            mapCreater.put("menus",menus);
            listNew.add(mapCreater);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, "查询成功！", listNew, null);
        } else {
            result = new ApiResult<>(Constant.FAIL_CODE_VALUE, "数据出错,未找到对应角色！", null, null);
        }

        return result;

    }

    /**
     * 查询用户权限
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "查询用户权限")
    @ApiImplicitParam(paramType = "query", name = "SystemId", value = "系统Id,3:项目管理系统", required = true, dataType = "int")
    @RequestMapping(value = "/findAllMenu", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> findAllMenu(HttpServletRequest request, HttpServletResponse response,
                                                            @RequestParam(value = "SystemId", required = true) int SystemId) throws Exception {
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = sysMenuService.fetchAllMenu(SystemId);
        list = ListUtil.list2Tree(list, "value", "parentId");
        list = ListUtil.treeForExt(list, null, null, true);

        result = new ApiResult<>(200, "查询成功", list, null);

        return result;

    }

    /**
     * 新增修改菜单
     *
     * @param response
     * @param status
     * @throws Exception
     */
    @ApiOperation(value = "新增修改菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "Name", value = "菜单名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "Url", value = "路径", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "GroupId", value = "GroupId", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "Px", value = "Px", required = false, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "ParentId", value = "父节点id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "Scriptid", value = "脚本名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "SystemId", value = "系统id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "增加or修改", required = true, dataType = "Integer")
    })
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
    public ApiResult<String> update(HttpServletResponse response,
                                    @RequestParam(value = "id", required = true) String id,
                                    @RequestParam(value = "Name", required = true) String Name,
                                    @RequestParam(value = "Url", required = false) String Url,
                                    @RequestParam(value = "GroupId", required = false) String GroupId,
                                    @RequestParam(value = "Px", required = false) String Px,
                                    @RequestParam(value = "ParentId", required = true) String ParentId,
                                    @RequestParam(value = "Scriptid", required = true) String Scriptid,
                                    @RequestParam(value = "SystemId", required = true) String SystemId,
                                    @RequestParam(value = "status", required = false) String status) throws Exception {

        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> responseMap = new HashMap<String, Object>();

        dataMap.put("id", id);
        dataMap.put("Name", Name);
        dataMap.put("Url", Url);
        dataMap.put("GroupId", GroupId);
        dataMap.put("Px", Px);
        dataMap.put("ParentId", ParentId);
        dataMap.put("Scriptid", Scriptid);
        dataMap.put("SystemId", SystemId);

        ApiResult<String> result = null;
        if ("create".equalsIgnoreCase(status)) {
            int n = sysMenuService.addMenu(dataMap);
            if (n > 0) {
                result = new ApiResult<>(200, "保存成功", null, null);
            } else {
                result = new ApiResult<>(400, "保存失败", null, null);
            }
        } else if ("update".equals(status)) {
            int total = sysMenuService.updateMenu(dataMap);
            if (total > 0) {
                result = new ApiResult<>(200, "保存成功", null, null);
            } else {
                result = new ApiResult<>(400, "保存失败", null, null);
            }
        }
        return result;
    }

}
