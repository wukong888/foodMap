package com.marketing.system.controller;


import com.marketing.system.entity.*;
import com.marketing.system.service.FinishProService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "归档接口", value = "归档接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class FinishProController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private FinishProService FinProService;

    /**
     * 查询归档列表
     *
     * @return
     */
    @ApiOperation(value = "查询归档列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "creatersquadid", value = "项目发起部门名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "creater", value = "项目发起人", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createdate1", value = "项目发起时间-开始", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createdate2", value = "项目发起时间-结束", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "finishdate1", value = "项目完成时间-开始", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "finishdate2", value = "项目完成时间-结束", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "onlinedate1", value = "项目上线时间-开始", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "onlinedate2", value = "项目上线时间-结束", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "protype", value = "项目类型", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "param", value = "关键字", required = false, dataType = "String"),
    })
    @RequestMapping(value = "/selectFinPro", method = RequestMethod.POST)
    public ApiResult<List<ProjectInfo>> selectFinPro(
            @RequestParam(value = "current") int current,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value="creatersquadid", required = false) String creatersquadid,
            @RequestParam(value="creater", required = false) String creater,
            @RequestParam(value="createdate1", required = false) String createdate1,
            @RequestParam(value="createdate2", required = false) String createdate2,
            @RequestParam(value="finishdate1", required = false) String finishdate1,
            @RequestParam(value="finishdate2", required = false) String finishdate2,
            @RequestParam(value="onlinedate1", required = false) String onlinedate1,
            @RequestParam(value="onlinedate2", required = false) String onlinedate2,
            @RequestParam(value="protype", required = false) String protype,
            @RequestParam(value="param", required = false) String param) {

        ApiResult<List<ProjectInfo>> result =null;
        Map<String,Object> FinProMap=FinProService.selectFinishPro(current,pageSize,creatersquadid,creater,createdate1,createdate2,finishdate1,finishdate2,onlinedate1,onlinedate2,protype,param);

        List<ProjectInfo> FinPro=(List<ProjectInfo>)FinProMap.get("FinPro");
        Integer sum=(Integer)FinProMap.get("FinProNum");

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
            result = new ApiResult<List<ProjectInfo>>(Constant.SUCCEED_CODE_VALUE,msg,FinPro,rdPage);
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
    @RequestMapping(value = "/selectFinProInfo", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectFinProInfo(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId){
        ApiResult<List<Map>> result =null;

        List<Map> FinProInfos=new ArrayList<Map>();
        Map<String,Object> FinProInfo=new HashMap<String,Object>();
        ProjectInfo ProInfo=FinProService.selectFinProInfo(id,proId);
        List<ProLogRecord> ProLogRecord=FinProService.selectFinProLogRecord(proId);
        List<ProjectTask> ProTask=FinProService.selectFinTask(proId);
        List<ProDevelopLog> ProDevRecord=FinProService.selectFinProDevRecord(proId);

        FinProInfo.put("ProInfo",ProInfo);
        FinProInfo.put("ProLogRecord",ProLogRecord);
        FinProInfo.put("ProTask",ProTask);
        FinProInfo.put("ProDevRecord",ProDevRecord);
        FinProInfos.add(FinProInfo);
        String msg = "查询成功！";

        result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,FinProInfos,null);
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
    @RequestMapping(value = "/selecFinTaskInfo", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectFinTaskInfo(
            @RequestParam(value = "taskId") int taskId){
        ApiResult<List<Map>> result =null;

        List<Map> FinTaskInfos=new ArrayList<Map>();
        Map<String,Object> FinTaskInfo=new HashMap<String,Object>();
        ProjectTask TaskInfo=FinProService.selectFinTaskInfo(taskId);
        List<TaskLogRecord> TaskLogRecord=FinProService.selectFinTaskLogRecord(taskId);
        List<ProjectSubtask> Subtask=FinProService.selectFinSubtask(taskId);
        List<TaskDevelopLog> TaskDevRecord=FinProService.selectFinTaskDevRecord(taskId);

        FinTaskInfo.put("TaskInfo",TaskInfo);
        FinTaskInfo.put("TaskLogRecord",TaskLogRecord);
        FinTaskInfo.put("Subtask",Subtask);
        FinTaskInfo.put("TaskDevRecord",TaskDevRecord);
        FinTaskInfos.add(FinTaskInfo);
        String msg = "查询成功！";

        result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,FinTaskInfos,null);
        return result;
    }

    /**
     * 查看子任务详细信息
     *
     * @return
     */
    @ApiOperation(value = "查看子任务的开发日志")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务Id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/selectFinSubtaskDevRecord", method = RequestMethod.POST)
    public ApiResult<List<SubtaskDevelopLog>> selectFinSubtaskDevRecord(
            @RequestParam(value = "subtaskId") int subtaskId){
        ApiResult<List<SubtaskDevelopLog>> result =null;

        List<SubtaskDevelopLog> SubDevRecords=FinProService.selectFinSubTaskDevRecord(subtaskId);
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
    @RequestMapping(value = "/selectFinSubtaskInfo", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectFinSubtaskInfo(
            @RequestParam(value = "subtaskId") int subtaskId){
        ApiResult<List<Map>> result =null;
        List<Map> FinSubtaskInfos=new ArrayList<Map>();
        Map<String,Object> FinSubtaskInfo=new HashMap<String,Object>();
        ProjectSubtask SubTaskInfo=FinProService.selectFinSubtaskInfo(subtaskId);
        List<SubtaskLogRecord> SubLogRecords=FinProService.selectFinSubtaskLogRecord(subtaskId);
        List<SubtaskDevelopLog> SubDevRecords=FinProService.selectFinSubTaskDevRecord(subtaskId);
        FinSubtaskInfo.put("SubTaskInfo",SubTaskInfo);
        FinSubtaskInfo.put("SubLogRecords",SubLogRecords);
        FinSubtaskInfo.put("SubDevRecords",SubDevRecords);
        FinSubtaskInfos.add(FinSubtaskInfo);
        String msg = "查询成功！";

        result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,FinSubtaskInfos,null);
        return result;
    }
}
