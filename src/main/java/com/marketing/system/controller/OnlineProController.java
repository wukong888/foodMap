package com.marketing.system.controller;

import com.marketing.system.entity.*;
import com.marketing.system.service.OnlineProService;
import com.marketing.system.util.ApiResult;
import com.marketing.system.util.Constant;
import com.marketing.system.util.RdPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            Map<String,Object> OnProMap=OnProService.selectOnPro(current,pageSize,creatersquadid,creater,createdate1,createdate2,finishdate1,finishdate2,protype,param);

            List<ProjectInfo> OnPro=(List<ProjectInfo>)OnProMap.get("OnPro");
            Integer sum=(Integer)OnProMap.get("OnProNum");

            //分页信息
            RdPage rdPage = new RdPage();
            rdPage.setTotal(sum);
            rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
            rdPage.setCurrent(current);
            rdPage.setPageSize(pageSize);

            String msg = "";
            if (current > rdPage.getPages()) {
                msg = "已经超过当前所有页数！";
                result = new ApiResult<List<ProjectInfo>>(Constant.FAIL_CODE_VALUE, msg, null, rdPage);
            } else {
                msg = "查询成功！";
                result = new ApiResult<List<ProjectInfo>>(Constant.SUCCEED_CODE_VALUE,msg,OnPro,rdPage);
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
          List<ProjectTask> ProTask=OnProService.selectOnTask(proId);
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
            @ApiImplicitParam(paramType = "query", name = "explain", value = "项目审批通过说明", required = false, dataType = "String")
    })
    @RequestMapping(value = "/selectInsertProPassLog", method = RequestMethod.POST)
    public ApiResult<String> selectInsertProPassLog(
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "explain",required = false) String explain){
        ApiResult<String> result =null;
        if(explain==null){
            explain="";
        }
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
            @ApiImplicitParam(paramType = "query", name = "explain", value = "项目审批驳回说明", required = false, dataType = "String")
    })
    @RequestMapping(value = "/selectInsertProReturnLog", method = RequestMethod.POST)
    public ApiResult<String> selectInsertProReturnLog(
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "explain",required = false) String explain){
        ApiResult<String> result =null;
        if(explain==null){
            explain="";
        }
        boolean success=OnProService.insertProReturnLog(proId,explain);
        if(success==true){
            result=new ApiResult<String>(Constant.SUCCEED_CODE_VALUE,"审批驳回","审批驳回",null);
        }else{
            result=new ApiResult<String>(Constant.SUCCEED_CODE_VALUE,"审批驳回操作失败","审批驳回操作失败",null);
        }

        return result;
    }

    }

