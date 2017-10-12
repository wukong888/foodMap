package com.marketing.system.controller;

import com.marketing.system.entity.*;
import com.marketing.system.service.MyProjectService;
import com.marketing.system.service.OnlineProService;
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
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "上线待审批接口", value = "上线待审批接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class OnlineProController {

        private Logger logger = LoggerFactory.getLogger(LoginController.class);

        @Resource
        private OnlineProService OnProService;

        @Resource
        private MyProjectService myProjectService;



        /**
         * 查询上线审批列表
         *
         * @return
         */
        @ApiOperation(value = "查询上线待审批列表")
        @ApiImplicitParams({
                @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
                @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer"),
                @ApiImplicitParam(paramType = "query", name = "creatersquadid", value = "项目发起部门名称", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "creater", value = "项目发起人", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "createdate1", value = "项目发起时间-开始", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "createdate2", value = "项目发起时间-结束", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "finishdate1", value = "项目完成时间-开始", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "finishdate2", value = "项目完成时间-结束", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "protype", value = "项目类型", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "param", value = "关键字", required = false, dataType = "String"),
        })
        @RequestMapping(value = "/selectOnPro", method = RequestMethod.POST)
        public ApiResult<List<ProjectInfo>> selectOnPro(
                @RequestParam(value = "current") int current,
                @RequestParam(value = "pageSize") int pageSize,
                @RequestParam(value="creatersquadid", required = false) String creatersquadid,
                @RequestParam(value="creater", required = false) String creater,
                @RequestParam(value="createdate1", required = false) String createdate1,
                @RequestParam(value="createdate2", required = false) String createdate2,
                @RequestParam(value="finishdate1", required = false) String finishdate1,
                @RequestParam(value="finishdate2", required = false) String finishdate2,
                @RequestParam(value="protype", required = false) String protype,
                @RequestParam(value="param", required = false) String param) {

            ApiResult<List<ProjectInfo>> result =null;
            if(creatersquadid==null){
                creatersquadid="";
            }
            if(creater==null){
                creater="";
            }
            if(createdate1==null||createdate1==""){
                createdate1="2010-01-01";
            }
            if(createdate2==null||createdate2==""){
                createdate2="2040-01-01";
            }
            if(finishdate1==null||finishdate1==""){
                finishdate1="2010-01-01";
            }
            if(finishdate2==null||finishdate2==""){
                finishdate2="2040-01-01";
            }
            if(protype==null){
                protype="";
            }
            if(param==null){
                param="";
            }
            //所有项目集合
            Map<String,Object> OnProMapAll=OnProService.selectOnPro(current,pageSize,creatersquadid,creater,createdate1,createdate2,finishdate1,finishdate2,protype,param);

            //项目相关人员集合
            List<ProjectInfo> OnPro = new ArrayList<>();

            List<ProjectInfo> OnProAll=(List<ProjectInfo>)OnProMapAll.get("OnPro");

            //SystemUser user = (SystemUser) SecurityUtils.getSubject().getPrincipal();
            SystemUser user = new SystemUser();
            //String userName = user.getUserName();//当前登录用户
            //测试用***************************************
            String userName = "陈东和";
            user.setUserName(userName);
            user.setDuty("CEO");
            //String department = user.getDepartment();
            //department = department.substring(0,2);
            String department = "总经";

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
            for (ProjectInfo pro : OnProAll) {
                for (Map map1 : taskList) {
                    if (map1.get("proId") == (Integer.valueOf(pro.getProid()))) {
                        OnPro.add(pro);
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
                            OnPro.add(pro);
                        }
                    }
                }
                //当前用户是创建人
                if (pro.getCreater() == user.getUserName()) {
                    OnPro.add(pro);
                }
            }

            RdPage rdPage = new RdPage();
            int sum = 0;
            if (user.getDuty() == "CEO") {
                sum = OnProAll.size();
            } else {
                sum = OnPro.size();
            }


            //分页信息
            rdPage.setTotal(sum);
            rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
            rdPage.setCurrent(current);
            rdPage.setPageSize(pageSize);

            if (user.getDuty() == "CEO") {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, OnProAll, rdPage);
            } else {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, OnPro, rdPage);
            }

            return  result;

        }

    /**
     * 项目详细信息
     *
     * @return
     */
    @ApiOperation(value = "查看项目的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目的记录id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目编号", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/selectOnProInfo", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectOnProInfo(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId){
          ApiResult<List<Map>> result =null;

          List<Map> OnProInfos=new ArrayList<Map>();
          Map<String,Object> OnProInfo=new HashMap<String,Object>();
          ProjectInfo ProInfo=OnProService.selectOnProInfo(id,proId);
          List<ProLogRecord> ProLogRecord=OnProService.selectOnProLogRecord(proId);
          List<Map> ProTask=OnProService.selectOnTask(proId);
          List<ProDevelopLog> ProDevRecord=OnProService.selectOnProDevRecord(proId);

          OnProInfo.put("ProInfo",ProInfo);
          OnProInfo.put("ProLogRecord",ProLogRecord);
          OnProInfo.put("ProTask",ProTask);
          OnProInfo.put("ProDevRecord",ProDevRecord);
          OnProInfos.add(OnProInfo);
          String msg = "查询成功！";

          result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,OnProInfos,null);
          return result;
    }

    /**
     * 任务详细信息
     *
     * @return
     */
    @ApiOperation(value = "查看任务的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务Id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/selectOnTaskInfo", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectOnTaskInfo(
            @RequestParam(value = "taskId") int taskId){
        ApiResult<List<Map>> result =null;

        List<Map> OnTaskInfos=new ArrayList<Map>();
        Map<String,Object> OnTaskInfo=new HashMap<String,Object>();
        ProjectTask TaskInfo=OnProService.selectOnTaskInfo(taskId);
        List<TaskLogRecord> TaskLogRecord=OnProService.selectOnTaskLogRecord(taskId);
        List<ProjectSubtask> Subtask=OnProService.selectOnSubtask(taskId);
        List<TaskDevelopLog> TaskDevRecord=OnProService.selectOnTaskDevRecord(taskId);

        OnTaskInfo.put("TaskInfo",TaskInfo);
        OnTaskInfo.put("TaskLogRecord",TaskLogRecord);
        OnTaskInfo.put("Subtask",Subtask);
        OnTaskInfo.put("TaskDevRecord",TaskDevRecord);
        OnTaskInfos.add(OnTaskInfo);
        String msg = "查询成功！";

        result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,OnTaskInfos,null);
        return result;
    }

    /**
     * 查看子任务的开发日志
     *
     * @return
     */
    @ApiOperation(value = "查看子任务的开发日志")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务Id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/selectOnSubtaskDevRecord", method = RequestMethod.POST)
    public ApiResult<List<SubtaskDevelopLog>> selectOnSubtaskDevRecord(
            @RequestParam(value = "subtaskId") int subtaskId){
        ApiResult<List<SubtaskDevelopLog>> result =null;

        List<SubtaskDevelopLog> SubDevRecords=OnProService.selectOnSubTaskDevRecord(subtaskId);
        String msg = "查询成功！";

        result = new ApiResult<List<SubtaskDevelopLog>>(Constant.SUCCEED_CODE_VALUE,msg,SubDevRecords,null);
        return result;
    }

    /**
     * 查看子任务详细信息
     *
     * @return
     */
    @ApiOperation(value = "查看子任务详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务Id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/selectOnSubtaskInfo", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectOnSubtaskInfo(
            @RequestParam(value = "subtaskId") int subtaskId){
        ApiResult<List<Map>> result =null;
        List<Map> OnSubtaskInfos=new ArrayList<Map>();
        Map<String,Object> OnSubtaskInfo=new HashMap<String,Object>();
        ProjectSubtask SubTaskInfo=OnProService.selectOnSubtaskInfo(subtaskId);
        List<SubtaskLogRecord> SubLogRecords=OnProService.selectOnSubtaskLogRecord(subtaskId);
        List<SubtaskDevelopLog> SubDevRecords=OnProService.selectOnSubTaskDevRecord(subtaskId);
        OnSubtaskInfo.put("SubTaskInfo",SubTaskInfo);
        OnSubtaskInfo.put("SubLogRecords",SubLogRecords);
        OnSubtaskInfo.put("SubDevRecords",SubDevRecords);
        OnSubtaskInfos.add(OnSubtaskInfo);
        String msg = "查询成功！";

        result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,OnSubtaskInfos,null);
        return result;
    }


    /**
     * 审批通过，添加项目日志记录
     *
     * @return
     */
    @ApiOperation(value = "审批通过")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "explain", value = "项目审批通过说明", required = true, dataType = "String")
    })
    @RequestMapping(value = "/selectInsertProPassLog", method = RequestMethod.POST)
    public ApiResult<String> selectInsertProPassLog(
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "explain",required = false) String explain){
        ApiResult<String> result =null;

        boolean success=OnProService.insertProPassLog(proId,explain);
        if(success==true){
            result=new ApiResult<String>(Constant.SUCCEED_CODE_VALUE,"审批通过","审批通过",null);
        }else{
            result=new ApiResult<String>(Constant.SUCCEED_CODE_VALUE,"审批通过操作失败","审批通过操作失败",null);
        }

        return result;
    }

    /**
     * 审批驳回，添加项目日志记录
     *
     * @return
     */
    @ApiOperation(value = "审批驳回")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "explain", value = "项目审批驳回说明", required = true, dataType = "String")
    })
    @RequestMapping(value = "/selectInsertProReturnLog", method = RequestMethod.POST)
    public ApiResult<String> selectInsertProReturnLog(
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "explain",required = false) String explain){
        ApiResult<String> result =null;

        boolean success=OnProService.insertProReturnLog(proId,explain);
        if(success==true){
            result=new ApiResult<String>(Constant.SUCCEED_CODE_VALUE,"审批驳回","审批驳回",null);
        }else{
            result=new ApiResult<String>(Constant.SUCCEED_CODE_VALUE,"审批驳回操作失败","审批驳回操作失败",null);
        }

        return result;
    }

    /**
     * 指定回复
     *
     * @return
     */
    @ApiOperation(value = "指定回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "Uid", value = "员工Id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "rescontent", value = "回复内容", required = true, dataType = "String")
    })
    @RequestMapping(value = "/weixinPush", method = RequestMethod.POST)
    public ApiResult<String> weixinPush(
            @RequestParam(value = "Uid") int Uid,
            @RequestParam(value = "rescontent") String rescontent){
       /* String postUrl="{\"Uid\":"+Uid
                +",\"Content\":\"今日发起工单数:"+list.get(0)+"\\n\",\"AgentId\":1000003,\"Title\":\"工单时报\",\"Url\":\"http://report.wsloan.com:8888/workorder/user/loginAuto.do?id="
                +user.getId()+"&password="+user.getPassword().trim().toUpperCase()+"\"}";
*/
       // WeiXinPushUtil.httpPostWithJSON(postUrl);
        ApiResult<String> result =null;
        return result;
    }

    }

