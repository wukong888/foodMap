package com.marketing.system.controller;


import com.marketing.system.entity.*;
import com.marketing.system.service.FinishProService;
import com.marketing.system.service.MyProjectService;
import com.marketing.system.service.SystemUserService;
import com.marketing.system.util.*;
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
import javax.annotation.Resource;
import java.util.*;

@Api(description = "归档接口", value = "归档接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class FinishProController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private FinishProService FinProService;

    @Autowired
    private MyProjectService myProjectService;

    @Autowired
    private SystemUserService systemUserService;

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
            @ApiImplicitParam(paramType = "query", name = "id", value = "登录用户id", required = true, dataType = "Integer"),
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
            @RequestParam(value = "id", required = true) int id,
            @RequestParam(value="createdate1", required = false) String createdate1,
            @RequestParam(value="createdate2", required = false) String createdate2,
            @RequestParam(value="finishdate1", required = false) String finishdate1,
            @RequestParam(value="finishdate2", required = false) String finishdate2,
            @RequestParam(value="onlinedate1", required = false) String onlinedate1,
            @RequestParam(value="onlinedate2", required = false) String onlinedate2,
            @RequestParam(value="protype", required = false) String protype,
            @RequestParam(value="param", required = false) String param) {

        ApiResult<List<ProjectInfo>> result =null;
        try {
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
        if(onlinedate1==null||onlinedate1==""){
            onlinedate1="2010-01-01";
        }
        if(onlinedate2==null||onlinedate2==""){
            onlinedate2="2040-01-01";
        }
        if(protype==null){
            protype="";
        }
        if(param==null){
            param="";
        }

        //所有项目集合
        Map<String,Object> FinProMapAll=FinProService.selectFinishPro(current,1000,creatersquadid,creater,createdate1,createdate2,finishdate1,finishdate2,onlinedate1,onlinedate2,protype,param);

        List<ProjectInfo> FinProAll=(List<ProjectInfo>)FinProMapAll.get("FinPro");
            List<ProjectInfo> projectInfosNew = new ArrayList<>();
        //项目相关人员集合
        List<ProjectInfo> FinPro = new ArrayList<>();
            List<ProjectInfo> FinPro2 = new ArrayList<>();

        SystemUser user2 = (SystemUser) SecurityUtils.getSubject().getPrincipal();
        SystemUser user = systemUserService.selectByPrimaryKey(id);
        String userName = user.getUserName();//当前登录用户
            String department = user.getDepartment();
            department = department.substring(0, 2);

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
            String menuLeafIdsmember = StringUtil.toString(MapUtil.collectProperty(mapList1, "member"));

            String[] Idsmember = menuLeafIdsmember.split(",");

            Map<String, Object> mapTmem = new HashMap<>();

            mapTmem.put("menuLeafIds", Idsmember);

        List<ProjectInfo> subtaskListProject = new ArrayList<>();
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
        for (ProjectInfo pro : FinProAll) {
            for (Map map1 : taskList) {
                if (map1.get("proId") == (Integer.valueOf(pro.getProid()))) {
                    FinPro.add(pro);
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
                        FinPro.add(pro);
                    }
                }
            }
            //当前用户是创建人
            if (pro.getCreater().equals( user.getUserName())) {
                FinPro.add(pro);
            }

        }
            if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && !user.getDuty().equals("CEO")) {
                //当前登录用户并其成员包含所涉及子任务
               subtaskListProject = myProjectService.getProjectByHanderMapFinish(mapTmem);

                FinPro.addAll(subtaskListProject);
                Iterator it = FinPro.iterator();
                while (it.hasNext()) {
                    ProjectInfo obj = (ProjectInfo) it.next();
                    if (!FinPro2.contains(obj)) {                //不包含就添加
                        FinPro2.add(obj);
                    }
                }
                FinPro = FinPro2;
            }

        RdPage rdPage = new RdPage();
        int sum = 0;
        if (user.getDuty() .equals("CEO") ) {
            sum = FinProAll.size();
            FinProAll=ToolUtil.listSplit2(current,pageSize,FinProAll);
        } else {
            sum = FinPro.size();
            FinPro=ToolUtil.listSplit2(current,pageSize,FinPro);
        }

        //分页信息
        rdPage.setTotal(sum);
        rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
        rdPage.setCurrent(current);
        rdPage.setPageSize(pageSize);

        if (user.getDuty().equals("CEO") ) {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, FinProAll, rdPage);
        } else {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, FinPro, rdPage);
        }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询归档列表 错误信息：" + e.getMessage());
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

        try {
        List<Map> FinProInfos=new ArrayList<Map>();
        Map<String,Object> FinProInfo=new HashMap<String,Object>();
        ProjectInfo ProInfo=FinProService.selectFinProInfo(id,proId);
        List<ProLogRecord> ProLogRecord=FinProService.selectFinProLogRecord(proId);
        List<Map> ProTask=FinProService.selectFinTask(proId);
        List<ProDevelopLog> ProDevRecord=FinProService.selectFinProDevRecord(proId);

        FinProInfo.put("ProInfo",ProInfo);
        FinProInfo.put("ProLogRecord",ProLogRecord);
        FinProInfo.put("ProTask",ProTask);
        FinProInfo.put("ProDevRecord",ProDevRecord);
        FinProInfos.add(FinProInfo);
        String msg = "查询成功！";

        result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,FinProInfos,null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查看项目的详细信息 错误信息：" + e.getMessage());
        }
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
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查看任务的详细信息 错误信息：" + e.getMessage());
        }
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
        try {
        List<SubtaskDevelopLog> SubDevRecords=FinProService.selectFinSubTaskDevRecord(subtaskId);
        String msg = "查询成功！";

        result = new ApiResult<List<SubtaskDevelopLog>>(Constant.SUCCEED_CODE_VALUE,msg,SubDevRecords,null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查看子任务的开发日志 错误信息：" + e.getMessage());
        }
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

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查看子任务详细信息 错误信息：" + e.getMessage());
        }
        return result;
    }
}
