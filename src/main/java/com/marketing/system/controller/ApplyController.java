package com.marketing.system.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.marketing.system.entity.*;
import com.marketing.system.service.*;
import com.marketing.system.util.*;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
                                           @RequestParam(value = "myDomain",required = true)  String myDomain,
                                           HttpServletRequest request,HttpSession session2
                                           ) throws IOException {

        String reBoolean = ToolUtil.cacheExist(proName);
        if (reBoolean.equals("full")) {
            ApiResult<Integer> r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE,Constant.AGAINCOMMIT_FAIL,null,null);
            return r;
        }
        ApiResult<Integer> r = null;
        ProjectInfo projectInfo = new ProjectInfo();

        ProjectTask projectTask = new ProjectTask();
        Object o = request.getAttribute("myDomain");

        //获取前端list值
        Gson gson = new Gson();

        List<ProjectTask> list = gson.fromJson(myDomain, new TypeToken<List<ProjectTask>>(){}.getType());

        int a = 0;
        //先查询有无项目编号，有则取最大值，无则默认为1开始
        try {
            a = applyService.selectMaxProId();

            if (a == 0) {
                a = 1;
            }
        } catch (Exception e) {
            a = 1;
            logger.error("错误信息："+e);
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

        Members members = applyService.selectSquadIdByMember(creatName);

        projectInfo.setProid(Integer.valueOf(code));//任务id
        projectInfo.setProname(proName);//项目名称
        projectInfo.setProtype(proType);//项目类型
        projectInfo.setPlansdate(planSDate);//预计上线时间
        projectInfo.setPlanedate(planEDate);//预计下线时间
        projectInfo.setProdeclare(proDeclare);//项目概况
        projectInfo.setCreater(creatName);//创建人
        projectInfo.setProprogress("0");//项目进度
        projectInfo.setProstate("1");//项目状态(1:立项待审批，2：开发中，3：上线带审批，4：完成，5：驳回，6：作废,7:逾期)

        projectInfo.setCreaterSquadId(members.getSquadid());//小组id

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date = new java.util.Date();
        String str = sdf.format(date);

        projectInfo.setCreatedate(str);//创建时间

        projectInfo.setProfilepath(filePath);

        //创建项目，则该创建人成为项目发起人
        int v = applyService.insertApplyProject(projectInfo);

        int ap = 0;
        int sum = 0;
        Map<String, Object> map1 = new HashMap<>();
        for (ProjectTask task : list) {
            projectTask.setProid(Integer.valueOf(code));//项目Id
            projectTask.setSquadId(task.getSquadId());//参与组id

            String squadId = task.getSquadId();

            String squad = upProjectService.selectSquadBySquadId(Integer.parseInt(squadId));

            map1.put("UserGroup", squad);

            //对应组所有人信息
            List<Map<String, Object>> systemUserList = systemUserService.selectUserGroupBydepartment(map1);

            List<Map<String, Object>> systemUserListNew = new ArrayList<>();

            for (Map sys : systemUserList) {
                if (sys.get("duty") != "" && sys.get("duty") != null) {
                    if (String.valueOf(sys.get("duty")).contains("组长")) {
                        systemUserListNew.add(sys);
                    }
                }
            }
            String handler ="";
            for (Map handle :systemUserListNew) {
                handler = String.valueOf(handle.get("UserName"));

            }
            projectTask.setHandler(handler);
            projectTask.setTaskname(task.getTaskname());//任务名称
            projectTask.setSdate(task.getSdate());//任务开始时间
            projectTask.setEdate(task.getEdate());//任务结束时间
            projectTask.setWorkDate(task.getWorkDate());//任务工时

            projectTask.setCreateDate(str);//创建时间
            projectTask.setTaskprogress("0");//任务进度
            projectTask.setTaskstate("1");

            sum ++;
            projectTask.setIdd(sum);//项目编号
            //创建任务
            ap = applyService.insertSelective(projectTask);

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


        if (v > 0 && ilog > 0 && ap > 0) {
            r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, null, null);
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
            List<Department> department = departmentService.getDepartment();

            r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, department, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("错误信息："+e);
        }

        return r;

    }

    @ApiOperation(value = "获取部门小组")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "参与部门", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/getGroup", method = RequestMethod.POST)
    public ApiResult<List<Group>> getGroup(@RequestParam(value = "id", required = false) Integer id) {

        ApiResult<List<Group>> r = null;
        List<Group> group = new ArrayList<>();
        try {
            if (id == null) {
                group = groupService.getGroupNo();
            } else {
                group = groupService.getGroup(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("错误信息："+e);
        }

        r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, group, null);

        return r;

    }

    @ApiOperation(value = "获取项目相关部门（回复人）")
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

            //根据squadId(小组id)查找小组所属部门id
            List<Map<String, Object>> squadList = applyService.getSquadList(mapS);

            String menuLeafIdsThree = StringUtil.toString(MapUtil.collectProperty(squadList, "departmentId"));

            String[] IdsTh = menuLeafIdsThree.split(",");

            Map<String, Object> mapThree = new HashMap<>();

            mapThree.put("mentIds", IdsTh);

            //根据部门id查找部门名称
            List<Map<String, Object>> departmentList = applyService.getDepartmentList(mapThree);

            r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, departmentList, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取项目相关部门："+e);
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

                    logger.info("原文件名：" + fileName);
                    logger.info("原文件名：" + fileOldName);

                    // 新文件名（唯一）
                    String newFileName = new Date().getTime() + fileOldName;
                    logger.info("新文件名：" + newFileName);


                    Files.copy(mfile.getInputStream(), Paths.get("static", newFileName));

                    String pathFile = "http://192.168.3.26:5826/" + newFileName;
                    session.setAttribute("uploadPath", pathFile);

                    Map<String,String> objectMap = new HashMap<>();
                    objectMap.put("uploadPath",pathFile);

                    r = new ApiResult<Map<String, String>>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, objectMap, null);
                }

            } catch (IllegalStateException e) {
                e.printStackTrace();
                r = new ApiResult<Map<String, String>>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
                logger.error("上传文件错误信息："+e);
            }catch (Exception e) {
                e.printStackTrace();
                r = new ApiResult<Map<String, String>>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
                logger.error("上传文件错误信息："+e);
            }

        }

        return r;
    }

    /**
     * 消息推送-回复
     * @param id
     * @param userName
     * @param content
     * @param type
     * @return
     */
    @ApiOperation(value = "消息推送-回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "选择回复人id", required = true, dataType = "Integer"),
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
            @RequestParam(value = "id", required = true) int id,
            @RequestParam(value = "proId", required = false) String proId,
            @RequestParam(value = "taskId", required = false) String taskId,
            @RequestParam(value = "subtaskId", required = false) String subtaskId,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "content", required = true) String content,
            @RequestParam(value = "filePath", required = false) String filePath,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = true) int type){

        ApiResult<String> result = null;

        String reBoolean = ToolUtil.cacheExist(content);
        if (reBoolean.equals("full")) {
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE,Constant.AGAINCOMMIT_FAIL,null,null);
            return result;
        }

        String postUrl = "";
        if (title.equals("")) {
            postUrl = "{\"Uid\":" + id + ",\"Content\":\"创建人:" + userName
                    + "\\n\\n项目管理系统:" + "测试" + "\\n\\n内容:" + content
                    + "\",\"AgentId\":1000011,\"Title\":\"创建\",\"Url\":\"http://192.168.3.26:5826/index?username=王东&password=5994471ABB01112AFCC18159F6CC74B4F511B99806DA59B3CAF5A9C173CACFC5\"}";

        } else {
            postUrl = "{\"Uid\":" + id + ",\"Content\":\"创建人:" + userName
                    + "\\n\\n项目管理系统:" + "测试" + "\\n\\n内容:" + content
                    + "\",\"AgentId\":1000011,\"Title\":"+title+",\"Url\":\"http://192.168.3.26:5826/index?username=王东&password=5994471ABB01112AFCC18159F6CC74B4F511B99806DA59B3CAF5A9C173CACFC5\"}";

        }
        try {
            //消息推送-回复
            httpPostWithJSON(postUrl);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date2 = new java.util.Date();
            String str2 = sdf.format(date2);

            int i = 0;
            //项目-插入日志记录
            if (type == 1) {
                ProLogRecord proLogRecord = new ProLogRecord();

                //日志类型(1:创建 2:立项待审批，3:提交上线，4:上线审批（完成），5:驳回，6:作废，7:分配，8:修改，9:删除，10:回复，11:附件)
                proLogRecord.setType("10");//类型 回复
                proLogRecord.setDate(str2);//创建时间
                proLogRecord.setEmp(userName);//操作人
                proLogRecord.setExplain(content);//说明
                proLogRecord.setProid(Integer.valueOf(proId));//项目id
                proLogRecord.setFilepath(filePath);//文件上传路径

                i = applyService.insertProLogRecord(proLogRecord);

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

            }

            if (i > 0) {
                result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE,Constant.OPERATION_SUCCESS,null,null);
            } else {
                result = new ApiResult<>(Constant.FAIL_CODE_VALUE,Constant.OPERATION_FAIL,null,null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new ApiResult<>(Constant.FAIL_CODE_VALUE,Constant.OPERATION_FAIL,null,null);
            logger.error("消息推送错误信息："+e);
        }


        return result;

    }

}
