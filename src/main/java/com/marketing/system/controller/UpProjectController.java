package com.marketing.system.controller;

import com.marketing.system.entity.*;
import com.marketing.system.mapper.DepartmentNewMapper;
import com.marketing.system.mapper.SystemUserMapper;
import com.marketing.system.mapper_two.ProjectInfoMapper;
import com.marketing.system.mapper_two.ProjectSubtaskMapper;
import com.marketing.system.mapper_two.ProjectTaskMapper;
import com.marketing.system.service.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.marketing.system.util.WeiXinPushUtil.httpPostWithJSON;

@Api(description = "立项待审批接口", value = "立项待审批接口")
@Scope("prototype")
@RestController
//@Controller
@EnableAutoConfiguration
public class UpProjectController {

    private Logger logger = LoggerFactory.getLogger(UpProjectController.class);

    @Autowired
    private UpProjectService upProjectService;

    @Autowired
    private MembersService membersService;

    @Autowired
    private GroupService groupService;

    @Autowired
    ProjectSubtaskMapper projectSubtaskMapper;

    @Autowired
    ProjectTaskMapper projectTaskMapper;

    @Autowired
    SystemUserService systemUserService;

    @Autowired
    ProjectInfoMapper projectInfoMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DepartmentNewMapper departmentNewMapper;

    @Autowired
    private IndexService indexService;

    @Value("${ceo.id}")
    private String ceoId;

    @Value("${ceo.phone}")
    private String ceoPhone;

    @Value("${ceo.email}")
    private String ceoEmail;

