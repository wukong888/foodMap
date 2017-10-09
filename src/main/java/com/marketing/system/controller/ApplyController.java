package com.marketing.system.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.marketing.system.entity.*;
import com.marketing.system.service.ApplyService;
import com.marketing.system.service.DepartmentService;
import com.marketing.system.service.GroupService;
import com.marketing.system.util.ApiResult;
import com.marketing.system.util.Constant;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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


    @ApiOperation(value = "项目申请")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "proName", value = "项目名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proType", value = "项目类型(1:产品，2：活动)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planSDate", value = "上线时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planEDate", value = "下线时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "proDeclare", value = "项目概况", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "creatName", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "myDomain", value = "参与部门", required = true, dataType = "String")
    })
    @RequestMapping(value = "/applyProject", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<Integer> applyProject(@RequestParam(value = "proName", required = true) String proName,
                                           @RequestParam(value = "proType", required = true) String proType,
                                           @RequestParam(value = "planSDate", required = true) String planSDate,
                                           @RequestParam(value = "planEDate", required = true) String planEDate,
                                           @RequestParam(value = "proDeclare", required = true) String proDeclare,
                                           @RequestParam(value = "creatName", required = true) String creatName,
                                           //@RequestParam(value = "myDomain", required = false) List<ProjectTask> myDomain
                                           @RequestParam(value = "myDomain",required = true)  String myDomain,
                                           HttpServletRequest request
                                           ) throws IOException {

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

        projectInfo.setId(1);
        projectInfo.setProid(Integer.valueOf(code));//任务id
        projectInfo.setProname(proName);//项目名称
        projectInfo.setProtype(proType);//项目类型
        projectInfo.setPlansdate(planSDate);//预计上线时间
        projectInfo.setPlanedate(planEDate);//预计下线时间
        projectInfo.setProdeclare(proDeclare);//项目概况
        projectInfo.setCreater(creatName);//创建人

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date = new java.util.Date();
        String str = sdf.format(date);

        projectInfo.setCreatedate(str);//创建时间

        Session session = SecurityUtils.getSubject().getSession();

        String filePath = String.valueOf(session.getAttribute("uploadPath"));

        projectInfo.setProfilepath(filePath);

        //创建项目，则该创建人成为项目发起人
        int v = applyService.insertApplyProject(projectInfo);

        int ap = 0;
        int sum = 0;
        for (ProjectTask task : list) {
            projectTask.setProid(Integer.valueOf(code));//项目Id
            projectTask.setSquadId(task.getSquadId());//参与组id
            projectTask.setTaskname(task.getTaskname());//任务名称
            projectTask.setSdate(task.getSdate());//任务开始时间
            projectTask.setEdate(task.getEdate());//任务结束时间
            projectTask.setWorkDate(task.getWorkDate());//任务工时

            projectTask.setCreateDate(str);//创建时间
            projectTask.setTaskprogress("0");//任务进度
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

        List<Department> department = departmentService.getDepartment();

        r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, department, null);

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
        if (id == null) {
            group = groupService.getGroupNo();
        } else {
            group = groupService.getGroup(id);
        }


        r = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, group, null);

        return r;

    }

    @ApiOperation(value = "上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> uploadFile(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        ApiResult<Map<String, String>> r = null;
        if (ServletFileUpload.isMultipartContent(request)) {

            try {
                Map<String, Object> result = new HashMap<String, Object>();
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iter = multipartRequest.getFileNames();

                Map<String, String> map = new HashMap<>();
                session = request.getSession(true);
                //遍历list，每迭代一个FileItem对象，调用其isFormField方法判断是否是上传文件
                while (iter.hasNext()) {
                    MultipartFile mfile = multipartRequest.getFile(iter.next().toString());
                    String fileName = mfile.getOriginalFilename();
                    logger.info("原文件名：" + fileName);

                    File ff = new File("E:\\uploadTest\\test\\");

                    //http://192.168.3.26:5026/uploadFile/cashloan/11.png
                    File file = new File(ff + "/" + fileName);

                    map.put(fileName, file.getAbsolutePath());

                    logger.info("上传地址：" + file.getAbsolutePath());
                    String pathFile = file.getAbsolutePath();


                    if (!ff.exists() && !ff.isDirectory()) {
                        logger.info("不存在上传地址新文件夹，将创建");

                        file.mkdir();
                        file.setWritable(true, false);
                    }
                    //file = new File(newPath);

                    mfile.transferTo(file);


                    session.setAttribute("uploadPath", pathFile);

                    Map<String,String> objectMap = new HashMap<>();
                    objectMap.put("uploadPath",pathFile);

                    r = new ApiResult<Map<String, String>>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, objectMap, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                r = new ApiResult<Map<String, String>>(Constant.FAIL_CODE_VALUE, Constant.OPERATION_FAIL, null, null);
            }

        }

        return r;
    }


}