package com.marketing.system.controller;

import com.marketing.system.entity.Role;
import com.marketing.system.service.RoleService;
import com.marketing.system.util.ApiResult;
import com.marketing.system.util.Constant;
import com.marketing.system.util.RdPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "立项待审批接口", value = "立项待审批接口")
@Scope("prototype")
@RestController
//@Controller
@EnableAutoConfiguration
public class UpProjectController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RoleService roleService;

    /**
     * 查询立项待审批列表
     *
     * @return
     */
    @ApiOperation(value = "查询立项待审批列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "department", value = "项目发起部门", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getUpProjectList", method = RequestMethod.POST)
    public ApiResult<List<Role>> getUpProjectList(
            @RequestParam(value = "current") int current,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "pageSize") int pageSize) {

        ApiResult<List<Role>> result = null;

        Map<String, Object> map = new HashMap<>();
        map.put("current", current - 1);
        map.put("pageSize", pageSize);
        map.put("Name", department);

        List<Role> roles = roleService.getUpProjectList(map);

        //增加排序序号
        for (int i = 0; i < roles.size(); i++) {
            roles.get(i).setIndex((current - 1) * pageSize + i + 1);
        }

        RdPage rdPage = new RdPage();

        Map<String, Object> mapT = new HashMap<>();

        mapT.put("Name", department);
        int sum = roleService.sumAll(mapT);
        //分页信息
        rdPage.setTotal(sum);
        rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
        rdPage.setCurrent(current);
        rdPage.setPageSize(pageSize);

        String msg = "";
        if (current > rdPage.getPages()) {
            msg = "已经超过当前所有页数！";
            result = new ApiResult<List<Role>>(Constant.FAIL_CODE_VALUE, msg, null, rdPage);
        } else {
            msg = "查询成功！";
            result = new ApiResult<List<Role>>(Constant.SUCCEED_CODE_VALUE, msg, roles, rdPage);
        }

        return result;

    }

    /**
     * 立项待审批操作通过or驳回
     * @param id
     * @param handle
     * @return
     */
    @ApiOperation(value = "立项待审批操作通过or驳回")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "立项待审批id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "handle", value = "立项待审批操作0:通过 1:驳回", required = true, dataType = "String")
    })
    @RequestMapping(value = "/passOrReject")
    public ApiResult<String> passOrReject(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "handle") String handle) {

        ApiResult<String> result = null;

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("handle", handle);

        int i = roleService.setPassOrReject(map);

        if (i > 0) {
            result = new ApiResult<String>(Constant.SUCCEED_CODE_VALUE, "操作成功！", null, null);
        } else {
            result = new ApiResult<String>(Constant.FAIL_CODE_VALUE, "操作失败！", null, null);
        }

        return result;

    }

}
