package com.marketing.system.controller;

import com.marketing.system.entity.*;
import com.marketing.system.service.GroupService;
import com.marketing.system.service.MembersService;
import com.marketing.system.service.UpProjectService;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private UpProjectService upProjectService;

    @Autowired
    private MembersService membersService;

    @Autowired
    private GroupService groupService;

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

        Map<String, Object> map = new HashMap<>();
        map.put("current", current);
        map.put("pageSize", pageSize);
        map.put("createrSquadId", createrSquadId);//项目发起部门
        map.put("creater", creater);//创建人
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
        map.put("proState", "1");//项目状态(1:立项待审批

        List<ProjectInfo> projectInfos = upProjectService.getProjectInfoList(map);

        //增加排序序号
        /*for (int i = 0; i < projectInfos.size(); i++) {
            projectInfos.get(i).setIndex((current - 1) * pageSize + i + 1);
        }*/

        RdPage rdPage = new RdPage();

        Map<String, Object> mapT = new HashMap<>();

        mapT.put("proState", "1");//项目状态(1:立项待审批
        int sum = upProjectService.sumAll(mapT);
        //分页信息
        rdPage.setTotal(sum);
        rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
        rdPage.setCurrent(current);
        rdPage.setPageSize(pageSize);

        String msg = "";
        if (current > rdPage.getPages()) {
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
            @ApiImplicitParam(paramType = "query", name = "proState", value = "操作日志类型(1:立项待审批，2：开发中，3：上线带审批，4：完成，5：驳回，6：作废)", required = true, dataType = "String")
    })
    @RequestMapping(value = "/passOrReject", method = RequestMethod.POST)
    public ApiResult<String> passOrReject(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "creatName") String creatName,
            @RequestParam(value = "explain") String explain,
            @RequestParam(value = "proState") String proState) {

        ApiResult<String> result = null;

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("proState", proState);

        int i = upProjectService.setPassOrReject(map);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date = new java.util.Date();
        String str = sdf.format(date);

        ProLogRecord proLogRecord = new ProLogRecord();
        proLogRecord.setDate(str);//时间
        proLogRecord.setExplain(explain);//说明
        proLogRecord.setEmp(creatName);//操作人
        proLogRecord.setType(proState);//类型
        proLogRecord.setProid(proId);//项目id

        //插入日志
        int ilog = upProjectService.insertProLogRecord(proLogRecord);

        if (i > 0 && ilog > 0) {
            result = new ApiResult<String>(Constant.SUCCEED_CODE_VALUE, "操作成功！", null, null);
        } else {
            result = new ApiResult<String>(Constant.FAIL_CODE_VALUE, "操作失败,请检查是否是立项待审批项目！", null, null);
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

        try{
            //基本信息+项目信息Basic Information
            ProjectInfo projectInfo = upProjectService.selectByPrimaryKey(id);

            //日志记录
            List<ProLogRecord> logRecordList = upProjectService.getProLogRecordList(proId);
            for (ProLogRecord proLogRecord:logRecordList) {
                if (proLogRecord.getType() == "创建"){
                    proLogRecord.setFilepath(projectInfo.getProfilepath());//申请创建项目附件路径
                }
            }
            //参与组
            List<ProjectTask> taskList = upProjectService.getProjectTaskList(proId);

            int sum = 0;
            for (ProjectTask projectTask:taskList) {
                projectTask.getWorkDate();
                if (projectTask.getWorkDate() !="" && projectTask.getWorkDate() != null)
                sum +=Integer.valueOf(projectTask.getWorkDate());

                Group group = groupService.getGroupBySquadId(Integer.valueOf(projectTask.getSquadId()));

                projectTask.setSquadId(group.getSquad());//根据id取对应小组中文名

            }
            projectInfo.setWorkTatalDay(String.valueOf(sum));//项目预计工期（任务工期之和）

            map.put("projectInfo",projectInfo); //基本信息+项目信息
            map.put("logRecordList",logRecordList);//日志记录
            map.put("taskList",taskList);//参与组

            list.add(map);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE,Constant.OPERATION_SUCCESS,list,null);

        }catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 根据部门id查找项目发起人
     * @param squadId
     * @return
     */
    @ApiOperation(value = "根据部门id查找项目发起人")
    @ApiImplicitParam(paramType = "query", name = "squadId", value = "小组id", required = true, dataType = "Integer")
    @RequestMapping(value = "/getMembersBySquadId", method = RequestMethod.POST)
    public ApiResult<List<Members>> getMembers(@RequestParam(value = "squadId") int squadId) {

        ApiResult<List<Members>> result = null;

        try {
            List<Members> list = membersService.getMembersById(squadId);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE,Constant.OPERATION_SUCCESS,list,null);
        }catch (Exception e){

            e.printStackTrace();
        }
        return result;

    }

}
