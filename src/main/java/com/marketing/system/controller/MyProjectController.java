package com.marketing.system.controller;


import com.marketing.system.entity.*;
import com.marketing.system.service.ApplyService;
import com.marketing.system.service.GroupService;
import com.marketing.system.service.MyProjectService;
import com.marketing.system.service.UpProjectService;
import com.marketing.system.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.MarshalledObject;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(description = "我的项目接口", value = "我的项目接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class MyProjectController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MyProjectService myProjectService;

    @Autowired
    private UpProjectService upProjectService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ApplyService applyService;


    /**
     * 查询我的项目列表
     * ceo查看全部项目
     *
     * @return
     */
    @ApiOperation(value = "查询我的项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "createrSquadId", value = "项目发起部门", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "creater", value = "创建人", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proState", value = "项目状态(1:立项待审批，2：开发中，3：上线带审批，4：完成，5：驳回，6：作废)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createDateStart", value = "项目发起开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createDateEnd", value = "项目发起结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planSDateStart", value = "预计完成开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planSDateEnd", value = "预计完成结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proType", value = "项目类型", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proName", value = "项目名称", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getMyProjectList", method = RequestMethod.POST)
    public ApiResult<List<ProjectInfo>> getMyProjectList(
            @RequestParam(value = "current") int current,
            @RequestParam(value = "createrSquadId", required = false) String createrSquadId,
            @RequestParam(value = "creater", required = false) String creater,
            @RequestParam(value = "proState", required = false) String proState,
            @RequestParam(value = "createDateStart", required = false) String createDateStart,
            @RequestParam(value = "createDateEnd", required = false) String createDateEnd,
            @RequestParam(value = "planSDateStart", required = false) String planSDateStart,
            @RequestParam(value = "planSDateEnd", required = false) String planSDateEnd,
            @RequestParam(value = "proType", required = false) String proType,
            @RequestParam(value = "proName", required = false) String proName,
            @RequestParam(value = "pageSize") int pageSize) {

        ApiResult<List<ProjectInfo>> result = null;

        Map<String, Object> map = new HashMap<>();

        map.put("current", current);
        map.put("pageSize", pageSize);
        map.put("createrSquadId", createrSquadId);//项目发起部门
        map.put("creater", creater);//创建人
        map.put("proState", proState);//项目状态(1:立项待审批，2：开发中，3：上线带审批，4：完成，5：驳回，6：作废）
        if (createDateStart == "" || createDateStart == null) {
            map.put("createDateStart", "1980-01-01 00:00:00");//项目发起开始时间
        } else {
            map.put("createDateStart", createDateStart);//项目发起开始时间
        }

        if (createDateEnd == "" || createDateEnd == null) {
            map.put("createDateEnd", "2999-01-01 00:00:00");//项目发起结束时间
        } else {
            map.put("createDateEnd", createDateEnd);//项目发起结束时间
        }

        if (planSDateStart == "" || planSDateStart == null) {
            map.put("planSDateStart", "1980-01-01 00:00:00");//预计上线开始时间
        } else {
            map.put("planSDateStart", planSDateStart);//预计上线开始时间
        }

        if (planSDateEnd == "" || planSDateEnd == null) {
            map.put("planSDateEnd", "2999-01-01 00:00:00");//预计上线结束时间
        } else {
            map.put("planSDateEnd", planSDateEnd);//预计上线结束时间
        }

        map.put("proType", proType);//项目类型
        map.put("proName", proName);//项目名称

        //所有项目list
        List<ProjectInfo> projectInfos = myProjectService.getMyProjectInfoList(map);

        List<ProjectInfo> projectInfosNew = new ArrayList<>();
        //项目相关人员集合

        //SystemUser user = (SystemUser) SecurityUtils.getSubject().getPrincipal();
        SystemUser user = new SystemUser();
        //String userName = user.getUserName();//当前登录用户
        //测试用***************************************
        String userName = "小";
        user.setUserName(userName);
        user.setDuty("CEO");
        //String department = user.getDepartment();
        //department = department.substring(0,2);
        String department = "技术";
        //当前用户为组长/经理时，可以查看自己和其小组成员相关的项目
        Department did = myProjectService.getDepartmentIdByMent(department);
        String departmentid = did.getDepartmentid();

        //根据部门id查找小组id
        List<Map<String, Object>> mapList = myProjectService.getSquadId(String.valueOf(departmentid));

        String mentIds = StringUtil.toString(MapUtil.collectProperty(mapList, "squadId"));
        String[] mIds = mentIds.split(",");
        Map<String, Object> mapTid = new HashMap<>();

        mapTid.put("mentIds", mIds);
        //组长/经理其小组成员
        List<Map<String, Object>> mapList1 = myProjectService.getMembers(mapTid);


        //当前登录用户所涉及子任务
        List<Map<String, Object>> subtaskList = myProjectService.getSubTaskIdByHander(userName);

        Map<String, Object> objectMapNew = new HashMap<>();

        String menuLeafIds = StringUtil.toString(MapUtil.collectProperty(subtaskList, "taskId"));

        String[] Ids = menuLeafIds.split(",");

        Map<String, Object> mapT = new HashMap<>();

        mapT.put("menuLeafIds", Ids);

        //根据taskId查找proId
        List<Map<String, Object>> taskList = myProjectService.getproIdByTaskId(mapT);

        List<Map<String, Object>> taskString = new ArrayList<>();
        List<Map<String, Object>> taskProId = new ArrayList<>();
        //判断项目集合中是否有对应小组成员
        //小组集合中是否匹配子任务负责人
        for (Map map1 : mapList1) {
            for (Map map0 : subtaskList) {
                if (map0.get("subtaskHandler") == map1.get("member")) {
                    taskString.add(map0);
                }
            }
        }

        //判断是否是项目相关的人（我的项目）是则重新赋值组成新 我的项目list
        for (ProjectInfo pro : projectInfos) {
            for (Map map1 : taskList) {
                if (map1.get("proId") == (Integer.valueOf(pro.getProid()))) {
                    projectInfosNew.add(pro);
                }
                //通过子任务里的taskId匹配对应任务里的taskId
                for (Map s : taskString) {
                    if (s.get("taskId") == map1.get("taskId")) {
                        taskProId.add(map1);
                    }
                }
                //通过匹配的taskId匹配对应的proId
                for (Map map3 : taskProId) {
                    if (map3.get("proId") == pro.getProid()) {
                        projectInfosNew.add(pro);
                    }
                }
            }
            //当前用户是创建人
            if (pro.getCreater() == user.getUserName()) {
                projectInfosNew.add(pro);
            }

        }

        RdPage rdPage = new RdPage();

        int sum = 0;

        if (user.getDuty() == "CEO") {
            sum = projectInfos.size();
        } else {
            sum = projectInfosNew.size();
        }

        //分页信息
        rdPage.setTotal(sum);
        rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
        rdPage.setCurrent(current);
        rdPage.setPageSize(pageSize);

        if (user.getDuty() == "CEO") {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, projectInfos, rdPage);
        } else {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, projectInfosNew, rdPage);
        }

        return result;

    }

    /**
     * 我的项目开发中详情页
     * 基本信息+项目信息+参与组
     *
     * @param id
     * @param proId
     * @return
     */
    @ApiOperation(value = "我的项目开发中详情页（基本信息+项目信息+参与组）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "我的项目主键id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getMyProjectDetails", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getMyProjectDetails(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //基本信息+项目信息Basic Information
        ProjectInfo projectInfo = upProjectService.selectByPrimaryKey(id);


        //参与组
        List<Map<String,Object>> taskList = upProjectService.getProjectTaskListMap1(proId);

        int sum = 0;
        for (Map<String,Object> projectTask : taskList) {
            projectTask.get("workDate");
            if (projectTask.get("workDate") != "" && projectTask.get("workDate") != null)
                sum += Integer.valueOf((String) projectTask.get("workDate"));

            Group group = groupService.getGroupBySquadId(Integer.valueOf((String)projectTask.get("squadId")));
            String squad=group.getSquad();
            //projectTask.setSquadId(group.getSquad());//根据id取对应小组中文名
            projectTask.put("squad",squad);//根据id取对应小组中文名
            String squadId=(String)projectTask.get("squadId");
            String departmentId=upProjectService.selectDepartmentIdBySquadId(Integer.parseInt(squadId));
            projectTask.put("departmentId",departmentId);//根据squadid取对应部门Id

        }
        projectInfo.setWorkTatalDay(String.valueOf(sum));//项目预计工期（任务工期之和）

        map.put("projectInfo", projectInfo); //基本信息+项目信息

        map.put("taskList", taskList);//参与组

        list.add(map);

        result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);

        return result;
    }

    /**
     * 我的项目参与组
     * 添加：添加，可添加新的组，将项目内的任务分配给新的人
     * 修改：修改，可修改当前任务的组别，需注意修改组别后该任务原进度全部清零，任务下的子任务全部清空
     * 删除：删除，可删除当前任务，删除后该任务内容全部清零
     *
     * @param id
     * @param proId
     * @return
     */
    @ApiOperation(value = "我的项目参与组（添加+修改+删除）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "我的项目主键id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "操作类型1添加 2修改 3删除", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "taskName", value = "任务名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sDate", value = "任务开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "eDate", value = "任务结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "handler", value = "操作人", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "squadId", value = "参与组id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workDate", value = "工作时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "idd", value = "编号", required = false, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "修改任务id", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/groupHandle", method = RequestMethod.POST)
    public ApiResult<Integer> groupHandle(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "type") int type,
            @RequestParam(value = "taskName", required = false) String taskName,
            @RequestParam(value = "sDate", required = false) String sDate,
            @RequestParam(value = "eDate", required = false) String eDate,
            @RequestParam(value = "handler", required = false) String handler,
            @RequestParam(value = "squadId", required = false) String squadId,
            @RequestParam(value = "workDate", required = false) String workDate,
            @RequestParam(value = "idd", required = false) int idd,
            @RequestParam(value = "taskId", required = false) int taskId) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<Integer> result = null;

        int i = 0;
        //添加
        if (type == 1) {
            ProjectTask projectTask = new ProjectTask();
            projectTask.setProid(proId);//项目Id
            projectTask.setSquadId(squadId);//参与组id
            projectTask.setTaskname(taskName);//任务名称
            projectTask.setSdate(sDate);//任务开始时间
            projectTask.setEdate(eDate);//任务结束时间
            projectTask.setWorkDate(workDate);//任务工时
            projectTask.setHandler(handler);//操作人
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            projectTask.setCreateDate(str);//创建时间
            projectTask.setTaskId(taskId);//项目编号
            projectTask.setTaskprogress("0");//任务进度
            projectTask.setTaskstate("1");//任务状态  值待定*************************************************

            i = myProjectService.insertProTask(projectTask);

            ProLogRecord proLogRecord = new ProLogRecord();

            java.util.Date date2 = new java.util.Date();
            String str2 = sdf.format(date2);

            proLogRecord.setType("7");//类型:分配
            proLogRecord.setDate(str2);//创建时间
            proLogRecord.setEmp(handler);//操作人
            proLogRecord.setExplain("添加任务");//说明
            proLogRecord.setProid(proId);//项目id

            //插入日志
            int ilog = applyService.insertProLogRecord(proLogRecord);

            if (i > 0 && ilog > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }

        //修改
        } else if (type == 2) {
            ProjectTask projectTask = new ProjectTask();
            projectTask.setTaskId(taskId);
            projectTask.setProid(proId);//项目Id
            projectTask.setSquadId(squadId);//参与组id
            projectTask.setTaskname(taskName);//任务名称
            projectTask.setSdate(sDate);//任务开始时间
            projectTask.setEdate(eDate);//任务结束时间
            projectTask.setWorkDate(workDate);//任务工时
            projectTask.setHandler(handler);//操作人

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            projectTask.setCreateDate(str);//创建时间
           // projectTask.setIdd(idd);//项目编号
            projectTask.setTaskprogress("0");//任务进度
            projectTask.setTaskstate("0");//任务状态  值待定*************************************************

            i = myProjectService.updateTaskById(projectTask);

            //任务下的子任务全部清空
            int k = myProjectService.deleteSubTaskById(taskId);

            ProLogRecord proLogRecord = new ProLogRecord();

            java.util.Date date2 = new java.util.Date();
            String str2 = sdf.format(date2);

            proLogRecord.setType("8");//类型:修改
            proLogRecord.setDate(str2);//创建时间
            proLogRecord.setEmp(handler);//操作人
            proLogRecord.setExplain("修改任务");//说明
            proLogRecord.setProid(proId);//项目id

            //插入日志
            int ilog = applyService.insertProLogRecord(proLogRecord);

            if (i > 0 && ilog > 0 ) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }

        //删除 可删除当前任务，删除后该任务内容全部清零
        } else {

            i = myProjectService.deleteTaskById(taskId);

            //任务下的子任务全部清空
            int k = myProjectService.deleteSubTaskById(taskId);

            ProLogRecord proLogRecord = new ProLogRecord();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            proLogRecord.setType("9");//类型:删除
            proLogRecord.setDate(str);//创建时间
            proLogRecord.setEmp(handler);//操作人
            proLogRecord.setExplain("删除任务");//说明
            proLogRecord.setProid(proId);//项目id

            //插入日志
            int ilog = applyService.insertProLogRecord(proLogRecord);

            if (i > 0 && ilog > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }
        }

        return result;

    }

    /**
     * 我的项目--任务分配详情页
     * 基本信息+任务信息+日志记录
     *
     * @param taskId
     * @param proId
     * @return
     */
    @ApiOperation(value = "我的项目开发中详情页（基本信息+项目信息+参与组）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getTaskDetails", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getTaskDetails(
            @RequestParam(value = "taskId") int taskId,
            @RequestParam(value = "proId") int proId) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //基本信息+任务信息Basic Information
        ProjectTask projectTask = myProjectService.getProjectTaskByTaskId(taskId);

        map.put("projectTask", projectTask);

        //日志记录
        List<TaskLogRecord> logRecordList = myProjectService.getTaskLogList(taskId);

        map.put("logRecordList", logRecordList);

        list.add(map);

        result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);

        return result;
    }

    /**
     * 我的项目--任务分配详细页
     * 子任务列表
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "我的项目开发中详情页（子任务列表）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getSubTaskList", method = RequestMethod.POST)
    public ApiResult<List<ProjectSubtask>> getSubTaskList(
            @RequestParam(value = "taskId") int taskId) {

        ApiResult<List<ProjectSubtask>> result = null;

        List<ProjectSubtask> list = myProjectService.getProjectSubtaskList(taskId);

        result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);

        return result;

    }

    /**
     * 我的项目--任务分配详细页
     * 子任务列表
     * 添加、修改、删除
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "我的项目开发中详情页（子任务列表添加、修改、删除）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务主键id", required = false, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "操作类型1添加 2修改 3删除", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "idd", value = "编号", required = false, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "subtaskHandler", value = "处理人", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "subtaskName", value = "任务名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sDate", value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "eDate", value = "结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workDate", value = "预计工期", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "subtaskProgress", value = "进度", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "subtaskState", value = "子任务状态", required = false, dataType = "String")
    })
    @RequestMapping(value = "/handleSubTaskList", method = RequestMethod.POST)
    public ApiResult<Integer> handleSubTaskList(
            @RequestParam(value = "taskId") int taskId,
            @RequestParam(value = "subtaskId", required = false) int subtaskId,
            @RequestParam(value = "type") int type,
            @RequestParam(value = "idd", required = false) int idd,
            @RequestParam(value = "subtaskHandler", required = false) String subtaskHandler,
            @RequestParam(value = "subtaskName", required = false) String subtaskName,
            @RequestParam(value = "sDate", required = false) String sDate,
            @RequestParam(value = "eDate", required = false) String eDate,
            @RequestParam(value = "workDate", required = false) String workDate,
            @RequestParam(value = "subtaskProgress", required = false) String subtaskProgress,
            @RequestParam(value = "subtaskState", required = false) String subtaskState) {

        ApiResult<Integer> result = null;

        //1:添加
        if (type == 1) {
            ProjectSubtask projectSubtask = new ProjectSubtask();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            projectSubtask.setEdate(eDate);//结束时间
            projectSubtask.setSdate(sDate);//开始时间
            projectSubtask.setSubtaskhandler(subtaskHandler);//处理人
            projectSubtask.setTaskid(taskId);
            projectSubtask.setSubtaskname(subtaskName);//任务名称
            projectSubtask.setIdd(idd);//编号
            projectSubtask.setWorkDate(workDate);//预计工期
            projectSubtask.setSubtaskprogress(subtaskProgress);//进度
            projectSubtask.setSubtaskstate(subtaskState);//子任务状态 值待定*******************************************
            projectSubtask.setCreateDate(str);

            int i = myProjectService.insertProSubTask(projectSubtask);

            SubtaskLogRecord subtaskLogRecord = new SubtaskLogRecord();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date2 = new java.util.Date();
            String str2 = sdf.format(date2);

            subtaskLogRecord.setType("7");//类型:分配
            subtaskLogRecord.setDate(str2);//创建时间
            subtaskLogRecord.setEmp(subtaskName);//操作人
            subtaskLogRecord.setExplain("添加任务");//说明
            subtaskLogRecord.setSubtaskid(subtaskId);//项目id

            //插入子任务日志
            int ilog = myProjectService.insertSubTaskLogRecord(subtaskLogRecord);

            TaskLogRecord taskLogRecord = new TaskLogRecord();

            //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
            taskLogRecord.setType("7");
            taskLogRecord.setDate(str2);//创建时间
            taskLogRecord.setEmp(subtaskName);//操作人
            taskLogRecord.setExplain("分配子任务");//说明
            taskLogRecord.setTaskid(taskId);

            //插入任务日志
            int sublog = myProjectService.insertTaskLogRecode(taskLogRecord);

            if (i > 0 && ilog > 0 && sublog > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }

            //2：修改
        } else if (type == 2) {
            ProjectSubtask projectSubtask = new ProjectSubtask();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            projectSubtask.setEdate(eDate);//结束时间
            projectSubtask.setSdate(sDate);//开始时间
            projectSubtask.setSubtaskhandler(subtaskHandler);//处理人
            projectSubtask.setTaskid(taskId);
            projectSubtask.setSubtaskname(subtaskName);//任务名称
            projectSubtask.setIdd(idd);//编号
            projectSubtask.setWorkDate(workDate);//预计工期
            projectSubtask.setSubtaskprogress(subtaskProgress);//进度
            projectSubtask.setSubtaskstate(subtaskState);//子任务状态 值待定*******************************************
            projectSubtask.setCreateDate(str);

            int k = myProjectService.updateProSubTask(projectSubtask);

            SubtaskLogRecord subtaskLogRecord = new SubtaskLogRecord();

            java.util.Date date2 = new java.util.Date();
            String str2 = sdf.format(date2);

            //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
            subtaskLogRecord.setType("8");//类型:修改
            subtaskLogRecord.setDate(str2);//创建时间
            subtaskLogRecord.setEmp(subtaskName);//操作人
            subtaskLogRecord.setExplain("修改子任务");//说明
            subtaskLogRecord.setSubtaskid(subtaskId);//项目id

            //插入日志
            int ilog = myProjectService.insertSubTaskLogRecord(subtaskLogRecord);

            TaskLogRecord taskLogRecord = new TaskLogRecord();

            //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
            taskLogRecord.setType("8");
            taskLogRecord.setDate(str2);//创建时间
            taskLogRecord.setEmp(subtaskName);//操作人
            taskLogRecord.setExplain("修改子任务");//说明
            taskLogRecord.setTaskid(taskId);

            //插入任务日志
            int sublog = myProjectService.insertTaskLogRecode(taskLogRecord);

            if (k > 0 && ilog > 0 && sublog > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }

        //删除
        } else {

            int j = myProjectService.deleteProSubTaskById(subtaskId);

            SubtaskLogRecord subtaskLogRecord = new SubtaskLogRecord();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
            subtaskLogRecord.setType("9");//类型:修改
            subtaskLogRecord.setDate(str);//创建时间
            subtaskLogRecord.setEmp(subtaskName);//操作人
            subtaskLogRecord.setExplain("删除子任务");//说明
            subtaskLogRecord.setSubtaskid(subtaskId);//项目id

            //插入日志
            int ilog = myProjectService.insertSubTaskLogRecord(subtaskLogRecord);

            TaskLogRecord taskLogRecord = new TaskLogRecord();

            //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
            taskLogRecord.setType("9");
            taskLogRecord.setDate(str);//创建时间
            taskLogRecord.setEmp(subtaskName);//操作人
            taskLogRecord.setExplain("删除子任务");//说明
            taskLogRecord.setTaskid(taskId);

            //插入任务日志
            int sublog = myProjectService.insertTaskLogRecode(taskLogRecord);

            if (j > 0 && ilog > 0 && sublog > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }

        }

        return result;
    }

    /**
     * 我的项目--子任务详情页
     * 基本信息+任务信息+日志记录
     *
     * @param subtaskId
     * @return
     */
    @ApiOperation(value = "我的项目子任务详情页（基本信息+项目信息+参与组）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getSubTaskDetails", method = RequestMethod.POST)
    public ApiResult<List<Map<String,Object>>> getSubTaskDetails(
            @RequestParam(value = "subtaskId") int subtaskId ){

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //基本信息+任务信息
        ProjectSubtask projectSubtask = myProjectService.getProjectSubtaskById(subtaskId);

        map.put("projectSubtask",projectSubtask);

        //日志记录
        List<SubtaskLogRecord> logList = myProjectService.getSubtaskLogList(subtaskId);

        map.put("subtaskLog",logList);

        list.add(map);

        result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE,Constant.OPERATION_SUCCESS,list,null);

        return result;
    }

    /**
     * 添加开发日志
     *
     * @param id
     * @param proId
     * @return
     */
    @ApiOperation(value = "添加开发日志")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "我的项目主键id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型1：开始:2：需求调整:3：会议 4：更新 5：预验收", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "progress", value = "进度", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "explain", value = "备注说明", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "filePath", value = "附件地址", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userName", value = "操作人", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "addType", value = "添加类型 1：项目开发日志 2：任务开发日志 3：子任务开发日志", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/addProDeveLog", method = RequestMethod.POST)
    public ApiResult<Integer> addProDeveLog(@RequestParam(value = "id") int id,
                                            @RequestParam(value = "proId") int proId,
                                            @RequestParam(value = "type") String type,
                                            @RequestParam(value = "progress", required = false) String progress,
                                            @RequestParam(value = "explain") String explain,
                                            @RequestParam(value = "filePath", required = false) String filePath,
                                            @RequestParam(value = "userName") String userName,
                                            @RequestParam(value = "taskId") int taskId,
                                            @RequestParam(value = "subtaskId") int subtaskId,
                                            @RequestParam(value = "addType") int addType) {

        ApiResult<Integer> result = null;
        //1：项目开发日志
        if (addType == 1) {
            ProDevelopLog proDevelopLog = new ProDevelopLog();

            SubtaskLogRecord subtaskLogRecord = new SubtaskLogRecord();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            proDevelopLog.setDate(str);//操作时间
            proDevelopLog.setEmp(userName);//操作人
            proDevelopLog.setExplain(explain);//备注说明
            proDevelopLog.setProid(proId);//项目id
            proDevelopLog.setType(type);//类型 1：开始:2：需求调整:3：会议 4：更新 5：预验收
            proDevelopLog.setProgress(progress);//进度
            proDevelopLog.setFilepath(filePath);//附件地址

            int i = myProjectService.insertProDeveLog(proDevelopLog);

            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setId(id);
            projectInfo.setProid(proId);
            projectInfo.setProprogress(progress);//进度
            projectInfo.setCreatedate(str);

            //有更新进度则同步项目更新进度
            int k = myProjectService.updateProjectInfo(projectInfo);

            if (i > 0 && k > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }

        //2：任务开发日志
        } else if (addType == 2) {
            TaskDevelopLog taskDevelopLog = new TaskDevelopLog();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            taskDevelopLog.setDate(str);
            taskDevelopLog.setEmp(userName);//操作人
            taskDevelopLog.setExplain(explain);//备注说明
            taskDevelopLog.setFilepath(filePath);//附件地址
            taskDevelopLog.setTaskid(taskId);//任务id
            taskDevelopLog.setProgress(progress);//进度
            taskDevelopLog.setType(type);//类型 1：开始:2：需求调整:3：会议 4：更新 5：预验收

            int i = myProjectService.insertTaskDevlog(taskDevelopLog);

            ProjectTask projectTask = new ProjectTask();
            projectTask.setTaskId(taskId);//任务id
            projectTask.setTaskprogress(progress);//进度
            projectTask.setCreateDate(str);

            //有更新进度则同步更新参与组开发进度
            int k = myProjectService.updateTaskById(projectTask);

            if (i > 0 && k > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }

        //2：子任务开发日志
        } else {
            SubtaskDevelopLog subtaskDevelopLog = new SubtaskDevelopLog();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            subtaskDevelopLog.setDate(str);
            subtaskDevelopLog.setEmp(userName);
            subtaskDevelopLog.setExplain(explain);
            subtaskDevelopLog.setFilepath(filePath);
            subtaskDevelopLog.setSubtaskid(subtaskId);
            subtaskDevelopLog.setProgress(progress);
            subtaskDevelopLog.setType(type);

            int i = myProjectService.insertSubTaskDevlog(subtaskDevelopLog);

            ProjectSubtask projectSubtask = new ProjectSubtask();
            projectSubtask.setSubtaskId(subtaskId);
            projectSubtask.setSubtaskprogress(progress);//进度
            projectSubtask.setCreateDate(str);

            //
            int k = myProjectService.updateProSubTask(projectSubtask);

            if (i > 0 && k > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }
        }

        return result;

    }

    /**
     * 我的项目详情页-提交任务
     *
     * @param taskId
     * @param subtaskId
     * @param explain
     * @param userName
     * @param type
     * @return
     */
    @ApiOperation(value = "我的项目详情页-提交任务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = false, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务id", required = false, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "explain", value = "说明", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userName", value = "操作人", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型1：任务 2：子任务", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/commitTask", method = RequestMethod.POST)
    public ApiResult<Integer> commitTask(
            @RequestParam(value = "taskId",required = false) int taskId,
            @RequestParam(value = "subtaskId",required = false) int subtaskId,
            @RequestParam(value = "explain",required = true) String explain,
            @RequestParam(value = "userName",required = true) String userName,
            @RequestParam(value = "type") int type ){

        ApiResult<Integer> result = null;

        //提交任务
        if (type == 1) {
            ProjectTask projectTask = new ProjectTask();
            projectTask.setTaskprogress("100");
            projectTask.setTaskId(taskId);
            //任务状态 *******************************************值待定
            projectTask.setTaskstate("预验收");

            int i = myProjectService.updateTaskById(projectTask);

            TaskDevelopLog taskDevelopLog = new TaskDevelopLog();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            taskDevelopLog.setDate(str);
            taskDevelopLog.setEmp(userName);//操作人
            taskDevelopLog.setExplain(explain);//备注说明
            taskDevelopLog.setTaskid(taskId);//任务id
            taskDevelopLog.setProgress("100");//进度
            taskDevelopLog.setType("5");//类型 1：开始:2：需求调整:3：会议 4：更新 5：预验收

            //添加开发日志
            int k = myProjectService.insertTaskDevlog(taskDevelopLog);

            if (i > 0 && k > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }

        //提交子任务
        } else {
            ProjectSubtask projectSubtask = new ProjectSubtask();

            projectSubtask.setSubtaskId(subtaskId);
            projectSubtask.setSubtaskprogress("100");
            //任务状态 *******************************************值待定
            projectSubtask.setSubtaskstate("预验收");

            int i = myProjectService.updateProSubTask(projectSubtask);

            SubtaskDevelopLog subtaskDevelopLog = new SubtaskDevelopLog();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            String str = sdf.format(date);

            subtaskDevelopLog.setDate(str);
            subtaskDevelopLog.setEmp(userName);
            subtaskDevelopLog.setExplain(explain);
            subtaskDevelopLog.setSubtaskid(subtaskId);
            subtaskDevelopLog.setProgress("100");
            subtaskDevelopLog.setType("5");//类型 1：开始:2：需求调整:3：会议 4：更新 5：预验收

            //添加开发日志
            int k = myProjectService.insertSubTaskDevlog(subtaskDevelopLog);

            if (i > 0 && k > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }
        }

        return result;

    }

    /**
     * 我的项目开发中详情页
     * 日志记录+开发日志
     *
     * @param id
     * @param proId
     * @return
     */
    @ApiOperation(value = "我的项目开发中详情页（日志记录+开发日志）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "我的项目主键id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "状态1：日志记录 2：开发日志", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型1：项目 2：任务 3：子任务", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "secondLevelId", value = "二级类型（id）", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getMyProjectDetailsLog", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getgetMyProjectDetailsLog(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "state") int state,
            @RequestParam(value = "type") int type,
            @RequestParam(value = "secondLevelId", required = false) String secondLevelId) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //日志记录
        if (state == 1) {
            //日志记录
            List<ProLogRecord> logRecordList = upProjectService.getProLogRecordList(proId);
            map.put("logRecordList", logRecordList);
            //开发日志
        } else {
            //开发日志
            if (type == 1) {
                //1：项目
                List<ProDevelopLog> logList = myProjectService.getProDevelopLogList(proId);
                map.put("DevLogList", logList);
            } else if (type == 2) {
                //2：任务
                List<TaskDevelopLog> logList = myProjectService.getTaskDeveLogList(Integer.valueOf(secondLevelId));
                map.put("DevLogList", logList);
            } else if (type == 3) {
                //3：子任务
                List<SubtaskDevelopLog> logList = myProjectService.getSubTaskDevLogList(Integer.valueOf(secondLevelId));
                map.put("DevLogList", logList);
            }
        }

        list.add(map);

        result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);

        return result;

    }

    /**
     * 根据类型查询二级类型名称
     *
     * @param id
     * @param proId
     * @return
     */
    @ApiOperation(value = "根据类型查询二级类型名称")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "我的项目主键id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型 2：任务 3：子任务", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getSecondLeverType", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getSecondLeverType(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "type") int type) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        if (type == 2) {
            //任务
            List<Map<String, Object>> taskList = upProjectService.getProjectTaskListMap(proId);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, taskList, null);

        } else if (type == 3) {
            //任务
            List<Map<String, Object>> taskList = upProjectService.getProjectTaskListMap(proId);

            Map<String, Object> objectMapNew = new HashMap<>();

            String menuLeafIds = StringUtil.toString(MapUtil.collectProperty(taskList, "taskId"));

            String[] Ids = menuLeafIds.split(",");

            Map<String, Object> mapT = new HashMap<>();

            mapT.put("menuLeafIds", Ids);
            //子任务
            List<Map<String, Object>> subtaskList = myProjectService.getSubTaskListMap(mapT);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, subtaskList, null);
        }

        return result;
    }


}
