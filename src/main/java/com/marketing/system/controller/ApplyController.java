package com.marketing.system.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.marketing.system.entity.*;
import com.marketing.system.mapper.DepartmentNewMapper;
import com.marketing.system.mapper.SystemUserMapper;
import com.marketing.system.mapper_two.DayReportMapper;
import com.marketing.system.mapper_two.OnlineProMapper;
import com.marketing.system.mapper_two.ProjectInfoMapper;
import com.marketing.system.service.*;
import com.marketing.system.util.*;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.marketing.system.util.WeiXinPushUtil.httpPostWithJSON;

@Api(description = "申请接口", value = "申请接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class ApplyController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ApplyService applyService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private MyProjectService myProjectService;

    @Autowired
    private UpProjectService upProjectService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private DepartmentNewMapper departmentNewMapper;

    @Autowired
    private IndexService indexService;

    @Autowired
    private OnlineProMapper OnProMapper;

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Autowired
    private DayReportMapper DayReportDao;

    @Autowired
    private ProjectInfoMapper proInfoMapper;

    @Value("${ceo.id}")
    private String ceoId;

    @Value("${ceo.phone}")
    private String ceoPhone;

    @Value("${ceo.email}")
    private String ceoEmail;

    @ApiOperation(value = "项目申请")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "proName", value = "项目名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proType", value = "项目类型(1:产品，2：活动)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planSDate", value = "上线时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planEDate", value = "下线时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proDeclare", value = "项目概况", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "creatName", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "filePath", value = "附件地址", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "myDomain", value = "参与部门", required = true, dataType = "String")
    })
    @RequestMapping(value = "/applyProject", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<Integer> applyProject(@RequestParam(value = "proName", required = true) String proName,
                                           @RequestParam(value = "proType", required = true) String proType,
                                           @RequestParam(value = "planSDate", required = true) String planSDate,
                                           @RequestParam(value = "planEDate", required = false) String planEDate,
                                           @RequestParam(value = "proDeclare", required = true) String proDeclare,
                                           @RequestParam(value = "creatName", required = true) String creatName,
                                           @RequestParam(value = "filePath", required = false) String filePath,
                                           //@RequestParam(value = "myDomain", required = false) List<ProjectTask> myDomain
                                           @RequestParam(value = "myDomain", required = true) String myDomain,
                                           HttpServletRequest request, HttpSession session2
    ) throws IOException {

        String reBoolean = ToolUtil.cacheExist(proName);
        if (reBoolean.equals("full")) {
            ApiResult<Integer> r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.AGAINCOMMIT_FAIL, null, null);
            return r;
        }
        ApiResult<Integer> r = null;
        ProjectInfo projectInfo = new ProjectInfo();

        ProjectTask projectTask = new ProjectTask();
        Object o = request.getAttribute("myDomain");

        //获取前端list值
        Gson gson = new Gson();

        List<ProjectTask> list = gson.fromJson(myDomain, new TypeToken<List<ProjectTask>>() {
        }.getType());

        int a = 0;
        //先查询有无项目编号，有则取最大值，无则默认为1开始
        try {
            a = applyService.selectMaxProId();

            if (a == 0) {
                a = 1;
            }
        } catch (Exception e) {
            a = 1;
            logger.error("错误信息：" + e);
        }

        String code = "";
        for (int i = a; i < 999999; i++) {    //for循环
            code = i + "";
            int leng = (code.trim()).length();  //定义长度
            if (leng == 1) {
                code = "00000" + (i + 1);
                break;
            } else if (leng == 2) {
                code = "0000" + (i + 1);
                break;
            } else if (leng == 3) {
                code = "000" + (i + 1);
                break;
            } else if (leng == 4) {
                code = "00" + (i + 1);
                break;
            } else if (leng == 5) {
                code = "0" + (i + 1);
                break;
            }
        }

        //Members members = applyService.selectSquadIdByMember(creatName);

        SystemUser systemUser = systemUserService.selectIdByName(creatName);

        projectInfo.setProid(Integer.valueOf(code));//任务id
        projectInfo.setProname(proName);//项目名称
        projectInfo.setProtype(proType);//项目类型
        projectInfo.setPlansdate(planSDate);//预计上线时间
        projectInfo.setPlanedate(planEDate);//预计下线时间
        projectInfo.setProdeclare(proDeclare);//项目概况
        projectInfo.setCreater(creatName);//创建人
        projectInfo.setProprogress("0");//项目进度
        projectInfo.setProstate("1");//项目状态(1:立项待审批，2：开发中，3：上线带审批，4：完成，5：驳回，6：作废,7:逾期)
        projectInfo.setUserId(systemUser.getId());

        projectInfo.setCreaterSquadId(String.valueOf(systemUser.getUserGroupId()));//小组id

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date = new java.util.Date();
        String str = sdf.format(date);

        projectInfo.setCreatedate(str);//创建时间

        projectInfo.setProfilepath(filePath);

        //创建项目，则该创建人成为项目发起人
        int v = applyService.insertApplyProject(projectInfo);

        logger.error("创建项目成功------" + v + "项目名称：" + proName);

        int ap = 0;
        int sum = 0;
        Map<String, Object> map1 = new HashMap<>();
        for (ProjectTask  Task: list) {
            projectTask.setProid(Integer.valueOf(code));//项目Id
            projectTask.setSquadId(Task.getSquadId());//参与组id

            String squadId = Task.getSquadId();

            /*String squad = upProjectService.selectSquadBySquadId(Integer.parseInt(squadId));

            map1.put("UserGroup", squad);*/

            Map<String, Object> stringObjectMap = new HashMap<>();

            stringObjectMap.put("UserGroupId", squadId);
            //对应组所有人信息
            //List<Map<String, Object>> systemUserList = systemUserService.selectUserGroupBydepartment(map1);
            List<Map<String, Object>> systemUserList = systemUserService.getGroupMembers(stringObjectMap);

            List<Map<String, Object>> systemUserListNew = new ArrayList<>();

            for (Map sys : systemUserList) {
                if (sys.get("duty") != "" && sys.get("duty") != null) {
                    if (String.valueOf(sys.get("duty")).contains("组长")) {
                        systemUserListNew.add(sys);
                    }
                }
            }
            String handler = "";
            for (Map handle : systemUserListNew) {
                handler = String.valueOf(handle.get("UserName"));

            }
            projectTask.setHandler(handler);
            projectTask.setTaskname(Task.getTaskname());//任务名称
            projectTask.setSdate(Task.getSdate());//任务开始时间
            projectTask.setEdate(Task.getEdate());//任务结束时间
            projectTask.setWorkDate(Task.getWorkDate());//任务工时

            projectTask.setCreateDate(str);//创建时间
            projectTask.setTaskprogress("0");//任务进度
            projectTask.setTaskstate("1");

            sum++;
            projectTask.setIdd(sum);//项目编号
            //创建任务
            ap = applyService.insertSelective(projectTask);

            logger.error("创建任务成功------" + ap + "任务名称：" + Task.getTaskname());

            //任务分配完成时，推送消息给相关负责人
            Integer Uid = systemUserMapper.getUidByName(handler);
            //获取当前时间
            String PushDate = DateUtil.getYMDHMDate();

            //推送给相应的任务处理人-组长
            String postUrl1 = "";
            String postUrl2 = "";
            String postUrl3 = "";
            postUrl1 = "{\"Uid\":" + Uid + ",\"Content\":\"【任务分配】\\n\\n《" + proName + "》需您协助实施" + Task.getTaskname() + "工作，请及时处理。"
                    + "\\n\\n任务分配:" + creatName
                    + "\\n\\n任务名称:" + Task.getTaskname()
                    + "\\n\\n开始时间:" + Task.getSdate()
                    + "\\n\\n结束时间:" + Task.getEdate()
                    + "\\n\\n推送时间:" + PushDate
                    + "\",\"AgentId\":1000011,\"Title\":\"任务分配\",\"Url\":\"\"}";

            /*postUrl1 = "{\"Uid\":" + 217 + ",\"Content\":\"《" + proName + "》需您协助实施" + task.getTaskname() + "工作，请及时处理。"
                    + "\\n\\n任务分配:" + creatName
                    + "\\n\\n任务名称:" + task.getTaskname()
                    + "\\n\\n开始时间:" + task.getSdate()
                    + "\\n\\n结束时间:" + task.getEdate()
                    + "\\n\\n推送时间:" + PushDate
                    + "\",\"AgentId\":1000011,\"Title\":\"任务分配\",\"Url\":\"\"}";*/

            //推送给郑洁
            postUrl2 = "{\"Uid\":" + 1340 + ",\"Content\":\"【任务分配】\\n\\n《" + proName + "》需您协助实施" + Task.getTaskname() + "工作，请及时处理。"
                    + "\\n\\n任务分配:" + creatName
                    + "\\n\\n任务名称:" + Task.getTaskname()
                    + "\\n\\n开始时间:" + Task.getSdate()
                    + "\\n\\n结束时间:" + Task.getEdate()
                    + "\\n\\n推送时间:" + PushDate
                    + "\",\"AgentId\":1000011,\"Title\":\"任务分配\",\"Url\":\"\"}";

            //推送给陈总
            postUrl3 = "{\"Uid\":" + 217 + ",\"Content\":\"【任务分配】\\n\\n《" + proName + "》需您协助实施" + Task.getTaskname() + "工作，请及时处理。"
                    + "\\n\\n任务分配:" + creatName
                    + "\\n\\n任务名称:" + Task.getTaskname()
                    + "\\n\\n开始时间:" + Task.getSdate()
                    + "\\n\\n结束时间:" + Task.getEdate()
                    + "\\n\\n推送时间:" + PushDate
                    + "\",\"AgentId\":1000011,\"Title\":\"任务分配\",\"Url\":\"\"}";

            try {
                //消息推送-任务分配
                httpPostWithJSON(postUrl1);
                httpPostWithJSON(postUrl2);
                httpPostWithJSON(postUrl3);
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

                    Integer taskId=Task.getTaskId();
                    //根据任务Id查找项目信息
                    ProjectInfo pro = proInfoMapper.getProjectInfoByTaskId(taskId);
                    Integer groupId=Integer.parseInt(Task.getSquadId());
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
                        postUrl2 = "{\"Uid\":" + 1340 + ",\"Content\":\"【延迟预警1级】\\n\\n《" +pro.getProname()+ "》需"+Task.getHandler()+"协助实施"+Task.getTaskname()+"工作，现已超过12小时未处理，请督促处理。"
                                + "\\n\\n任务分配:" + Task.getHandler()
                                + "\\n\\n任务名称:" + Task.getTaskname()
                                + "\\n\\n开始时间:" + Task.getSdate()
                                + "\\n\\n结束时间:" + Task.getEdate()
                                + "\\n\\n推送时间:" + PushDate
                                + "\",\"AgentId\":1000011,\"Title\":\"延迟预警\",\"Url\":\"\"}";
                        logger.error("延迟预警内容："+postUrl2);
                    }
                    try {
                        //消息推送-延迟预警
                        String Str = httpPostWithJSON(postUrl1);
                        System.out.println(Str);
                        httpPostWithJSON(postUrl2);
                        logger.error("消息推送-延迟预警成功！");
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("消息推送-延迟预警出错："+e);
                    }
                }
            };
            timer.schedule(threadTask, 5*60*1000);





    }


        ProLogRecord proLogRecord = new ProLogRecord();

        java.util.Date date2 = new java.util.Date();
        String str2 = sdf.format(date2);

        proLogRecord.setType("1");//类型 创建
        proLogRecord.setDate(str2);//创建时间
        proLogRecord.setEmp(creatName);//操作人
        proLogRecord.setExplain("创建项目");//说明
        proLogRecord.setProid(Integer.valueOf(code));//项目id
        proLogRecord.setFilepath(filePath);
        //插入日志
        int ilog = applyService.insertProLogRecord(proLogRecord);

        //发起小组
        String group = departmentNewMapper.getGroupByCreater(creatName);

        //附件数量
        if (filePath == "" || filePath == null) {
            filePath = "0";
        } else {
            filePath = "1";
        }


        //2、立项待审批
        Integer lx_cp = indexService.getLxProjects("");
        //2、立项待审批L
        Integer lx_hd = indexService.getHdLxProjects("");

        Integer lx = lx_cp + lx_hd;


        String proTypeName = "";
        //项目类型(1:产品，2：活动)
        if ("1".equals(proType)) {
            proTypeName = "产品";
        } else {
            proTypeName = "活动";
        }
        if (v > 0 && ilog > 0 && ap > 0) {
            r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            String postUrl = "";
            postUrl = "{\"Uid\":" + ceoId + ",\"Content\":\"【项目立项】\\n\\n您有关于《" + proName + "》的立项申请，请及时处理。"
                    + "\\n\\n发起小组:" + group
                    + "\\n\\n发起人:" + creatName
                    + "\\n\\n项目名称:" + proName
                    + "\\n\\n项目类型:" + proTypeName
                    + "\\n\\n上线时间:" + planSDate
                    + "\\n\\n附件数量:" + filePath + "个"
                    + "\\n\\n立项审批总量:" + lx + "个"
                    + "\\n\\n推送时间:" + str2
                    + "\",\"AgentId\":1000011,\"Title\":\"创建\",\"Url\":\"\"}";
            try {
                //消息推送-回复
                httpPostWithJSON(postUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //数据研发中心柏铭成向您申请对《大数据分析平台项目》实施项目立项，请您及时处理。
            ToolUtil.sendMsg(ceoPhone, group + creatName + "向您申请对《" + projectInfo.getProname() + "》实施项目立项审批，请您及时处理。");

            //发送邮件
            ToolUtil.sendEmial(ceoEmail, "关于《" + projectInfo.getProname() + "》的立项申请审批", "陈总：<br>" + group + creatName + "向您发起名为《" + projectInfo.getProname() + "》的立项申请，该项目类型为" + proTypeName + "，要求上线时间为" + planSDate + "，提交的附件数量为" + filePath + "个。请您及时处理。项目简介如下：<br>" +
                    projectInfo.getProdeclare() + "<br>" +
                    "点击进入项目审批页：https://192.168.11.132:2222<br>" +
                    "注：您目前还有" + lx + "个未处理的立项申请。<br>");
        } else {
            r = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
        }

        return r;
    }


    @ApiOperation(value = "获取部门")
    @RequestMapping(value = "/getDepartment", method = RequestMethod.POST)
    public ApiResult<List<Department>> getDepartment() {

        ApiResult<List<Department>> r = null;

        try {
            List<Department> department = null;

            // List<DepartmentNew> department = departmentNewMapper.getDepartment();

            r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, department, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("错误信息：" + e);
        }

        return r;

    }

    @ApiOperation(value = "获取所有中心部门小组")
    @RequestMapping(value = "/getAllDepartment", method = RequestMethod.POST)
    public ApiResult<List<DepartmentNew>> getAllDepartment() {

        ApiResult<List<DepartmentNew>> r = null;
        try {

            List<DepartmentNew> department = departmentNewMapper.getDepartment();

            r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, department, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("错误信息：" + e);
        }

        return r;

    }

    @ApiOperation(value = "获取部门小组")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "参与部门", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/getGroup", method = RequestMethod.POST)
    public ApiResult<List<DepartmentNew>> getGroup(@RequestParam(value = "id", required = false) Integer id) {

        ApiResult<List<DepartmentNew>> r = null;
        List<DepartmentNew> group = new ArrayList<>();
        try {
            if (id == null) {
                //group = groupService.getGroupNo();
                group = departmentNewMapper.getGroupNo();

            } else {
                //group = groupService.getGroup(id);
                group = departmentNewMapper.getGroup(id);

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("错误信息：" + e);
        }

        r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, group, null);

        return r;

    }

    @ApiOperation(value = "获取项目相关部门（回复人位置）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getMsgPushDepartment", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getMsgPushDepartment(@RequestParam(value = "proId", required = true) Integer proId) {

        ApiResult<List<Map<String, Object>>> r = null;

        try {
            //根据项目id查询任务相关信息
            List<Map<String, Object>> taskList = applyService.selecttaskInfoByProId(proId);

            //小组id
            String menuLeafIdsTwo = StringUtil.toString(MapUtil.collectProperty(taskList, "squadId"));

            String[] IdsT = menuLeafIdsTwo.split(",");

            Map<String, Object> mapS = new HashMap<>();

            mapS.put("mentIds", IdsT);

            //项目涉及部门
            List<Map<String, Object>> groupList = departmentNewMapper.groupList(mapS);

            //根据squadId(小组id)查找小组所属部门id
            /*List<Map<String, Object>> squadList = applyService.getSquadList(mapS);

            String menuLeafIdsThree = StringUtil.toString(MapUtil.collectProperty(squadList, "departmentId"));

            String[] IdsTh = menuLeafIdsThree.split(",");

            Map<String, Object> mapThree = new HashMap<>();

            mapThree.put("mentIds", IdsTh);

            //根据部门id查找部门名称
            List<Map<String, Object>> departmentList = applyService.getDepartmentList(mapThree);*/

            r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, groupList, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取项目相关部门：" + e);
        }

        return r;

    }

    @ApiOperation(value = "上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> uploadFile(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        ApiResult<Map<String, String>> r = null;

        if (ServletFileUpload.isMultipartContent(request)) {

            try {
                request.setCharacterEncoding("utf-8");

                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iter = multipartRequest.getFileNames();

                session = request.getSession(true);
                //遍历list，每迭代一个FileItem对象，调用其isFormField方法判断是否是上传文件
                while (iter.hasNext()) {
                    MultipartFile mfile = multipartRequest.getFile(iter.next().toString());

                    String fileName = mfile.getOriginalFilename();

                    String fileOldName = mfile.getOriginalFilename();

                    logger.error("原文件名：" + fileName);
                    logger.error("原文件名：" + fileOldName);

                    // 新文件名（唯一）
                    String newFileName = new Date().getTime() + fileOldName;
                    logger.error("新文件名：" + newFileName);


                    Files.copy(mfile.getInputStream(), Paths.get("static", newFileName));

                    String pathFile = "http://report.wsloan.com:8888/projectManage/" + newFileName;
                    session.setAttribute("uploadPath", pathFile);

                    Map<String, String> objectMap = new HashMap<>();
                    objectMap.put("uploadPath", pathFile);

                    r = new ApiResult<Map<String, String>>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, objectMap, null);
                }

            } catch (IllegalStateException e) {
                e.printStackTrace();
                r = new ApiResult<Map<String, String>>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
                logger.error("上传文件错误信息：" + e);
            } catch (Exception e) {
                e.printStackTrace();
                r = new ApiResult<Map<String, String>>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
                logger.error("上传文件错误信息：" + e);
            }

        }

        return r;
    }

    /**
     * 消息推送-回复
     *
     * @param pushId
     * @param userName
     * @param content
     * @param type
     * @return
     */
    @ApiOperation(value = "消息推送-回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pushId", value = "选择回复人id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "subtaskId", value = "子任务id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userName", value = "创建人", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "回复内容", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "filePath", value = "上传附件路径", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "推送消息标题", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型1：项目 2：任务 3：子任务", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/messagePush", method = RequestMethod.POST)
    public ApiResult<String> messagePush(
            @RequestParam(value = "pushId", required = true) String pushId,
            @RequestParam(value = "proId", required = false) String proId,
            @RequestParam(value = "taskId", required = false) String taskId,
            @RequestParam(value = "subtaskId", required = false) String subtaskId,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "content", required = true) String content,
            @RequestParam(value = "filePath", required = false) String filePath,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = true) int type) {

        ApiResult<String> result = null;

        String reBoolean = ToolUtil.cacheExist(content);
        if (reBoolean.equals("full")) {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.AGAINCOMMIT_FAIL, null, null);
            return result;
        }

        String postUrl = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> listId = new ArrayList<String>();
        if (pushId.contains(",")) {
            String[] id = pushId.split(",");

            for (int i = 0; i < id.length; i++) {
                listId.add(id[i]);
            }
        } else {
            String id = pushId;
            listId.add(id);
        }

        try {

            java.util.Date date2 = new java.util.Date();
            String str2 = sdf.format(date2);

            int i = 0;
            //项目-插入日志记录
            if (type == 1) {

                if (proId != null && proId != "") {
                    ProLogRecord proLogRecord = new ProLogRecord();

                    //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
                    proLogRecord.setType("10");//类型 回复
                    proLogRecord.setDate(str2);//创建时间
                    proLogRecord.setEmp(userName);//操作人
                    proLogRecord.setExplain(content);//说明
                    proLogRecord.setProid(Integer.valueOf(proId));//项目id
                    proLogRecord.setFilepath(filePath);//文件上传路径

                    i = applyService.insertProLogRecord(proLogRecord);
                }

                ProjectInfo projectInfo = myProjectService.getProjectInfoByProId(Integer.valueOf(proId));

                for (String s : listId) {
                    if (title == "" || title == null) {
                        postUrl = "{\"Uid\":" + s + ",\"Content\":\"创建人:" + userName
                                + "\\n\\n项目名称:" + projectInfo.getProname() + "\\n\\n推送内容:" + content
                                + "\\n\\n推送时间:" + str2
                                + "\",\"AgentId\":1000011,\"Title\":\"创建\",\"Url\":\"\"}";

                    } else {
                        postUrl = "{\"Uid\":" + s + ",\"Content\":\"创建人:" + userName
                                + "\\n\\n项目名称:" + projectInfo.getProname() + "\\n\\n推送内容:" + content
                                + "\\n\\n推送时间:" + str2
                                + "\",\"AgentId\":1000011,\"Url\":\"\"}";
                    }

                    //消息推送-回复
                    httpPostWithJSON(postUrl);
                }


                //任务-插入日志记录
            } else if (type == 2) {
                TaskLogRecord taskLogRecord = new TaskLogRecord();

                //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
                taskLogRecord.setType("10");
                taskLogRecord.setDate(str2);//创建时间
                taskLogRecord.setEmp(userName);//操作人
                taskLogRecord.setExplain(content);//说明
                taskLogRecord.setTaskid(Integer.valueOf(taskId));
                taskLogRecord.setFilepath(filePath);//文件上传路径

                //插入任务日志
                i = myProjectService.insertTaskLogRecode(taskLogRecord);

                ProjectInfo projectInfo = applyService.getProjectInfoByTaskId(Integer.valueOf(taskId));

                for (String s : listId) {
                    if (title == "" || title == null) {
                        postUrl = "{\"Uid\":" + s + ",\"Content\":\"创建人:" + userName
                                + "\\n\\n项目名称:" + projectInfo.getProname() + "\\n\\n推送内容:" + content
                                + "\\n\\n推送时间:" + str2
                                + "\",\"AgentId\":1000011,\"Title\":\"创建\",\"Url\":\"\"}";

                    } else {
                        postUrl = "{\"Uid\":" + s + ",\"Content\":\"创建人:" + userName
                                + "\\n\\n项目名称:" + projectInfo.getProname() + "\\n\\n推送内容:" + content
                                + "\\n\\n推送时间:" + str2
                                + "\",\"AgentId\":1000011,\"Url\":\"\"}";
                    }

                    //消息推送-回复
                    httpPostWithJSON(postUrl);
                }


                //子任务-插入日志记录
            } else {
                SubtaskLogRecord subtaskLogRecord = new SubtaskLogRecord();

                //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
                subtaskLogRecord.setType("10");//类型:修改
                subtaskLogRecord.setDate(str2);//创建时间
                subtaskLogRecord.setEmp(userName);//操作人
                subtaskLogRecord.setExplain(content);//说明
                subtaskLogRecord.setSubtaskid(Integer.valueOf(subtaskId));//项目id
                subtaskLogRecord.setFilepath(filePath);//文件上传路径

                //插入日志
                i = myProjectService.insertSubTaskLogRecord(subtaskLogRecord);

                ProjectInfo projectInfo = applyService.getProjectInfoBySubTaskId(Integer.valueOf(subtaskId));

                for (String s : listId) {
                    if (title == "" || title == null) {
                        postUrl = "{\"Uid\":" + s + ",\"Content\":\"创建人:" + userName
                                + "\\n\\n项目名称:" + projectInfo.getProname() + "\\n\\n推送内容:" + content
                                + "\\n\\n推送时间:" + str2
                                + "\",\"AgentId\":1000011,\"Title\":\"创建\",\"Url\":\"\"}";

                    } else {
                        postUrl = "{\"Uid\":" + s + ",\"Content\":\"创建人:" + userName
                                + "\\n\\n项目名称:" + projectInfo.getProname() + "\\n\\n推送内容:" + content
                                + "\\n\\n推送时间:" + str2
                                + "\",\"AgentId\":1000011,\"Url\":\"\"}";

                    }

                    //消息推送-回复
                    httpPostWithJSON(postUrl);
                }


            }

            if (i > 0 || proId == null || proId == "") {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new ApiResult<>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            logger.error("消息推送错误信息：" + e);
        }


        return result;

    }

}
