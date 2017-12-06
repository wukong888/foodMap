package com.marketing.system.controller;

import com.marketing.system.entity.*;
import com.marketing.system.mapper.DepartmentNewMapper;
import com.marketing.system.mapper.RoleMenusMapper;
import com.marketing.system.service.ApplyService;
import com.marketing.system.service.MyProjectService;
import com.marketing.system.service.SystemUserService;
import com.marketing.system.service.UpProjectService;
import com.marketing.system.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(description = "项目权限接口", value = "项目权限接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class AuthorityController {

    private Logger logger = LoggerFactory.getLogger(AuthorityController.class);

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private UpProjectService upProjectService;

    @Autowired
    private DepartmentNewMapper departmentNewMapper;

    @Autowired
    private RoleMenusMapper roleMenusMapper;

    @Autowired
    private MyProjectService myProjectService;

    /**
     * 我的项目开发中详情页
     * 基本信息+项目信息+参与组
     *
     * @param id
     * @param proId
     * @return
     */
    @ApiOperation(value = "项目详情页面按钮权限",notes = "返回说明，xm_sxsp：上线审批按钮，xm_tjkfrz：添加开发日志按钮，xm_tj：添加按钮，xm_xg：修改按钮，xm_sc：删除按钮，xm_xmzf：项目作废按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "我的项目主键id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "登录用户id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getAuthorityByUser", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getAuthorityByUser(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "userId") int userId) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            ProjectInfo projectInfo = upProjectService.selectByPrimaryKey(id);

            SystemUser user = systemUserService.selectByPrimaryKey(userId);

/*******************************************对应组成员 开始***************************************************************/

            /**
             * 判断是总监、经理、组员
             * listDuty = 1 经理
             * listDuty > 1 总监
             * listDuty 为空 组员
             */
            List<Map<String, Object>> listDuty = departmentNewMapper.getCheckDuty(user.getUserGroupId());

            List<Map<String, Object>> listMem = new ArrayList<>();
            if (listDuty.size() > 1) {
                listMem = departmentNewMapper.getZjMember(user.getUserGroupId());
            } else if (listDuty.size() == 1){
                listMem = departmentNewMapper.getJlMember(user.getUserGroupId());
            } else if (listDuty.size() == 0 ){
                listMem = departmentNewMapper.getMemMember(user.getUserGroupId());
            }

            Map<String, Object> mapMem = new HashMap<>();

            mapMem = ToolUtil.getmapList(listMem, "id");

            List<Map<String, Object>> listMembers = systemUserService.getMembersByUserGroupId(mapMem);

            String menuLeafIdsmember = StringUtil.toString(MapUtil.collectProperty(listMembers, "UserName"));

/*******************************************对应组成员 结束******************************************************************/

            //是否有子任务
            List<Map<String, Object>> projectSubtaskList = applyService.selectProSubtaskByProId(proId);

            //获取菜单按钮权限
            List<Map<String, Object>> buttonList = roleMenusMapper.getButton();

            //项目详情页按钮
            buttonList = buttonList.stream().filter(x ->x.get("Url").toString().contains("xm_")).collect(Collectors.toList());

            if (projectSubtaskList.size() > 0) {
                for (Map map1 : projectSubtaskList) {
                    for (Map mapButton : buttonList) {
                        if (user.getUserName().equals(projectInfo.getCreater()) && !user.getDuty().equals("CEO")) {
                            //projectInfo.setDuty("项目发起人");
                            map.put(mapButton.get("Url").toString(),"true");
                        } else if (!user.getUserName().equals(projectInfo.getCreater()) && user.getUserName().equals(map1.get("subtaskHandler"))) {
                            //projectInfo.setDuty("组员");
                            map.put(mapButton.get("Url").toString(),"false");
                            if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && menuLeafIdsmember.contains(projectInfo.getCreater())) {
                                //projectInfo.setDuty("经理/组长");
                                map.put(mapButton.get("Url").toString(),"true");
                            }
                        } else if (user.getDuty().equals("CEO")) {
                            //projectInfo.setDuty("CEO");
                            map.put(mapButton.get("Url").toString(),"true");
                        } else if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && menuLeafIdsmember.contains(projectInfo.getCreater())) {
                            //projectInfo.setDuty("经理/组长");
                            map.put(mapButton.get("Url").toString(),"true");
                        } else if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && !menuLeafIdsmember.contains(projectInfo.getCreater())) {
                            //projectInfo.setDuty("组员");
                            map.put(mapButton.get("Url").toString(),"false");
                        } else {
                            map.put(mapButton.get("Url").toString(),"false");
                        }
                    }
                }
            } else {
                for (Map mapButton : buttonList) {
                    if (user.getUserName().equals(projectInfo.getCreater()) && !user.getDuty().equals("CEO")) {
                        //projectInfo.setDuty("项目发起人");
                        map.put(mapButton.get("Url").toString(),"true");
                    }  else if (user.getDuty().equals("CEO")) {
                        //projectInfo.setDuty("CEO");
                        map.put(mapButton.get("Url").toString(),"true");
                    } else if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && menuLeafIdsmember.contains(projectInfo.getCreater())) {
                       //projectInfo.setDuty("经理/组长");
                        map.put(mapButton.get("Url").toString(),"true");
                    } else if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && !menuLeafIdsmember.contains(projectInfo.getCreater())) {
                        //projectInfo.setDuty("组员");
                        map.put(mapButton.get("Url").toString(),"false");
                    } else {
                        //projectInfo.setDuty("组员");
                        map.put(mapButton.get("Url").toString(),"false");
                    }
                }

            }
            list.add(map);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("项目详情页面按钮权限错误信息：" + e);
        }

        return result;
    }

    /**
     * 任务详情页按钮权限
     * @param taskId
     * @param proId
     * @return
     */
    @ApiOperation(value = "任务详情页按钮权限",notes = "返回说明，rw_tjrw：提交任务按钮，rw_tjkfrz：添加开发日志按钮，rw_tj：添加按钮，rw_xg：修改按钮，rw_sc：删除按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "登录用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer") })
    @RequestMapping(value = "/getTaskAuthorityByUser", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getTaskAuthorityByUser(
            @RequestParam(value = "taskId") int taskId,
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "userId") int userId) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {
            ProjectInfo projectInfo = myProjectService.getProjectInfoByProId(proId);


            SystemUser user = systemUserService.selectByPrimaryKey(userId);

/*******************************************对应组成员 开始***************************************************************/
            /**
             * 判断是总监、经理、组员
             * listDuty = 1 经理
             * listDuty > 1 总监
             * listDuty 为空 组员
             */
            List<Map<String, Object>> listDuty = departmentNewMapper.getCheckDuty(user.getUserGroupId());

            List<Map<String, Object>> listMem = new ArrayList<>();
            if (listDuty.size() > 1) {
                listMem = departmentNewMapper.getZjMember(user.getUserGroupId());
            } else if (listDuty.size() == 1){
                listMem = departmentNewMapper.getJlMember(user.getUserGroupId());
            } else if (listDuty.size() == 0 ){
                listMem = departmentNewMapper.getMemMember(user.getUserGroupId());
            }

            Map<String, Object> mapMem = new HashMap<>();

            mapMem = ToolUtil.getmapList(listMem, "id");

            List<Map<String, Object>> listMembers = systemUserService.getMembersByUserGroupId(mapMem);

            String menuLeafIdsmember = StringUtil.toString(MapUtil.collectProperty(listMembers, "UserGroupId"));

/*******************************************对应组成员 结束******************************************************************/

            //基本信息+任务信息Basic Information
            ProjectTask projectTask = myProjectService.getProjectTaskByTaskId(taskId);

            //获取菜单按钮权限
            List<Map<String, Object>> buttonList = roleMenusMapper.getButton();

            //项目详情页按钮
            buttonList = buttonList.stream().filter(x ->x.get("Url").toString().contains("rw_")).collect(Collectors.toList());

            for (Map mapButton : buttonList) {
                if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && menuLeafIdsmember.contains(projectTask.getSquadId())) {
                    //projectTask.setDuty("经理/组长");
                    map.put(mapButton.get("Url").toString(),"true");
                } else {
                    //projectTask.setDuty("组员");
                    map.put(mapButton.get("Url").toString(),"false");
                }
                if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
                    if (user.getDuty().equals("CEO")) {
                        //projectTask.setDuty("CEO");
                        map.put(mapButton.get("Url").toString(),"true");
                    }
                }
                if (projectInfo.getCreater().equals(user.getUserName())) {
                    //projectTask.setDuty("项目发起人");
                    map.put(mapButton.get("Url").toString(),"true");
                }
            }

            list.add(map);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("任务详情页按钮权限：" + e);
        }
        return result;
    }

    /**
     * 子任务详情页按钮权限
     * @param taskId
     * @return
     */
    @ApiOperation(value = "子任务详情页按钮权限",notes = "返回说明，sub_tjzrw：提交子任务按钮，sub_tjkfrz：添加开发日志按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "登录用户id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getSubTaskAuthorityByUser", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getSubTaskAuthorityByUser(
            @RequestParam(value = "taskId") int taskId,
            @RequestParam(value = "userId") int userId) {

        ApiResult<List<Map<String, Object>>> result = null;
        Map<String, Object> map = new HashMap<>();

        List<Map<String, Object>> listNew = new ArrayList<>();

        try {
            List<ProjectSubtask> list = myProjectService.getProjectSubtaskList(taskId);

            SystemUser user = systemUserService.selectByPrimaryKey(userId);

/*******************************************对应组成员 开始***************************************************************/

            /**
             * 判断是总监、经理、组员
             * listDuty = 1 经理
             * listDuty > 1 总监
             * listDuty 为空 组员
             */
            List<Map<String, Object>> listDuty = departmentNewMapper.getCheckDuty(user.getUserGroupId());

            List<Map<String, Object>> listMem = new ArrayList<>();
            if (listDuty.size() > 1) {
                listMem = departmentNewMapper.getZjMember(user.getUserGroupId());
            } else if (listDuty.size() == 1){
                listMem = departmentNewMapper.getJlMember(user.getUserGroupId());
            } else if (listDuty.size() == 0 ){
                listMem = departmentNewMapper.getMemMember(user.getUserGroupId());
            }

            Map<String, Object> mapMem = new HashMap<>();

            mapMem = ToolUtil.getmapList(listMem, "id");

            List<Map<String, Object>> listMembers = systemUserService.getMembersByUserGroupId(mapMem);

/*******************************************对应组成员 结束******************************************************************/

            for (ProjectSubtask projectSubtask : list) {

                //子任务权限：自己、自己组长、经理和ceo有权限

                List<Map<String, Object>> systemUserList = listMembers;

                List<Map<String, Object>> systemUserListNew = new ArrayList<>();

                for (Map sys : systemUserList) {
                    if (sys.get("duty") != "" && sys.get("duty") != null) {
                        if (String.valueOf(sys.get("duty")).contains("组长") || String.valueOf(sys.get("duty")).contains("经理")) {
                            systemUserListNew.add(sys);
                        }
                    } /*else {
                        projectSubtask.setDuty("组员");
                    }*/
                }
                String menuLeafIds = StringUtil.toString(MapUtil.collectProperty(systemUserListNew, "UserName"));

                //获取菜单按钮权限
                List<Map<String, Object>> buttonList = roleMenusMapper.getButton();

                //项目详情页按钮
                buttonList = buttonList.stream().filter(x ->x.get("Url").toString().contains("sub_")).collect(Collectors.toList());
                for (Map mapButton : buttonList) {
                    if (menuLeafIds.contains(user.getUserName())) {
                        //projectSubtask.setDuty("经理/组长");
                        map.put(mapButton.get("Url").toString(),"true");
                    } else {
                        //projectSubtask.setDuty("组员");
                        map.put(mapButton.get("Url").toString(),"false");
                    }
                    if (projectSubtask.getSubtaskhandler().equals(user.getUserName())) {
                        //projectSubtask.setDuty("项目发起人");
                        map.put(mapButton.get("Url").toString(),"true");
                    }
                    if (user.getDuty().equals("CEO")) {
                       //projectSubtask.setDuty("CEO");
                        map.put(mapButton.get("Url").toString(),"true");
                    }
                }
            }

            listNew.add(map);
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, listNew, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("子任务详情页按钮权限错误信息：" + e);
        }
        return result;

    }

}
