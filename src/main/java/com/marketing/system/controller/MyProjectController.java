package com.marketing.system.controller;


import com.marketing.system.entity.*;
import com.marketing.system.mapper.DepartmentNewMapper;
import com.marketing.system.mapper.SystemUserMapper;
import com.marketing.system.mapper_two.*;
import com.marketing.system.service.*;
import com.marketing.system.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.marketing.system.util.WeiXinPushUtil.httpPostWithJSON;

@Api(description = "我的项目接口", value = "我的项目接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class MyProjectController {

    private Logger logger = LoggerFactory.getLogger(MyProjectController.class);

    @Autowired
    private MyProjectService myProjectService;

    @Autowired
    private UpProjectService upProjectService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private ProjectSubtaskMapper projectSubtaskMapper;

    @Autowired
    private DepartmentNewMapper departmentNewMapper;

    @Autowired
    private OnlineProMapper OnProMapper;

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Autowired
    private DayReportMapper DayReportDao;

    @Autowired
    private ProjectInfoMapper proInfoMapper;

    @Value("${prosupervisor.id}")
    private String prosupervisorid;

    @Value("${ceo.id}")
    private String ceoid;

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
            @ApiImplicitParam(paramType = "query", name = "id", value = "登录用户id", required = true, dataType = "Integer"),
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
            @RequestParam(value = "id", required = true) int id,
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

        try {
            map = ToolUtil.putMap(current, createrSquadId, creater, proState, createDateStart, createDateEnd, planSDateStart, planSDateEnd, proType, proName);

            //所有项目list
            List<ProjectInfo> projectInfos = myProjectService.getMyProjectInfoList(map);

            List<ProjectInfo> projectInfotaskNew = new ArrayList<>();

            //4：完成，5：驳回，6：作废不在我的项目里显示
            projectInfos = projectInfos.stream().filter(lin -> lin.getProstate().equals("1") || lin.getProstate().equals("2") || lin.getProstate().equals("3") || lin.getProstate().equals("7")).collect(Collectors.toList());

            ProjectInfo projectInfoNew = new ProjectInfo();

            for (ProjectInfo projectInfo : projectInfos) {

                long betweenDays = ToolUtil.getBetweenTimes(projectInfo.getPlansdate());

                //如果是完成状态则不更新
                if (projectInfo.getProstate().equals("3") || projectInfo.getProstate().equals("4")) {

                } else {
                    //则更新状态为逾期
                    if (betweenDays < 0) {
                        projectInfoNew.setProstate("7");//逾期
                        projectInfoNew.setId(projectInfo.getId());
                        int i = myProjectService.updateProjectInfo(projectInfoNew);
                    }
                }
            }

            List<ProjectInfo> projectInfosNew = new ArrayList<>();
            List<ProjectInfo> projectInfosNew2 = new ArrayList<>();
            List<ProjectInfo> projectInfosNew3 = new ArrayList<>();
            //项目相关人员集合

            SystemUser user = systemUserService.selectByPrimaryKey(id);

            String userName = user.getUserName();//当前登录用户

            //当前登录人为任务负责人则加入我的项目
            List<Map<String, Object>> projectTaskInfos = myProjectService.getTaskInfoList(userName);

            String mentIdtask = StringUtil.toString(MapUtil.collectProperty(projectTaskInfos, "proId"));
            String[] mIdtask = mentIdtask.split(",");
            Map<String, Object> mapTask = new HashMap<>();

            mapTask.put("menuLeafIds", mIdtask);

            //查询当前登录人是任务负责人的项目
            List<ProjectInfo> infoList = myProjectService.getMyProjectInfoListByProId(mapTask);

            for (ProjectInfo p : infoList) {
                projectInfotaskNew.add(p);
            }

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

            String menuLeafIdsmember2 = StringUtil.toString(MapUtil.collectProperty(listMembers, "UserName"));

            String[] Idsmember = menuLeafIdsmember2.split(",");


/*******************************************对应组成员 结束******************************************************************/


            Map<String, Object> mapTmem = new HashMap<>();

            mapTmem = ToolUtil.putMaplimit(Idsmember, createrSquadId, creater, proState, createDateStart, createDateEnd, planSDateStart, planSDateEnd, proType, proName);

            List<Map<String, Object>> subtaskList = new ArrayList<>();
            List<ProjectInfo> subtaskListProject = new ArrayList<>();

            if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
                if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && !user.getDuty().equals("CEO")) {
                    //当前登录用户并其成员包含所涉及子任务
                    subtaskList = myProjectService.getSubTaskIdByHanderMap(mapTmem);

                } else {
                    //当前登录用户所涉及子任务
                    subtaskList = myProjectService.getSubTaskIdByHander(userName);
                }
            } else {
                //当前登录用户所涉及子任务
                subtaskList = myProjectService.getSubTaskIdByHander(userName);
            }

            String menuLeafIds = StringUtil.toString(MapUtil.collectProperty(subtaskList, "taskId"));

            String[] Ids = menuLeafIds.split(",");

            Map<String, Object> mapT = new HashMap<>();

            mapT.put("menuLeafIds", Ids);

            //根据taskId查找proId
            List<Map<String, Object>> taskList = myProjectService.getproIdByTaskId(mapT);

            if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
                if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && !user.getDuty().equals("CEO")) {
                    Map<String, Object> objectMap = new HashMap<>();

                    Map<String, Object> stringObjectMap = new HashMap<>();

                    stringObjectMap.put("UserGroupId",user.getUserGroupId());

                    /**
                     * 经理判断  是总监的话单独一种情况
                     */
                    List<Map<String, Object>> listGm = departmentNewMapper.getGmMember();

                    String GmMember = StringUtil.toString(MapUtil.collectProperty(listGm, "id"));
                    GmMember.contains(String.valueOf(user.getUserGroupId()));

                    if (GmMember.contains(String.valueOf(user.getUserGroupId()))) {

                        List<Map<String, Object>> list = departmentNewMapper.getZjMember(user.getUserGroupId());

                        Map<String, Object> mapMemZj = new HashMap<>();

                        mapMemZj = ToolUtil.getmapList(list, "id");

                        List<Map<String, Object>> listMembers_zj = systemUserService.getMembersByUserGroupId(mapMemZj);

                        String Idsmembers = StringUtil.toString(MapUtil.collectProperty(listMembers_zj, "UserName"));

                        String[] groupMember = Idsmembers.split(",");
                        objectMap.put("Idsmember", groupMember);//自己部门小组成员

                    } else {
                        if (user.getDuty().contains("经理")) {

                            List<Map<String, Object>> list = departmentNewMapper.getJlMember(user.getUserGroupId());

                            Map<String, Object> mapMemZj = new HashMap<>();

                            mapMemZj = ToolUtil.getmapList(list, "id");

                            List<Map<String, Object>> listMembers_zj = systemUserService.getMembersByUserGroupId(mapMemZj);

                            String Idsmembers = StringUtil.toString(MapUtil.collectProperty(listMembers_zj, "UserName"));

                            String[] groupMember = Idsmembers.split(",");
                            objectMap.put("Idsmember", groupMember);//自己部门小组成员

                        } else {
                            //组长
                            List<Map<String, Object>> list = systemUserService.getGroupMembers(stringObjectMap);

                            String Idsmembers = StringUtil.toString(MapUtil.collectProperty(list, "UserName"));

                            String[] groupMember = Idsmembers.split(",");
                            objectMap.put("Idsmember", groupMember);//自己部门小组成员
                        }
                    }

                    objectMap.put("Ids", Ids);//taskId集合
                    objectMap.put("handler", userName);
                    objectMap.put("createrSquadId", createrSquadId);//项目发起部门
                    objectMap.put("proState", proState);//项目状态(1:立项待审批，2：开发中，3：上线带审批，4：完成，5：驳回，6：作废）
                    if (createDateStart == "" || createDateStart == null) {
                        objectMap.put("createDateStart", "1980-01-01 00:00:00");//项目发起开始时间
                    } else {
                        objectMap.put("createDateStart", createDateStart);//项目发起开始时间
                    }

                    if (createDateEnd == "" || createDateEnd == null) {
                        objectMap.put("createDateEnd", "2999-01-01 00:00:00");//项目发起结束时间
                    } else {
                        objectMap.put("createDateEnd", createDateEnd);//项目发起结束时间
                    }

                    if (planSDateStart == "" || planSDateStart == null) {
                        objectMap.put("planSDateStart", "1980-01-01 00:00:00");//预计上线开始时间
                    } else {
                        objectMap.put("planSDateStart", planSDateStart);//预计上线开始时间
                    }

                    if (planSDateEnd == "" || planSDateEnd == null) {
                        objectMap.put("planSDateEnd", "2999-01-01 00:00:00");//预计上线结束时间
                    } else {
                        objectMap.put("planSDateEnd", planSDateEnd);//预计上线结束时间
                    }

                    objectMap.put("proType", proType);//项目类型
                    objectMap.put("proName", proName);//项目名称
                    projectInfosNew3 = myProjectService.getProjectInfoByZuzhang(objectMap);


                }
            }

            List<Map<String, Object>> taskString = new ArrayList<>();
            List<Map<String, Object>> taskProId = new ArrayList<>();
            //判断项目集合中是否有对应小组成员
            //小组集合中是否匹配子任务负责人
            for (Map map1 : listMembers) {
                for (Map map0 : subtaskList) {
                    if (map0.get("subtaskHandler") == map1.get("UserName")) {
                        taskString.add(map0);
                    }
                }
            }
            //判断是否是项目相关的人（我的项目）是则重新赋值组成新 我的项目list
            for (ProjectInfo pro : projectInfos) {
                //根据项目id查询子任务负责人
                List<Map<String, Object>> projectSubtaskList = applyService.selectProSubtaskByProId(pro.getProid());
                //判断当前登录用户在对应项目中是项目发起人还是项目负责人（组员）

                //有子任务的情况
                if (projectSubtaskList.size() > 0) {
                    for (Map map1 : projectSubtaskList) {
                        //如果当前登录用户为该项目发起人并且是或者不是该项目子任务负责人都是项目发起人
                        pro = ToolUtil.setDuty(pro, userName, user, map1);
                    }
                } else {
                    pro = ToolUtil.setDuty(pro, userName, user, new HashMap());
                }

                /*if (projectInfosNew3.size() > 0) {
                    for (ProjectInfo map1 : projectInfosNew3) {
                        map1.getCreater();
                        if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
                            //如果当前登录用户为该项目发起人并且是或者不是该项目子任务负责人都是项目发起人
                            if (userName.equals(map1.getCreater()) && !user.getDuty().equals("CEO")) {
                                map1.setDuty("项目发起人");
                                //
                            } else if (!userName.equals(pro.getCreater()) && userName.equals(map1.getCreater())) {
                                map1.setDuty("组员");
                            } else if (user.getDuty().equals("CEO")) {
                                map1.setDuty("CEO");
                            } else if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && !map1.getCreater().equals("陈冬和")) {
                                map1.setDuty("经理/组长");
                            } else if (map1.getCreater().equals("陈冬和")) {
                                map1.setDuty("组员");
                            } else {
                                map1.setDuty("项目无关人员");
                            }
                        } else{
                            map1.setDuty("项目无关人员");
                        }
                    }
                }*/
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

                if (projectInfotaskNew.size() > 0) {
                    for (ProjectInfo map1 : projectInfotaskNew) {
                        map1.getCreater();
                        //如果当前登录用户为该项目发起人并且是或者不是该项目子任务负责人都是项目发起人
                        map1 = ToolUtil.setDutyProInfo(pro, userName, user, map1);
                    }
                }
                //当前用户是创建人
                if (pro.getCreater().equals(user.getUserName())) {
                    projectInfosNew.add(pro);
                }
                //当前登录用户并其成员包含所涉及子任务
                subtaskListProject = myProjectService.getProjectByHanderMap(mapTmem);
                if (subtaskListProject.size() > 0) {
                    for (ProjectInfo map1 : subtaskListProject) {
                        map1.getCreater();
                        //如果当前登录用户为该项目发起人并且是或者不是该项目子任务负责人都是项目发起人
                        map1 = ToolUtil.setDutyProInfo(pro, userName, user, map1);
                    }
                }
            }
            if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
                if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && !user.getDuty().equals("CEO")) {
                    if (!creater.equals("")) {
                        projectInfosNew3 = projectInfosNew3.stream().filter(lin -> lin.getCreater().equals(creater)).collect(Collectors.toList());
                    }

                    projectInfosNew = projectInfosNew3;
                    //Collections.sort(projectInfosNew);

                }
            }
            RdPage rdPage = new RdPage();

            int sum = 0;
            if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
                if (user.getDuty().equals("CEO")) {
                    sum = projectInfos.size();
                    projectInfos = ToolUtil.listSplit2(current, pageSize, projectInfos);
                } else {

                    Iterator it = projectInfosNew.iterator();
                    while (it.hasNext()) {
                        ProjectInfo obj = (ProjectInfo) it.next();
                        if (!projectInfosNew2.contains(obj)) {                //不包含就添加
                            projectInfosNew2.add(obj);
                        }
                    }
                    projectInfosNew = projectInfosNew2;
                    sum = projectInfosNew.size();
                    projectInfosNew = ToolUtil.listSplit2(current, pageSize, projectInfosNew);
                }
            } else {
                Iterator it = projectInfosNew.iterator();
                while (it.hasNext()) {
                    ProjectInfo obj = (ProjectInfo) it.next();
                    if (!projectInfosNew2.contains(obj)) {                //不包含就添加
                        projectInfosNew2.add(obj);
                    }
                }
                projectInfosNew = projectInfosNew2;
                sum = projectInfosNew.size();
                projectInfosNew = ToolUtil.listSplit2(current, pageSize, projectInfosNew);
            }

            //分页信息
            rdPage.setTotal(sum);
            rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
            rdPage.setCurrent(current);
            rdPage.setPageSize(pageSize);

            if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
                if (user.getDuty().equals("CEO")) {
                    result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, projectInfos, rdPage);
                } else {
                    result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, projectInfosNew, rdPage);
                }
            } else {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, projectInfosNew, rdPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("我的项目列表错误信息：" + e);
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
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getMyProjectDetails", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getMyProjectDetails(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "userId") int userId) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {
            //基本信息+项目信息Basic Information
            ProjectInfo projectInfo = upProjectService.selectByPrimaryKey(id);

            //通过任务计算项目进度
            int avgTaskProgress = myProjectService.getAvgTaskProgress(proId);

            //自动更新对应项目进度
            ProjectInfo projectInfoGress = new ProjectInfo();
            projectInfoGress.setProprogress(String.valueOf(avgTaskProgress));
            projectInfoGress.setId(id);

            int proProgress = myProjectService.updateProjectInfo(projectInfoGress);


            SystemUser user = systemUserService.selectByPrimaryKey(userId);

           /* String department = user.getDepartment();
            department = department.substring(0, 2);
            //当前用户为组长/经理时，可以查看自己和其小组成员相关的项目
            Department did = myProjectService.getDepartmentIdByMent(department);
            String departmentid = did.getDepartmentid();

            //根据部门id查找小组id
            List<Map<String, Object>> mapList = myProjectService.getSquadId(String.valueOf(departmentid));

            Map<String, Object> mapTid = new HashMap<>();

            mapTid = ToolUtil.getmapList(mapList, "squadId");
            //组长/经理其小组成员
            List<Map<String, Object>> mapList1 = myProjectService.getMembers(mapTid);

            String menuLeafIdsmember = StringUtil.toString(MapUtil.collectProperty(mapList1, "subtaskHandler"));*/

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
            if (projectSubtaskList.size() > 0) {
                for (Map map1 : projectSubtaskList) {
                    if (user.getUserName().equals(projectInfo.getCreater()) && !user.getDuty().equals("CEO")) {
                        projectInfo.setDuty("项目发起人");
                    } else if (!user.getUserName().equals(projectInfo.getCreater()) && user.getUserName().equals(map1.get("subtaskHandler"))) {
                        projectInfo.setDuty("组员");
                    } else if (user.getDuty().equals("CEO")) {
                        projectInfo.setDuty("CEO");
                    } else if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && menuLeafIdsmember.contains(projectInfo.getCreater())) {
                        projectInfo.setDuty("经理/组长");
                    } else if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && !menuLeafIdsmember.contains(projectInfo.getCreater())) {
                        projectInfo.setDuty("组员");
                    }
                }
            } else {
                if (user.getUserName().equals(projectInfo.getCreater()) && !user.getDuty().equals("CEO")) {
                    projectInfo.setDuty("项目发起人");
                }  else if (user.getDuty().equals("CEO")) {
                    projectInfo.setDuty("CEO");
                } else if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && menuLeafIdsmember.contains(projectInfo.getCreater())) {
                    projectInfo.setDuty("经理/组长");
                } else if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && !menuLeafIdsmember.contains(projectInfo.getCreater())) {
                    projectInfo.setDuty("组员");
                } else {
                    projectInfo.setDuty("组员");
                }
            }

            List<Map<String, Object>> taskList = new ArrayList<>();

            //参与组
            taskList = upProjectService.getProjectTaskListMap1(proId);

            ProjectTask projectTaskNew = new ProjectTask();

            Map<String, Object> map1 = new HashMap<>();
            Double sum = 0.0;
            for (Map<String, Object> projectTask : taskList) {
                if (projectTask.get("workDate") != "" && projectTask.get("workDate") != null) {
                    sum += Double.parseDouble(String.valueOf(projectTask.get("workDate")));
                    //dbnum1 = Double.parseDouble(String.valueOf(projectTask.get("workDate")));
                }

                //Group group = groupService.getGroupBySquadId(Integer.valueOf((String) projectTask.get("squadId")));
                DepartmentNew departmentNew = departmentNewMapper.getDeptnoBySquadId(Integer.valueOf((String) projectTask.get("squadId")));

                String squad = departmentNew.getDeptno();
                //projectTask.setSquadId(group.getSquad());//根据id取对应小组中文名
                projectTask.put("squad", squad);//根据id取对应小组中文名

                if (user.getDuty().contains("组长")) {
                    Map<String,Object> mapZuZhang = new HashMap<>();
                    mapZuZhang.put("proId",proId);
                    mapZuZhang.put("squadId",user.getUserGroupId());

                    if (projectTask.get("squadId").equals(String.valueOf(user.getUserGroupId()))) {
                        projectTask.put("power","true");
                    } else {
                        projectTask.put("power","false");
                    }

                }

                //判断该任务有无子任务
                int taskProgress = 0;
                List<ProjectSubtask> subtaskList = myProjectService.getProjectSubtaskList(Integer.valueOf(String.valueOf(projectTask.get("taskId"))));
                if (subtaskList.size() > 0) {
                    //根据子任务查计算询任务进度
                    taskProgress = myProjectService.getAvgSubTaskProgress(Integer.valueOf(String.valueOf(projectTask.get("taskId"))));
                }

                //如果任务为预验收/完成状态则不更新
                if (projectTask.get("taskState").equals("3") || projectTask.get("taskState").equals("4")) {

                } else{
                    //根据子任务更新任务进度
                    ProjectTask projectTaskUp = new ProjectTask();
                    projectTaskUp.setTaskprogress(String.valueOf(taskProgress));
                    projectTaskUp.setTaskId(Integer.valueOf(String.valueOf(projectTask.get("taskId"))));
                    if (taskProgress > 0 && projectTask.get("taskState").equals("1")) {
                        projectTaskUp.setTaskstate("2");
                    }

                    int subUp = myProjectService.updateTaskById(projectTaskUp);
                }

                //对应组所有人信息
                //List<Map<String, Object>> systemUserList = systemUserService.selectManagerBydepartment(map1);
                List<Map<String, Object>> systemUserList = listMembers;
                List<Map<String, Object>> systemUserListNew = new ArrayList<>();

                for (Map sys : systemUserList) {
                    if (sys.get("duty") != "" && sys.get("duty") != null) {
                        if (String.valueOf(sys.get("duty")).contains("组长") || String.valueOf(sys.get("duty")).contains("经理")) {
                            systemUserListNew.add(sys);
                        }
                    } else {
                        projectTask.put("duty", "组员");
                    }

                }
                String menuLeafIds = StringUtil.toString(MapUtil.collectProperty(systemUserListNew, "UserName"));

                String[] Ids = menuLeafIds.split(",");

                Map<String, Object> mapT = new HashMap<>();

                mapT.put("menuLeafIds", Ids);
                if (menuLeafIds.contains(user.getUserName())) {
                    projectTask.put("duty", "经理/组长");
                } else {
                    projectTask.put("duty", "组员");
                }
                if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
                    if (user.getDuty().equals("CEO")) {
                        projectTask.put("duty", "CEO");
                    }
                }
                if (projectInfo.getCreater().equals(user.getUserName())) {
                    projectTask.put("duty", "项目发起人");
                }

                //判断是否逾期，是则更新状态为逾期
                long betweenDays = ToolUtil.getBetweenTimes(String.valueOf(projectTask.get("eDate")));
                //完成
                if (String.valueOf(projectTask.get("taskState")).equals("4")) {

                } else {
                    //则更新状态为逾期
                    if (betweenDays < 0) {
                        if (!String.valueOf(projectTask.get("taskState")).equals("3")) {
                            projectTaskNew.setTaskstate("5");//逾期

                            projectTaskNew.setTaskId(Integer.valueOf(String.valueOf(projectTask.get("taskId"))));
                            int i = myProjectService.updateTaskById(projectTaskNew);
                        }

                    }
                }

            }
            projectInfo.setWorkTatalDay(String.valueOf(sum));//项目预计工期（任务工期之和）

            map.put("projectInfo", projectInfo); //基本信息+项目信息

            map.put("taskList", taskList);//参与组

            list.add(map);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("我的项目开发中详情页（基本信息+项目信息+参与组）错误信息：" + e);
        }
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
            @ApiImplicitParam(paramType = "query", name = "id", value = "我的项目主键id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "操作类型1添加 2修改 3删除", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taskName", value = "任务名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sDate", value = "任务开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "eDate", value = "任务结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "handler", value = "操作人", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "squadId", value = "参与组id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workDate", value = "工作时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "idd", value = "编号", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "修改任务id", required = false, dataType = "String")
    })
    @RequestMapping(value = "/groupHandle", method = RequestMethod.POST)
    public ApiResult<Integer> groupHandle(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "proId") String proId,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "taskName", required = false) String taskName,
            @RequestParam(value = "sDate", required = false) String sDate,
            @RequestParam(value = "eDate", required = false) String eDate,
            @RequestParam(value = "handler", required = false) String handler,
            @RequestParam(value = "squadId", required = false) String squadId,
            @RequestParam(value = "workDate", required = false) String workDate,
            @RequestParam(value = "idd", required = false) String idd,
            @RequestParam(value = "taskId", required = false) String taskId) throws ParseException{

        Integer Type = Integer.parseInt(type);
        Integer ProId = Integer.parseInt(proId);

        Map<String, Object> map = new HashMap<>();
        ApiResult<Integer> result = null;
        String reBoolean = ToolUtil.cacheExist(type);
        if (reBoolean.equals("full")) {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.AGAINCOMMIT_FAIL, null, null);
            return result;
        }
        int i = 0;
        Map<String, Object> map1 = new HashMap<>();
        try {
            //添加
            if (Type == 1) {
                ProjectTask projectTask = new ProjectTask();
                projectTask.setProid(ProId);//项目Id
                projectTask.setSquadId(squadId);//参与组id
                projectTask.setTaskname(taskName);//任务名称
                projectTask.setSdate(sDate);//任务开始时间
                projectTask.setEdate(eDate);//任务结束时间
                projectTask.setWorkDate(workDate);//任务工时

                List<Map<String,Object>> list = new ArrayList<>();
                list = systemUserService.getMembersById(squadId);

                //list = list.stream().filter(x -> x.get("UserGroupId").equals(squadId)).collect(Collectors.toList());

                List<Map<String, Object>> systemUserListNew = new ArrayList<>();

                for (Map sys : list) {
                    if (sys.get("duty") != "" && sys.get("duty") != null) {
                        if (String.valueOf(sys.get("duty")).contains("组长")) {
                            systemUserListNew.add(sys);
                        } else if (!String.valueOf(sys.get("duty")).contains("组长") && String.valueOf(sys.get("duty")).contains("经理")){
                            systemUserListNew.add(sys);
                        }
                    }
                }
                String handler2 = "";
                if (systemUserListNew.size() > 1) {
                    systemUserListNew = systemUserListNew.stream().filter(x -> x.get("duty").toString().contains("组长") && !(x.get("duty").toString()).contains("副")).collect(Collectors.toList());
                }
                for (Map handle : systemUserListNew) {
                    handler2 = String.valueOf(handle.get("UserName"));
                }
                projectTask.setHandler(handler2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                java.util.Date date = new java.util.Date();
                String str = sdf.format(date);

                projectTask.setCreateDate(str);//创建时间
                //projectTask.setTaskId(TaskId);//项目编号
                projectTask.setTaskprogress("0");//任务进度
                projectTask.setTaskstate("1");//任务状态 1:未开始  2:开发中 3:预验收  4:完成

                i = myProjectService.insertProTask(projectTask);

                logger.error("我的项目参与组添加任务成功------"+i+"任务名称："+taskName);

                //任务分配完成时，推送消息给相关负责人
                Integer Uid=systemUserMapper.getUidByName(handler2);
                //获取当前时间
                String PushDate=DateUtil.getYMDHMDate();
                //获取相应项目信息
                ProjectInfo pro = OnProMapper.selectProByProId(ProId);

                //推送给相应的任务处理人-组长
                String postUrl1 = "";
                String postUrl2 = "";
                String postUrl3 = "";
                postUrl1 = "{\"Uid\":" + Uid + ",\"Content\":\"【任务分配】\\n\\n《" +pro.getProname()+ "》需您协助实施"+taskName+"工作，请及时处理。"
                        + "\\n\\n任务分配:" + handler
                        + "\\n\\n任务名称:" + taskName
                        + "\\n\\n开始时间:" + sDate
                        + "\\n\\n结束时间:" + eDate
                        + "\\n\\n推送时间:" + PushDate
                        + "\",\"AgentId\":1000011,\"Title\":\"任务分配\",\"Url\":\"\"}";

                //推送给郑洁
                postUrl2 = "{\"Uid\":" + prosupervisorid + ",\"Content\":\"【任务分配】\\n\\n《" +pro.getProname()+ "》需您协助实施"+taskName+"工作，请及时处理。"
                        + "\\n\\n任务分配:" + handler
                        + "\\n\\n任务名称:" + taskName
                        + "\\n\\n开始时间:" + sDate
                        + "\\n\\n结束时间:" + eDate
                        + "\\n\\n推送时间:" + PushDate
                        + "\",\"AgentId\":1000011,\"Title\":\"任务分配\",\"Url\":\"\"}";

                //推送给陈总
                postUrl3 = "{\"Uid\":" + ceoid + ",\"Content\":\"【任务分配】\\n\\n《" +pro.getProname()+ "》需您协助实施"+taskName+"工作，请及时处理。"
                        + "\\n\\n任务分配:" + handler
                        + "\\n\\n任务名称:" + taskName
                        + "\\n\\n开始时间:" + sDate
                        + "\\n\\n结束时间:" + eDate
                        + "\\n\\n推送时间:" + PushDate
                        + "\",\"AgentId\":1000011,\"Title\":\"任务分配\",\"Url\":\"\"}";

                try {
                    //消息推送-任务分配
                   /*httpPostWithJSON(postUrl1);
                    httpPostWithJSON(postUrl2);
                    httpPostWithJSON(postUrl3);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //判断12小时后分配的任务有没有执行操作
                Timer timer = new Timer();
                // timer.schedule(new SynchronizingTask(Task), 5*60*1000);
                TimerTask threadTask = new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("进行延迟预警");
                        logger.error("进行延迟预警!!!");

                        Integer taskId=projectTask.getTaskId();
                        //根据任务Id查找项目信息
                        ProjectInfo pro = proInfoMapper.getProjectInfoByTaskId(taskId);
                        Integer groupId=Integer.parseInt(projectTask.getSquadId());
                        List<ProjectSubtask> subtasks=DayReportDao.getSubtaskByWXTimeOutPush(taskId);
                        String postUrl1 = "";
                        String postUrl2 = "";
                        if(subtasks.size()==0){
                            //该任务下面未分配子任务
                            String PushDate = DateUtil.getYMDHMDate();
                            Integer managerId=DayReportDao.getManagerIdByGroupId(groupId);
                            String proName=DayReportDao.getProNameByTaskId(taskId);
                            //推送给部门经理
                    /*postUrl1 = "{\"Uid\":" + managerId + ",\"Content\":\"【延迟预警1级】\\n\\n《" +pro.getProname()+ "》需"+Task.getHandler()+"协助实施"+Task.getTaskname()+"工作，现已超过12小时未处理，请督促处理。"
                            + "\\n\\n任务分配:" + Task.getHandler()
                            + "\\n\\n任务名称:" + Task.getTaskname()
                            + "\\n\\n开始时间:" + Task.getSdate()
                            + "\\n\\n结束时间:" + Task.getEdate()
                            + "\\n\\n推送时间:" + PushDate
                            + "\",\"AgentId\":1000011,\"Title\":\"延迟预警\",\"Url\":\"\"}";*/

                            //推送给郑洁
                            postUrl2 = "{\"Uid\":" + prosupervisorid + ",\"Content\":\"【延迟预警1级】\\n\\n《" +pro.getProname()+ "》需"+projectTask.getHandler()+"协助实施"+projectTask.getTaskname()+"工作，现已超过12小时未处理，请督促处理。"
                                    + "\\n\\n任务分配:" + projectTask.getHandler()
                                    + "\\n\\n任务名称:" + projectTask.getTaskname()
                                    + "\\n\\n开始时间:" + projectTask.getSdate()
                                    + "\\n\\n结束时间:" + projectTask.getEdate()
                                    + "\\n\\n推送时间:" + PushDate
                                    + "\",\"AgentId\":1000011,\"Title\":\"延迟预警\",\"Url\":\"\"}";
                            logger.error("延迟预警内容："+postUrl2);
                        }
                        try {
                            //消息推送-延迟预警
                          //  String Str = httpPostWithJSON(postUrl1);
                           // System.out.println(Str);
                            //httpPostWithJSON(postUrl2);
                            logger.error("消息推送-延迟预警成功！");
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("消息推送-延迟预警出错："+e);
                        }
                    }
                };
                timer.schedule(threadTask, 2*60*1000);

                ProLogRecord proLogRecord = new ProLogRecord();

                java.util.Date date2 = new java.util.Date();
                String str2 = sdf.format(date2);

                proLogRecord.setType("7");//类型:分配
                proLogRecord.setDate(str2);//创建时间
                proLogRecord.setEmp(handler);//操作人
                proLogRecord.setExplain("添加任务");//说明
                proLogRecord.setProid(ProId);//项目id

                //插入日志
                int ilog = applyService.insertProLogRecord(proLogRecord);

                if (i > 0 && ilog > 0) {
                    result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
                } else {
                    result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
                }

                //修改
            } else if (Type == 2) {
                //判断任务工时是否是任务开始时间和任务结束时间
                long days = ToolUtil.getBetweenDays(sDate,eDate);

                if (!String.valueOf(days).equals(workDate)) {
                    return new ApiResult<>(Constant.FAIL_CODE_VALUE,Constant.DAYS_FAIL,null,null);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<ProjectSubtask> subtaskListNew = new ArrayList<>();
                //判断修改任务的时间是否包含其子任务的开始和结束时间
                List<ProjectSubtask> subtaskList = myProjectService.getProjectSubtaskList(Integer.valueOf(taskId));

                for (ProjectSubtask projectSubtask:subtaskList) {
                    projectSubtask.setSdate(String.valueOf(sdf.parse(projectSubtask.getSdate()).getTime()));
                    projectSubtask.setEdate(String.valueOf(sdf.parse(projectSubtask.getEdate()).getTime()));
                }
                subtaskListNew = subtaskList;
                //子任务开始时间最小值
                LongSummaryStatistics stats = subtaskListNew.stream().mapToLong(x -> Long.valueOf(x.getSdate())).summaryStatistics();

                //子任务结束时间最大值
                LongSummaryStatistics statMax = subtaskListNew.stream().mapToLong(x -> Long.valueOf(x.getEdate())).summaryStatistics();

                Long mindate = stats.getMin();//子任务开始时间最小值
                Date plansdate = sdf.parse(sDate);//任务修改开始时间

                Long maxdate = statMax.getMax();//子任务开始时间最大值
                Date planedate = sdf.parse(eDate);//任务修改结束时间

                if (subtaskList.size() > 0) {
                    if ((plansdate.getTime() <= mindate) && plansdate.getTime() < maxdate) {

                    } else {
                        return new ApiResult<>(Constant.FAIL_CODE_VALUE,Constant.TASK_TIMEFAIL_START,null,null);
                    }
                    if (planedate.getTime() > mindate && planedate.getTime() >= maxdate) {

                    } else {
                        return new ApiResult<>(Constant.FAIL_CODE_VALUE,Constant.TASK_TIMEFAIL_END,null,null);
                    }
                    logger.error("修改任务时间正常：任务id"+taskId);
                }

                Integer TaskId = Integer.parseInt(taskId);
                ProjectTask projectTask = new ProjectTask();
                projectTask.setTaskId(TaskId);
                projectTask.setProid(ProId);//项目Id
                projectTask.setSquadId(squadId);//参与组id
                projectTask.setTaskname(taskName);//任务名称
                projectTask.setSdate(sDate);//任务开始时间
                projectTask.setEdate(eDate);//任务结束时间
                projectTask.setWorkDate(workDate);//任务工时
                //projectTask.setHandler(handler);//操作人

                java.util.Date date = new java.util.Date();
                String str = sdf.format(date);

                projectTask.setCreateDate(str);//创建时间
                // projectTask.setIdd(idd);//项目编号
                projectTask.setTaskprogress("0");//任务进度
                projectTask.setTaskstate("1");//任务状态 1:未开始  2:开发中 3:预验收  4:完成

                i = myProjectService.updateTaskById(projectTask);

                logger.error("我的项目参与组修改任务成功------"+i+"任务名称："+taskName);
                //任务下的子任务全部清空
                //int k = myProjectService.deleteSubTaskById(TaskId);

                //logger.error("任务下的子任务全部清空成功------"+k+"任务名称："+taskName);
                ProLogRecord proLogRecord = new ProLogRecord();

                java.util.Date date2 = new java.util.Date();
                String str2 = sdf.format(date2);

                proLogRecord.setType("8");//类型:修改
                proLogRecord.setDate(str2);//创建时间
                proLogRecord.setEmp(handler);//操作人
                proLogRecord.setExplain("修改任务");//说明
                proLogRecord.setProid(ProId);//项目id

                //插入日志
                int ilog = applyService.insertProLogRecord(proLogRecord);

                if (i > 0 && ilog > 0) {
                    result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
                } else {
                    result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
                }

                //删除 可删除当前任务，删除后该任务内容全部清零
            } else if (Type == 3){
                Integer TaskId = Integer.parseInt(taskId);
                i = myProjectService.deleteTaskById(TaskId);

                //任务下的子任务全部清空
                int k = myProjectService.deleteSubTaskById(TaskId);

                logger.error("我的项目参与组删除任务、子任务成功------"+k+"任务id："+taskId);

                ProLogRecord proLogRecord = new ProLogRecord();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                java.util.Date date = new java.util.Date();
                String str = sdf.format(date);

                proLogRecord.setType("9");//类型:删除
                proLogRecord.setDate(str);//创建时间
                proLogRecord.setEmp(handler);//操作人
                proLogRecord.setExplain("删除任务");//说明
                proLogRecord.setProid(ProId);//项目id

                //插入日志
                int ilog = applyService.insertProLogRecord(proLogRecord);

                if (i > 0 && ilog > 0) {
                    result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
                } else {
                    result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("我的项目参与组（添加+修改+删除）错误信息：" + e);
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
    @ApiOperation(value = "我的项目任务分配详情页（基本信息+项目信息+日志记录）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getTaskDetails", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getTaskDetails(
            @RequestParam(value = "taskId") int taskId,
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "userId") int userId) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {
            ProjectInfo projectInfo = myProjectService.getProjectInfoByProId(proId);

            SystemUser user = systemUserService.selectByPrimaryKey(userId);

            //参与组
            List<Map<String, Object>> taskList = upProjectService.getProjectTaskListMap1(proId);
            ProjectTask projectTaskNew = new ProjectTask();

            Map<String, Object> map1 = new HashMap<>();
            Double sum = 0.0;
            String menuLeafIds = "";
            /*String department2 = user.getDepartment();
            department2 = department2.substring(0, 2);
            //当前用户为组长/经理时，可以查看自己和其小组成员相关的项目
            Department did = myProjectService.getDepartmentIdByMent(department2);
            String departmentid = did.getDepartmentid();

            //根据部门id查找小组id
            List<Map<String, Object>> mapList = myProjectService.getSquadId(String.valueOf(departmentid));

            //小组
            String menuLeafIdsmember = StringUtil.toString(MapUtil.collectProperty(mapList, "squadId"));*/

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
            for (Map<String, Object> projectTask : taskList) {
                if (projectTask.get("workDate") != "" && projectTask.get("workDate") != null) {
                    sum += Double.parseDouble(String.valueOf(projectTask.get("workDate")));
                    //dbnum1 = Double.parseDouble(String.valueOf(projectTask.get("workDate")));
                }

                //Group group = groupService.getGroupBySquadId(Integer.valueOf((String) projectTask.get("squadId")));
                DepartmentNew departmentNew = departmentNewMapper.getDeptnoBySquadId(Integer.valueOf((String) projectTask.get("squadId")));

                String squad = departmentNew.getDeptno();
                //projectTask.setSquadId(group.getSquad());//根据id取对应小组中文名
                projectTask.put("squad", squad);//根据id取对应小组中文名
                /*String squadId = (String) projectTask.get("squadId");

                String departmentId = upProjectService.selectDepartmentIdBySquadId(Integer.parseInt(squadId));
                projectTask.put("departmentId", departmentId);//根据squadid取对应部门Id

                String department = upProjectService.selectDepartmentByDId(departmentId);

                if (department.length() > 1) {
                    department = department.substring(0, 2);
                }

                map1.put("department", department);*/

                //对应组所有人信息
                //List<Map<String, Object>> systemUserList = systemUserService.selectManagerBydepartment(map1);
                List<Map<String, Object>> systemUserList = listMembers;
                List<Map<String, Object>> systemUserListNew = new ArrayList<>();


                for (Map sys : systemUserList) {
                    if (sys.get("duty") != "" && sys.get("duty") != null) {
                        if (String.valueOf(sys.get("duty")).contains("组长") || String.valueOf(sys.get("duty")).contains("经理")) {
                            systemUserListNew.add(sys);
                        }
                    } else {
                        projectTask.put("duty", "组员");
                    }

                }
                menuLeafIds = StringUtil.toString(MapUtil.collectProperty(systemUserListNew, "UserName"));

                String[] Ids = menuLeafIds.split(",");

                Map<String, Object> mapT = new HashMap<>();

                mapT.put("menuLeafIds", Ids);
            }


            //基本信息+任务信息Basic Information
            ProjectTask projectTask = myProjectService.getProjectTaskByTaskId(taskId);
            int taskProgress = 0;
            List<ProjectSubtask> subtaskList = myProjectService.getProjectSubtaskList(projectTask.getTaskId());
            if (subtaskList.size() > 0) {
                //根据子任务查计算询任务进度
                taskProgress = myProjectService.getAvgSubTaskProgress(projectTask.getTaskId());
            }

            if (taskProgress > 0 && projectTask.getTaskstate().equals("1")) {
                ProjectTask projectTaskUp = new ProjectTask();
                projectTaskUp.setTaskstate("2");
                projectTaskUp.setTaskprogress(String.valueOf(taskProgress));
                projectTaskUp.setTaskId(projectTask.getTaskId());

                int subUp = myProjectService.updateTaskById(projectTaskUp);

                projectTask.setTaskstate("2");
            } else if (taskProgress > 0 && projectTask.getTaskstate().equals("2")) {
                ProjectTask projectTaskUp = new ProjectTask();
                projectTaskUp.setTaskstate("2");
                projectTaskUp.setTaskprogress(String.valueOf(taskProgress));
                projectTaskUp.setTaskId(projectTask.getTaskId());

                int subUp = myProjectService.updateTaskById(projectTaskUp);
            }

            if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && menuLeafIdsmember.contains(projectTask.getSquadId())) {
                projectTask.setDuty("经理/组长");
            } else {
                projectTask.setDuty("组员");
            }
            if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
                if (user.getDuty().equals("CEO")) {
                    projectTask.setDuty("CEO");
                }
            }
            if (projectInfo.getCreater().equals(user.getUserName())) {
                projectTask.setDuty("项目发起人");
            }

            //Group group = groupMapper.getGroupBySquadId(Integer.valueOf(projectTask.getSquadId()));

            DepartmentNew departmentNew = departmentNewMapper.getDeptnoBySquadId(Integer.valueOf(projectTask.getSquadId()));

            //对应组名称
            projectTask.setSquad(departmentNew.getDeptno());

            map.put("projectTask", projectTask);

            //日志记录
            List<TaskLogRecord> logRecordList = myProjectService.getTaskLogList(taskId);

            map.put("logRecordList", logRecordList);

            list.add(map);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("我的项目任务分配详情页（基本信息+项目信息+日志记录）错误信息：" + e);
        }
        return result;
    }

    /**
     * 我的项目--任务分配详细页
     * 子任务列表
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "我的项目任务分配详细页（子任务列表）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "登录用户id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getSubTaskList", method = RequestMethod.POST)
    public ApiResult<List<ProjectSubtask>> getSubTaskList(
            @RequestParam(value = "taskId") int taskId,
            @RequestParam(value = "id") int id) {

        ApiResult<List<ProjectSubtask>> result = null;

        try {
            List<ProjectSubtask> list = myProjectService.getProjectSubtaskList(taskId);

            SystemUser user = systemUserService.selectByPrimaryKey(id);

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

            ProjectSubtask projectSubtask1 = new ProjectSubtask();
            for (ProjectSubtask projectSubtask : list) {

                //子任务权限：自己、自己组长、经理和ceo有权限
                /*Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();

                map2.put("member", projectSubtask.getSubtaskhandler());
                Department department = myProjectService.getDepartmentById(map2);

                String department2 = department.getDepartment();
                if (department2.length() > 1) {
                    department2 = department2.substring(0, 2);
                }
                map1.put("department", department2);*/

                //对应组所有人信息
                //List<Map<String, Object>> systemUserList = systemUserService.selectManagerBydepartment(map1);
                List<Map<String, Object>> systemUserList = listMembers;

                List<Map<String, Object>> systemUserListNew = new ArrayList<>();

                for (Map sys : systemUserList) {
                    if (sys.get("duty") != "" && sys.get("duty") != null) {
                        if (String.valueOf(sys.get("duty")).contains("组长") || String.valueOf(sys.get("duty")).contains("经理")) {
                            systemUserListNew.add(sys);
                        }
                    } else {
                        projectSubtask.setDuty("组员");
                    }
                }

                String menuLeafIds = StringUtil.toString(MapUtil.collectProperty(systemUserListNew, "UserName"));

                if (menuLeafIds.contains(user.getUserName())) {
                    projectSubtask.setDuty("经理/组长");
                } else {
                    projectSubtask.setDuty("组员");
                }
                if (projectSubtask.getSubtaskhandler().equals(user.getUserName())) {
                    projectSubtask.setDuty("项目发起人");
                }
                if (user.getDuty().equals("CEO")) {
                    projectSubtask.setDuty("CEO");
                }
                long between = ToolUtil.getBetweenTimes(projectSubtask.getEdate());

                if (projectSubtask.getSubtaskstate().equals("4")) {

                } else {
                    if (between < 0) {
                        projectSubtask1.setSubtaskstate("5");
                        projectSubtask1.setSubtaskId(projectSubtask.getSubtaskId());
                        int i = myProjectService.updateProSubTask(projectSubtask1);
                    }
                }
            }
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("我的项目任务分配详细页（子任务列表）错误信息：" + e);
        }
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
    @ApiOperation(value = "我的项目任务分配详细页（子任务列表添加、修改、删除）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务主键id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "操作类型1添加 2修改 3删除", required = true, dataType = "Integer"),
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
            @RequestParam(value = "subtaskId", required = false) String subtaskId,
            @RequestParam(value = "type") int type,
            @RequestParam(value = "subtaskHandler", required = false) String subtaskHandler,
            @RequestParam(value = "subtaskName", required = false) String subtaskName,
            @RequestParam(value = "sDate", required = false) String sDate,
            @RequestParam(value = "eDate", required = false) String eDate,
            @RequestParam(value = "workDate", required = false) String workDate,
            @RequestParam(value = "subtaskProgress", required = false) String subtaskProgress,
            @RequestParam(value = "subtaskState", required = false) String subtaskState) {

        ApiResult<Integer> result = null;
        String reBoolean = ToolUtil.cacheExist(String.valueOf(taskId));
        if (reBoolean.equals("full")) {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.AGAINCOMMIT_FAIL, null, null);
            return result;
        }
        try {
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
                //projectSubtask.setIdd(idd);//编号
                projectSubtask.setWorkDate(workDate);//预计工期
                if (subtaskProgress == "" || subtaskProgress == null) {
                    projectSubtask.setSubtaskprogress("0");//进度
                } else {
                    projectSubtask.setSubtaskprogress(subtaskProgress);//进度
                }

                projectSubtask.setSubtaskstate(subtaskState);//子任务状态 值待定*******************************************
                projectSubtask.setCreateDate(str);

                int i = myProjectService.insertProSubTask(projectSubtask);

                logger.error("子任务列表添加成功------"+i+"子任务名称："+subtaskName);

                SubtaskLogRecord subtaskLogRecord = new SubtaskLogRecord();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                java.util.Date date2 = new java.util.Date();
                String str2 = sdf.format(date2);

                subtaskLogRecord.setType("7");//类型:分配
                subtaskLogRecord.setDate(str2);//创建时间
                subtaskLogRecord.setEmp(subtaskHandler);//操作人
                subtaskLogRecord.setExplain("添加任务");//说明

                Map<String,Object> map = new HashMap<>();
                map.put("taskId",taskId);
                map.put("subtaskHandler",subtaskHandler);
                map.put("subtaskName",subtaskName);

                //取subtaskId
                ProjectSubtask projectSubtask1 = projectSubtaskMapper.selectProSubtaskByMap(map);

                subtaskLogRecord.setSubtaskid(projectSubtask1.getSubtaskId());//项目id

                //插入子任务日志
                int ilog = myProjectService.insertSubTaskLogRecord(subtaskLogRecord);

                TaskLogRecord taskLogRecord = new TaskLogRecord();

                //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
                taskLogRecord.setType("7");
                taskLogRecord.setDate(str2);//创建时间
                taskLogRecord.setEmp(subtaskHandler);//操作人
                taskLogRecord.setExplain("分配子任务");//说明
                taskLogRecord.setTaskid(taskId);

                //插入任务日志
                int sublog = myProjectService.insertTaskLogRecode(taskLogRecord);

                if (i > 0 && ilog > 0 && sublog > 0) {
                    result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
                } else {
                    result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
                }

                //子任务分配成功消息推送
                //获取当前时间
                String PushDate=DateUtil.getYMDHMDate();

                //推送给相应的子任务处理人-组员

                   //获取组员id
                   Integer Uid = systemUserMapper.getUidByName(subtaskHandler);
                   //获取子任务所属的项目名称
                   String proName = OnProMapper.getProNameByTaskId(taskId);
                   //获取子任务所属任务的信息
                   ProjectTask task = OnProMapper.getTaskByTaskId(taskId);

                String postUrl1 = "";
                String postUrl2 = "";
                postUrl1 = "{\"Uid\":" + Uid + ",\"Content\":\"【子任务分配】<br>《" +proName+ "》需您协助实施"+subtaskName+"工作，请及时处理。"
                        + "\\n\\n任务分配:" + task.getHandler()
                        + "\\n\\n任务名称:" + subtaskName
                        + "\\n\\n开始时间:" + sDate
                        + "\\n\\n结束时间:" + eDate
                        + "\\n\\n推送时间:" + PushDate
                        + "\",\"AgentId\":1000011,\"Title\":\"任务分配\",\"Url\":\"\"}";

               /* postUrl2 = "{\"Uid\":" + 1340 + ",\"Content\":\"《" +proName+ "》需您协助实施"+subtaskName+"工作，请及时处理。"
                        + "\\n\\n任务分配:" + task.getHandler()
                        + "\\n\\n任务名称:" + subtaskName
                        + "\\n\\n开始时间:" + sDate
                        + "\\n\\n结束时间:" + eDate
                        + "\\n\\n推送时间:" + PushDate
                        + "\",\"AgentId\":1000011,\"Title\":\"任务分配\",\"Url\":\"\"}";*/

                try {
                    //消息推送-任务分配
                   // httpPostWithJSON(postUrl1);
                   // httpPostWithJSON(postUrl2);
                } catch (Exception e) {
                    e.printStackTrace();
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
                //projectSubtask.setIdd(idd);//编号
                projectSubtask.setSubtaskId(Integer.valueOf(subtaskId));
                projectSubtask.setWorkDate(workDate);//预计工期

                if (subtaskProgress == "" || subtaskProgress == null) {
                    projectSubtask.setSubtaskprogress("0");//进度
                } else {
                    projectSubtask.setSubtaskprogress(subtaskProgress);//进度
                }
                projectSubtask.setSubtaskstate(subtaskState);//子任务状态 值待定*******************************************
                projectSubtask.setCreateDate(str);

                int k = myProjectService.updateProSubTask(projectSubtask);

                logger.error("子任务列表修改成功------"+k+"子任务名称："+subtaskName);
                SubtaskLogRecord subtaskLogRecord = new SubtaskLogRecord();

                java.util.Date date2 = new java.util.Date();
                String str2 = sdf.format(date2);

                //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
                subtaskLogRecord.setType("8");//类型:修改
                subtaskLogRecord.setDate(str2);//创建时间
                subtaskLogRecord.setEmp(subtaskHandler);//操作人
                subtaskLogRecord.setExplain("修改子任务");//说明
                subtaskLogRecord.setSubtaskid(Integer.valueOf(subtaskId));//项目id

                //插入日志
                int ilog = myProjectService.insertSubTaskLogRecord(subtaskLogRecord);

                TaskLogRecord taskLogRecord = new TaskLogRecord();

                //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
                taskLogRecord.setType("8");
                taskLogRecord.setDate(str2);//创建时间
                taskLogRecord.setEmp(subtaskHandler);//操作人
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
            } else if (type == 3){

                int j = myProjectService.deleteProSubTaskById(Integer.valueOf(subtaskId));

                logger.error("子任务列表删除成功------"+j+"子任务id："+subtaskId);

                SubtaskLogRecord subtaskLogRecord = new SubtaskLogRecord();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                java.util.Date date = new java.util.Date();
                String str = sdf.format(date);

                //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
                subtaskLogRecord.setType("9");//类型:修改
                subtaskLogRecord.setDate(str);//创建时间
                subtaskLogRecord.setEmp(subtaskHandler);//操作人
                subtaskLogRecord.setExplain("删除子任务");//说明
                subtaskLogRecord.setSubtaskid(Integer.valueOf(subtaskId));//项目id

                //插入日志
                int ilog = myProjectService.insertSubTaskLogRecord(subtaskLogRecord);

                TaskLogRecord taskLogRecord = new TaskLogRecord();

                //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
                taskLogRecord.setType("9");
                taskLogRecord.setDate(str);//创建时间
                taskLogRecord.setEmp(subtaskHandler);//操作人
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
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("我的项目任务分配详请页（子任务列表添加、修改、删除）错误信息：" + e);
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
    @ApiOperation(value = "我的项目子任务详情页（基本信息+项目信息+日志记录）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getSubTaskDetails", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getSubTaskDetails(
            @RequestParam(value = "subtaskId") int subtaskId) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {
            //基本信息+任务信息
            ProjectSubtask projectSubtask = myProjectService.getProjectSubtaskById(subtaskId);

            map.put("projectSubtask", projectSubtask);

            //日志记录
            List<SubtaskLogRecord> logList = myProjectService.getSubtaskLogList(subtaskId);

            map.put("subtaskLog", logList);

            list.add(map);

            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("我的项目子任务详情页（基本信息+项目信息+参与组）错误信息：" + e);
        }
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
            @ApiImplicitParam(paramType = "query", name = "id", value = "我的项目主键id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型1：开始:2：需求调整:3：会议 4：更新 5：预验收", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "progress", value = "进度", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "explain", value = "备注说明", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "filePath", value = "附件地址", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userName", value = "操作人", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "addType", value = "添加类型 1：项目开发日志 2：任务开发日志 3：子任务开发日志", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/addProDeveLog", method = RequestMethod.POST)
    public ApiResult<Integer> addProDeveLog(@RequestParam(value = "id", required = false) String id,
                                            @RequestParam(value = "proId", required = false) String proId,
                                            @RequestParam(value = "type") String type,
                                            @RequestParam(value = "progress", required = false) String progress,
                                            @RequestParam(value = "explain") String explain,
                                            @RequestParam(value = "filePath", required = false) String filePath,
                                            @RequestParam(value = "userName") String userName,
                                            @RequestParam(value = "taskId", required = false) String taskId,
                                            @RequestParam(value = "subtaskId", required = false) String subtaskId,
                                            @RequestParam(value = "addType") int addType) {

        ApiResult<Integer> result = null;
        String reBoolean = ToolUtil.cacheExist(explain);
        if (reBoolean.equals("full")) {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.AGAINCOMMIT_FAIL, null, null);
            return result;
        }
        try {
            //1：项目开发日志
            if (addType == 1) {
                ProDevelopLog proDevelopLog = new ProDevelopLog();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                java.util.Date date = new java.util.Date();
                String str = sdf.format(date);

                proDevelopLog.setDate(str);//操作时间
                proDevelopLog.setEmp(userName);//操作人
                proDevelopLog.setExplain(explain);//备注说明
                proDevelopLog.setProid(Integer.valueOf(proId));//项目id
                proDevelopLog.setType(type);//类型 1：开始:2：需求调整:3：会议 4：更新 5：预验收
                if (progress == "" || progress == null) {
                    proDevelopLog.setProgress("0");//进度
                } else if (Integer.valueOf(type) == 5) {//5：预验收
                    proDevelopLog.setProgress("100");//进度
                } else {
                    proDevelopLog.setProgress(progress);
                }
                proDevelopLog.setFilepath(filePath);//附件地址

                int i = myProjectService.insertProDeveLog(proDevelopLog);

                ProjectInfo projectInfo = new ProjectInfo();
                projectInfo.setId(Integer.valueOf(id));
                projectInfo.setProid(Integer.valueOf(proId));
                if (progress == "" || progress == null) {
                    projectInfo.setProprogress("0");//进度
                } else if (Integer.valueOf(type) == 5) {//5：预验收
                    projectInfo.setProprogress("100");//进度
                } else {
                    projectInfo.setProprogress(progress);//进度
                }

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
                taskDevelopLog.setTaskid(Integer.valueOf(taskId));//任务id
                if (Integer.valueOf(type) == 5) {
                    taskDevelopLog.setProgress("100");
                } else {
                    taskDevelopLog.setProgress(progress);//进度
                }
                taskDevelopLog.setType(type);//类型 1：开始:2：需求调整:3：会议 4：更新 5：预验收

                int i = myProjectService.insertTaskDevlog(taskDevelopLog);

                ProjectTask projectTask = new ProjectTask();
                projectTask.setTaskId(Integer.valueOf(taskId));//任务id
                projectTask.setTaskprogress(progress);//进度
                projectTask.setCreateDate(str);

                if (Integer.valueOf(type) == 5) {
                    projectTask.setTaskstate("4");//完成
                    projectTask.setTaskprogress("100");
                } else {
                    projectTask.setTaskstate("2");//开发中
                }

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
                subtaskDevelopLog.setSubtaskid(Integer.valueOf(subtaskId));
                if (Integer.valueOf(type) == 5) {
                    subtaskDevelopLog.setProgress("100");
                } else {
                    subtaskDevelopLog.setProgress(progress);//进度
                }
                subtaskDevelopLog.setType(type);

                int i = myProjectService.insertSubTaskDevlog(subtaskDevelopLog);

                ProjectSubtask projectSubtask = new ProjectSubtask();
                projectSubtask.setSubtaskId(Integer.valueOf(subtaskId));
                projectSubtask.setSubtaskprogress(progress);//进度
                projectSubtask.setCreateDate(str);
                if (Integer.valueOf(type) == 5) {
                    projectSubtask.setSubtaskstate("4");
                    projectSubtask.setSubtaskprogress("100");
                } else {
                    projectSubtask.setSubtaskstate("2");//开发中
                }
                //
                int k = myProjectService.updateProSubTask(projectSubtask);

                if (i > 0 && k > 0) {
                    result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
                } else {
                    result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加开发日志错误信息：" + e);
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
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "explain", value = "说明", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userName", value = "操作人", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型1：任务 2：子任务", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/commitTask", method = RequestMethod.POST)
    public ApiResult<Integer> commitTask(
            @RequestParam(value = "taskId", required = false) String taskId,
            @RequestParam(value = "subtaskId", required = false) String subtaskId,
            @RequestParam(value = "explain", required = true) String explain,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "type") int type) {

        ApiResult<Integer> result = null;
        String reBoolean = ToolUtil.cacheExist(userName);
        if (reBoolean.equals("full")) {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.AGAINCOMMIT_FAIL, null, null);
            return result;
        }
        try {
            //提交任务
            if (type == 1) {
                ProjectTask projectTask = new ProjectTask();
                projectTask.setTaskprogress("100");
                projectTask.setTaskId(Integer.valueOf(taskId));
                //任务状态
                //1:未开始  2:开发中 3:预验收  4:完成
                projectTask.setTaskstate("3");

                int i = myProjectService.updateTaskById(projectTask);

                logger.error("提交任务成功------"+i+"任务id："+taskId);
                TaskDevelopLog taskDevelopLog = new TaskDevelopLog();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                java.util.Date date = new java.util.Date();
                String str = sdf.format(date);

                taskDevelopLog.setDate(str);
                taskDevelopLog.setEmp(userName);//操作人
                taskDevelopLog.setExplain(explain);//备注说明
                taskDevelopLog.setTaskid(Integer.valueOf(taskId));//任务id
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

                projectSubtask.setSubtaskId(Integer.valueOf(subtaskId));
                projectSubtask.setSubtaskprogress("100");
                //任务状态
                //1:未开始  2:开发中 3:预验收  4:完成，5：逾期
                projectSubtask.setSubtaskstate("3");

                int i = myProjectService.updateProSubTask(projectSubtask);

                logger.error("提交子任务成功------"+i+"子任务id："+subtaskId);
                SubtaskDevelopLog subtaskDevelopLog = new SubtaskDevelopLog();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                java.util.Date date = new java.util.Date();
                String str = sdf.format(date);

                subtaskDevelopLog.setDate(str);
                subtaskDevelopLog.setEmp(userName);
                subtaskDevelopLog.setExplain(explain);
                subtaskDevelopLog.setSubtaskid(Integer.valueOf(subtaskId));
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
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("我的项目详情页-提交任务错误信息：" + e);
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

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("我的项目开发中详情页（日志记录+开发日志）错误信息：" + e);
        }
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
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型 2：任务 3：子任务", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getSecondLeverType", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getSecondLeverType(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId,
            @RequestParam(value = "type") int type,
            @RequestParam(value = "taskId",required = false) String taskId) {

        ApiResult<List<Map<String, Object>>> result = null;
        try {
            if (type == 2) {
                //任务
                List<Map<String, Object>> taskList = upProjectService.getProjectTaskListMap(proId);

                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, taskList, null);

            } else if (type == 3) {
                //任务
                List<Map<String, Object>> taskList = upProjectService.getProjectTaskListMap(proId);

                String menuLeafIds = StringUtil.toString(MapUtil.collectProperty(taskList, "taskId"));

                String[] Ids = menuLeafIds.split(",");

                Map<String, Object> mapT = new HashMap<>();

                mapT.put("menuLeafIds", Ids);
                //子任务
                List<Map<String, Object>> subtaskList = myProjectService.getSubTaskListMap(mapT);

                if (taskId != "" && taskId != null) {
                    subtaskList = subtaskList.stream().filter(x -> x.get("taskId").toString().equals(taskId)).collect(Collectors.toList());
                }

                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, subtaskList, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据类型查询二级类型名称错误信息：" + e);

        }
        return result;
    }

    /**
     * 我的项目任务分配详情页子任务列表
     * 添加（选择参与部门）
     *
     * @return
     */
    @ApiOperation(value = "我的项目任务分配详情页子任务列表-添加（选择参与部门）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "登录用户id", required = true, dataType = "int")
    })
    @RequestMapping(value = "/getMembersByLoginUser", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getMembersByLoginUser(HttpServletRequest request,
                                                                      @RequestParam(value = "id", required = true) int id) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String reBoolean = ToolUtil.cacheExist(String.valueOf(id));
        if (reBoolean.equals("full")) {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.AGAINCOMMIT_FAIL, null, null);
            return result;
        }

        try {
            SystemUser user = systemUserService.selectByPrimaryKey(id);

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
            /*String department = user.getDepartment();
            department = department.substring(0, 2);

            //当前用户为组长/经理时，可以查看自己和其小组成员相关的项目
            Department did = myProjectService.getDepartmentIdByMent(department);*/

                /*String departmentid = did.getDepartmentid();

                //根据部门id查找小组id
                List<Map<String, Object>> mapList = myProjectService.getSquadId(String.valueOf(departmentid));

                String mentIds = StringUtil.toString(MapUtil.collectProperty(mapList, "squadId"));
                String[] mIds = mentIds.split(",");
                Map<String, Object> mapTid = new HashMap<>();

                mapTid.put("mentIds", mIds);
                //组长/经理其小组成员
                List<Map<String, Object>> mapList1 = myProjectService.getMembers(mapTid);*/

                map.put("members", listMembers);

                list.add(map);

                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("我的项目任务分配详情页子任务列表-添加（选择参与部门）错误信息：" + e);
        }
        return result;
    }
}
