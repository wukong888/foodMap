package com.marketing.system.controller;

import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectSubtask;
import com.marketing.system.entity.ProjectTask;
import com.marketing.system.entity.SystemUser;
import com.marketing.system.service.IndexService;
import com.marketing.system.service.SystemUserService;
import com.marketing.system.util.ApiResult;
import com.marketing.system.util.Constant;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "用户首页接口", value = "用户首页接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private IndexService indexService;

    @Autowired
    private SystemUserService systemUserService;

    /**
     * 用户首页
     *
     * @return
     */
    @ApiOperation(value = "用户首页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "登录用户id", required = true, dataType = "int")
    })
    @RequestMapping(value = "/getIndexInfo", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getIndexInfo(
            @RequestParam(value = "id", required = true) int id) throws ParseException {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {
            SystemUser user2 = (SystemUser) SecurityUtils.getSubject().getPrincipal();
            SystemUser user = systemUserService.selectByPrimaryKey(id);

            //职位，立项待审批、上线待审批暂时只有CEO有该权限
            String dutyName = user.getDuty();

            map.put("dutyName", dutyName);

            //项目推送
            //我申请的项目
            Integer i = indexService.getMyApplyProject(user.getUserName());

            //参与的项目 j 项目任务+子任务
            Integer j = indexService.getMyJoinProject(user.getUserName());

            //立项待审批
            String proState = "1";
            Integer k = indexService.getUpApplyProject(proState);

            //上线待审批
            String proTypeOnline = "3";
            Integer m = indexService.getUpApplyProject(proTypeOnline);

            map.put("applyProject", i);//我申请的项目
            map.put("joinProject", j);//参与的项目
            map.put("upProject", k);//立项待审批
            map.put("onlineProject", k);//上线待审批

            String creater = "";

            if ("CEO".equals(dutyName)) {
                creater = "";
            } else {
                creater = user.getUserName();
            }
            //逾期提示 1：项目

            List<ProjectInfo> infoList = indexService.getProjectInfoList(creater);

            //逾期提示 2：任务

            List<Map<String, Object>> taskList = indexService.getProjectTaskList(creater);

            //逾期提示 3：子任务

            List<ProjectSubtask> subtaskList = indexService.getProjectSubTaskList(creater);

            map.put("infoList", infoList);//项目
            map.put("taskList", taskList);//任务
            map.put("subtaskList", subtaskList);//子任务

            list.add(map);
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);
            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户首页错误信息：" + e);
        }
        return result;
    }

}