    /**
     * 查询立项待审批列表
     *
     * @return
     */
    @ApiOperation(value = "查询立项待审批列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "createrSquadId", value = "项目发起部门", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "creater", value = "创建人", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createDateStart", value = "项目发起开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createDateEnd", value = "项目发起结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planSDateStart", value = "预计上线开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planSDateEnd", value = "预计上线结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proType", value = "项目类型", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proName", value = "项目名称", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getUpProjectList", method = RequestMethod.POST)
    public ApiResult<List<ProjectInfo>> getUpProjectList(
            @RequestParam(value = "current") int current,
            @RequestParam(value = "createrSquadId", required = false) String createrSquadId,
            @RequestParam(value = "creater", required = false) String creater,
            @RequestParam(value = "createDateStart", required = false) String createDateStart,
            @RequestParam(value = "createDateEnd", required = false) String createDateEnd,
            @RequestParam(value = "planSDate", required = false) String planSDate,
            @RequestParam(value = "planSDateStart", required = false) String planSDateStart,
            @RequestParam(value = "planSDateEnd", required = false) String planSDateEnd,
            @RequestParam(value = "proType", required = false) String proType,
            @RequestParam(value = "proName", required = false) String proName,
            @RequestParam(value = "pageSize") int pageSize) {

        ApiResult<List<ProjectInfo>> result = null;

        try {
            Map<String, Object> map = new HashMap<>();

            map = ToolUtil.putMap(current, createrSquadId, creater, "1", createDateStart, createDateEnd, planSDateStart, planSDateEnd, proType, proName);

            List<ProjectInfo> projectInfos = upProjectService.getProjectInfoList(map);

            //增加排序序号
            /*for (int i = 0; i < projectInfos.size(); i++) {
                projectInfos.get(i).setIndex((current - 1) * pageSize + i + 1);
            }*/

            RdPage rdPage = new RdPage();

            Map<String, Object> mapT = new HashMap<>();
            int sum = 0;
            sum = projectInfos.size();
            projectInfos = ToolUtil.listSplit2(current, pageSize, projectInfos);

            mapT.put("proState", "1");//项目状态(1:立项待审批
            //int sum = upProjectService.sumAll(mapT);
            //分页信息
            rdPage.setTotal(sum);
            rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
            rdPage.setCurrent(current);
            rdPage.setPageSize(pageSize);

            String msg = "";
            if (current > rdPage.getPages() && sum != 0) {
                msg = "已经超过当前所有页数！";
                result = new ApiResult<List<ProjectInfo>>(Constant.FAIL_CODE_VALUE, msg, null, rdPage);
            } else {

                if (projectInfos.size() > 0) {
                    msg = "查询成功！";
                    result = new ApiResult<List<ProjectInfo>>(Constant.SUCCEED_CODE_VALUE, msg, projectInfos, rdPage);
                } else {
                    msg = "没有相关数据！";
                    result = new ApiResult<List<ProjectInfo>>(Constant.SUCCEED_CODE_VALUE, msg, projectInfos, null);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询立项待审批列表错误信息：" + e);
        }
        return result;

    }

    /**
     * 项目操作
     *
     * @param id
     * @param proState
     * @return
     */
    @ApiOperation(value = "项目操作")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "立项待审批id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "creatName", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "explain", value = "原因", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proState", value = "操作日志类型(1:立项待审批，2：开发中，3：上线待审批，4：完成，5：驳回，6：作废)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "登录用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "rejectState", value = "区分驳回（1：立项待审批驳回 2：上线待审批驳回）", required = false, dataType = "String")
    })
    @RequestMapping(value = "/passOrReject", method = RequestMethod.POST)
    public ApiResult<String> passOrReject(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "creatName") String creatName,
            @RequestParam(value = "explain") String explain,
            @RequestParam(value = "proState") String proState,
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "rejectState", required = false) String rejectState) {

        ApiResult<String> result = null;

        String reBoolean = ToolUtil.cacheExist(explain);
        if (reBoolean.equals("full")) {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.AGAINCOMMIT_FAIL, null, null);
            return result;
        }
        SystemUser user = systemUserService.selectByPrimaryKey(userId);

        //验证是否ceo权限操作
        if (!user.getDuty().equals("CEO") && (proState == "2" || proState == "5" || proState == "6")) {
            result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.NOTCEO_FAIL, null, null);
            return result;
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("proState", proState);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            map.put("finishDate", str);

            int i = 0;
            //申请的项目审批通过或者驳回
            //1：立项待审批驳回
            if (proState.equals("2")) {
                map.put("againState", 2);//开发中
                i = upProjectService.setPassOrRejectTwo(map);
                //驳回 1：立项待审批驳回 2：上线待审批驳回
            } else if (proState.equals("5")) {
                if (rejectState.equals("1")) {
                    //&& (rejectState.equals("1") || rejectState.equals("2"))
                    map.put("againState", null);//开发中
                    i = upProjectService.setPassOrRejectTwo(map);
                } else if (rejectState.equals("2")) {
                    map.put("againState", 2);//开发中
                    i = upProjectService.setPassOrRejectTwo(map);
                }

            } else if (proState.equals("3")) { //上线带审批
                map.put("againState", null);//开发中
                i = upProjectService.setPassOrRejectTwo(map);
            } else {
                i = upProjectService.setPassOrReject(map);
            }

            //上线待审批更新任务和子任务进度
            if (proState.equals("3")) {
                //任务
                boolean updateTaskProgress = projectTaskMapper.updateTaskProgress(proId);

                //子任务
                List<Map<String, Object>> updateSubtaskProgress = projectSubtaskMapper.selectProSubtaskByProId(proId);

                String mentIds = StringUtil.toString(MapUtil.collectProperty(updateSubtaskProgress, "taskId"));
                String[] mIds = mentIds.split(",");
                Map<String, Object> mapTid = new HashMap<>();

                mapTid.put("mentIds", mIds);

                //更新任务相关子任务进度为100
                boolean updateSubtaskProgressResult = projectSubtaskMapper.updateSubtaskProgress(mapTid);

            }

            ProLogRecord proLogRecord = new ProLogRecord();
            proLogRecord.setDate(str);//时间
            proLogRecord.setExplain(explain);//说明
            proLogRecord.setEmp(creatName);//操作人
            proLogRecord.setType(proState);//类型
            proLogRecord.setProid(proId);//项目id

            //插入日志
            int ilog = upProjectService.insertProLogRecord(proLogRecord);

            //SystemUser systemUser = systemUserService.selectIdByName(creatName);
            //ProjectInfo projectInfo = projectInfoMapper.getProjectInfoByProId(proId);
            ProjectInfo projectInfo = projectInfoMapper.selectByPrimaryKey(id);

            java.util.Date date2 = new java.util.Date();
            String str2 = sdf.format(date2);

            //发起小组
            String group = departmentNewMapper.getGroupByCreater(projectInfo.getCreater());

            //3、上线待审批
            Integer sx_cp = indexService.getSxProjects("");

            //3、上线待审批
            Integer sx_hd = indexService.getHdSxProjects("");

            Integer sx = sx_cp + sx_hd;


            if (i > 0 && ilog > 0) {
                result = new ApiResult<String>(Constant.SUCCEED_CODE_VALUE, "操作成功！", null, null);

                //项目类型(1:产品，2：活动)
                String proTypeName = "";
                if (projectInfo.getProtype().equals("1")) {
                    proTypeName = "产品";
                } else {
                    proTypeName = "活动";
                }
                long betweenDays = ToolUtil.getBetweenTimes(projectInfo.getPlanedate());

                String yuqi = "";
                if (betweenDays > 0) {
                    yuqi = "否";
                } else {
                    yuqi = "是";
                }

                String postUrl = "";
                //(1:立项待审批，2：开发中，3：上线待审批，4：完成，5：驳回，6：作废)
                if (Integer.valueOf(proState) == 2 || Integer.valueOf(proState) == 4 || Integer.valueOf(proState) == 5) {
                    /*postUrl = "{\"Uid\":" + projectInfo.getUserId() + ",\"Content\":\"创建人:" + creatName
                            + "\\n\\n项目名称:" + projectInfo.getProname() + "\\n\\n内容:" + explain
                            + "\\n\\n推送时间:" + str2
                            + "\",\"AgentId\":1000011,\"Title\":\"创建\",\"Url\":\"\"}";*/
                    /*postUrl = "{\"Uid\":" + projectInfo.getUserId() + ",\"Content\":\"您有关于《" + projectInfo.getProname() + "》的上线申请，请及时处理。"
                            + "\\n\\n发起小组:" + group
                            + "\\n\\n发起人:" + projectInfo.getCreater()
                            + "\\n\\n项目名称:" + projectInfo.getProname()
                            + "\\n\\n项目类型:" + proTypeName
                            + "\\n\\n发起时间:" + projectInfo.getCreatedate()
                            + "\\n\\n是否逾期:" + yuqi
                            + "\\n\\n上线审批总量:" + sx + "个"
                            + "\\n\\n推送时间:" + str2
                            + "\",\"AgentId\":1000011,\"Title\":\"创建\",\"Url\":\"\"}";*/

                } else if (Integer.valueOf(proState) == 3) {
                    postUrl = "{\"Uid\":" + ceoId + ",\"Content\":\"【项目上线】\\n\\n您有关于《" + projectInfo.getProname() + "》的上线申请，请及时处理。"
                            + "\\n\\n发起小组:" + group
                            + "\\n\\n发起人:" + projectInfo.getCreater()
                            + "\\n\\n项目名称:" + projectInfo.getProname()
                            + "\\n\\n项目类型:" + proTypeName
                            + "\\n\\n发起时间:" + projectInfo.getCreatedate()
                            + "\\n\\n上线审批总量:" + sx + "个"
                            + "\\n\\n推送时间:" + str2
                            + "\",\"AgentId\":1000011,\"Title\":\"创建\",\"Url\":\"\"}";
                }

                try {
                    //消息推送-回复
                    httpPostWithJSON(postUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //上线待审批，发送短信
                if (Integer.valueOf(proState) == 3) {

                    //数据研发中心柏铭成向您申请对《大数据分析平台项目》实施项目立项，请您及时处理。
                    ToolUtil.sendMsg(ceoPhone, group + projectInfo.getCreater() + "向您申请对《" + projectInfo.getProname() + "》实施项目上线，请您及时处理。");

                    //发送邮件
                    ToolUtil.sendEmial(ceoEmail, "关于《" + projectInfo.getProname() + "》的上线申请", "陈总:<br> " + group + projectInfo.getCreater() + "向您发起名为《" + projectInfo.getProname() + "》的上线申请，该项目类型为" + proTypeName + "，此项目按照要求时间上线（要求上线时间为" + projectInfo.getPlanedate() + ")。请您及时处理。项目简介如下：<br>" +
                            projectInfo.getProdeclare() + "<br>" +
                            "点击进入项目审批页：https://192.168.11.132:2222<br>" +
                            "注：您目前还有" + sx + "个未处理的上线申请。<br>");
                }

            } else {
                result = new ApiResult<String>(Constant.FAIL_CODE_VALUE, "操作失败,请检查是否是立项待审批项目！", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("项目操作错误信息：" + e);

        }
        return result;

    }

    /**
     * 立项待审批详情页
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "立项待审批详情页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "立项待审批项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getUpDetails", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getUpDetails(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {
            //基本信息+项目信息Basic Information
            ProjectInfo projectInfo = upProjectService.selectByPrimaryKey(id);

            Map<String, Object> mapTx = new HashMap<>();
            mapTx.put("Name", "特殊组员");
            mapTx.put("SystemId", 3);

            Role role = roleService.getRoleByName(mapTx);

            if (role.getRemark().contains(String.valueOf(id))) {
                projectInfo.setDuty("组员");
            } else {
                projectInfo.setDuty("CEO");
            }

            //日志记录
            List<ProLogRecord> logRecordList = upProjectService.getProLogRecordList(proId);
            for (ProLogRecord proLogRecord : logRecordList) {
                if (proLogRecord.getType() == "创建") {
                    proLogRecord.setFilepath(projectInfo.getProfilepath());//申请创建项目附件路径
                }
            }
            //参与组
            List<ProjectTask> taskList = upProjectService.getProjectTaskList(proId);

            Double sum = 0.0;
            for (ProjectTask projectTask : taskList) {
                projectTask.getWorkDate();
                if (projectTask.getWorkDate() != "" && projectTask.getWorkDate() != null)
                    //sum += Integer.valueOf(projectTask.getWorkDate());
                    sum += Double.parseDouble(String.valueOf(projectTask.getWorkDate()));
                //Group group = groupService.getGroupBySquadId(Integer.valueOf(projectTask.getSquadId()));
                DepartmentNew departmentNew = departmentNewMapper.getDeptnoBySquadId(Integer.valueOf(projectTask.getSquadId()));

                projectTask.setSquadId(String.valueOf(departmentNew.getDeptno()));//根据id取对应小组中文名

            }
            projectInfo.setWorkTatalDay(String.valueOf(sum));//项目预计工期（任务工期之和）

            map.put("projectInfo", projectInfo); //基本信息+项目信息
            map.put("logRecordList", logRecordList);//日志记录
            map.put("taskList", taskList);//参与组

            list.add(map);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("立项待审批详情页错误信息：" + e);
        }

        return result;
    }


    /**
     * 根据部门id查找项目发起人
     *
     * @param squadId
     * @return
     */
    @ApiOperation(value = "根据部门id查找项目发起人")
    @ApiImplicitParam(paramType = "query", name = "squadId", value = "小组id", required = true, dataType = "Integer")
    @RequestMapping(value = "/getMembersBySquadId", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getMembers(@RequestParam(value = "squadId") int squadId) {

        ApiResult<List<Map<String, Object>>> result = null;

        try {
            //List<DepartmentNew> list = membersService.getMembersById(String.valueOf(squadId));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("UserGroupId", squadId);
            List<Map<String, Object>> list = new ArrayList<>();
            list = systemUserService.getMembersById(String.valueOf(squadId));

            //list = list.stream().filter(x -> x.get("UserGroupId").equals(squadId)).collect(Collectors.toList());

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);

            logger.error("结果list：" + list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("错误信息：" + e);
        }
        return result;

    }

}
