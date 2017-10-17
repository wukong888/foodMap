package com.marketing.system.controller;

import com.marketing.system.entity.*;
import com.marketing.system.service.MyProjectService;
import com.marketing.system.service.OnlineProService;
import com.marketing.system.service.RecycleProService;
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
import javax.tools.Tool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "上线待审批接口", value = "上线待审批接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class RecycleProController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private RecycleProService RecProService;

    @Resource
    private MyProjectService myProjectService;

    @Autowired
    private SystemUserService systemUserService;



    /**
     * 查询回收列表
     *
     * @return
     */
    @ApiOperation(value = "查询回收列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "creatersquadid", value = "项目发起部门", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "creater", value = "项目发起人", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "登录用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "createdate1", value = "项目发起时间-开始", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createdate2", value = "项目发起时间-结束", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "plansdate1", value = "项目预计完成时间-开始", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "plansdate2", value = "项目预计完成时间-结束", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "protype", value = "项目类型", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "prostate", value = "项目状态", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "param", value = "关键字", required = false, dataType = "String"),
    })
    @RequestMapping(value = "/selectRecPro", method = RequestMethod.POST)
    public ApiResult<List<ProjectInfo>> selectRecPro(
            @RequestParam(value = "current") int current,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value="creatersquadid", required = false) String creatersquadid,
            @RequestParam(value="creater", required = false) String creater,
            @RequestParam(value = "id", required = true) int id,
            @RequestParam(value="createdate1", required = false) String createdate1,
            @RequestParam(value="createdate2", required = false) String createdate2,
            @RequestParam(value="plansdate1", required = false) String plansdate1,
            @RequestParam(value="plansdate2", required = false) String plansdate2,
            @RequestParam(value="protype", required = false) String protype,
            @RequestParam(value="prostate", required = false) String prostate,
            @RequestParam(value="param", required = false) String param) {

        ApiResult<List<ProjectInfo>> result =null;
        try {
        Map<String,Object> RecProMapAll=null;

        if(creatersquadid==null){
            creatersquadid="";
        }
        if(creater==null){
            creater="";
        }
        if(createdate1==null||createdate1==""){
            createdate1="2010-01-01 00:00:00";
        }else{
            createdate1=createdate1+" 00:00:00";
        }
        if(createdate2==null||createdate2==""){
            createdate2="2040-01-01 23:59:59";
        }else{
            createdate1=createdate1+" 23:59:59";
        }
        if(plansdate1==null||plansdate1==""){
            plansdate1="2010-01-01 00:00:00";
        }else{
            plansdate1=plansdate1+" 00:00:00";
        }
        if(plansdate2==null||plansdate2==""){
            plansdate2="2040-01-01 23:59:59";
        }else{
            plansdate2=plansdate2+" 23:59:59";
        }
        if(protype==null){
            protype="";
        }
        if(param==null){
            param="";
        }
        if(prostate==null||prostate==""){
            //所有项目集合
            RecProMapAll=RecProService.selectRecPro(current,1000,creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param);
        }else if(prostate.equals("5")){
            //驳回项目集合
            RecProMapAll=RecProService.selectRecProState5(current,pageSize,creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param);
        }else if(prostate.equals("6")){
            //作废项目集合
            RecProMapAll=RecProService.selectRecProState6(current,pageSize,creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param);
        }

        List<ProjectInfo> RecProAll=(List<ProjectInfo>)RecProMapAll.get("RecPro");

        //项目相关人员集合
        List<ProjectInfo> RecPro = new ArrayList<>();

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
        for (ProjectInfo pro : RecProAll) {
            for (Map map1 : taskList) {
                if (map1.get("proId") == (Integer.valueOf(pro.getProid()))) {
                    RecPro.add(pro);
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
                        RecPro.add(pro);
                    }
                }
            }
            //当前用户是创建人
            if (pro.getCreater().equals(user.getUserName( )) ) {
                RecPro.add(pro);
            }
        }

        RdPage rdPage = new RdPage();
        int sum = 0;
        if (user.getDuty() .equals("CEO") ) {
            sum = RecProAll.size();
            RecProAll=ToolUtil.listSplit2(current,pageSize,RecProAll);
        } else {
            sum = RecPro.size();
            RecPro=ToolUtil.listSplit2(current,pageSize,RecPro);
        }


        //分页信息
        rdPage.setTotal(sum);
        rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
        rdPage.setCurrent(current);
        rdPage.setPageSize(pageSize);

        if (user.getDuty().equals("CEO") ) {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, RecProAll, rdPage);
        } else {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, RecPro, rdPage);
        }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询回收列表 错误信息：" + e.getMessage());
        }
        return  result;

    }

    /**
     * 项目详细信息
     *
     * @return
     */
    @ApiOperation(value = "查看项目的基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目的记录id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目编号", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/selectRecProInfo", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectRecProInfo(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId){
        ApiResult<List<Map>> result =null;
        try {
        List<Map> RecProInfos=new ArrayList<Map>();
        Map<String,Object> RecProInfo=new HashMap<String,Object>();
        ProjectInfo ProInfo=RecProService.selectRecProInfo(id,proId);
        List<ProLogRecord> ProLogRecord=RecProService.selectRecProLogRecord(proId);
        List<ProjectTask> ProTask=RecProService.selectRecTask(proId);

        RecProInfo.put("ProInfo",ProInfo);
        RecProInfo.put("ProLogRecord",ProLogRecord);
        RecProInfo.put("ProTask",ProTask);
        RecProInfos.add(RecProInfo);
        String msg = "查询成功！";

        result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,RecProInfos,null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查看项目的基本信息 错误信息：" + e.getMessage());
        }
        return result;
    }

}
