package com.marketing.system.job;

import com.marketing.system.entity.*;
import com.marketing.system.mapper.SystemUserMapper;
import com.marketing.system.mapper_two.DayReportMapper;
import com.marketing.system.mapper_two.OnlineProMapper;
import com.marketing.system.service.UserInfoService;
import com.marketing.system.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.marketing.system.util.WeiXinPushUtil.httpPostWithJSON;


@Component
@EnableScheduling
@PropertySource("classpath:application.properties")
public class TestJob extends BatchProperties.Job {
    private static final Logger logger = Logger.getLogger(TestJob.class);

    @Autowired
    private UserInfoService userInfo;

    @Autowired
    private DayReportMapper DayReportDao;

    @Autowired
    private  OnlineProMapper OnProDao;

    @Value("${prosupervisor.email}")
    private String prosupervisoremail;

    @Value("${ceo.email}")
    private String ceoemail;

    @Resource
    private com.marketing.system.service.DayReportService DayReportService;

    @Autowired
    private SystemUserMapper systemUserMapper;

    /*@Scheduled(cron = "${jobs.schedule}")
    public ApiResult<UserInfo> handleOrderStatus() {
        logger.info("开始执行定时任务");

        *//*UserInfo user = userInfo.selectByPrimaryKey(Long.valueOf("2"));
        ApiResult<UserInfo> result = null;
        result = new ApiResult<UserInfo>(200,"xxxx",user,null);
        logger.info("结束执行定时任务");
        logger.info("结果："+result.getData());
        logger.info("结果2："+user.getLoginName());
        return result;*//*
        return null;
    }*/

    @Scheduled(cron="0 0/30 * * * ?")
    public ApiResult<List<Map>> exportExcelTime() {
        String date= DateUtil.getYMDDate();

        System.out.println("导出"+date+"项目日报");
        //项目日报的定时导出
        Map<String,Object> Report1=DayReportService.exportProExcel(date);
        List<Map> ProReport=(List<Map>)Report1.get("proReports");
        String pathProName ="static//"+date+"ProjectReport.xls";
        try {
            File file=new File(pathProName);
            if(!file.exists()){
                file.createNewFile();
            }

            OutputStream outputStream1= new FileOutputStream(file);
            DayReportExport1.exportExcel(ProReport,outputStream1
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*//保存项目日报相关数据到数据库
        String DATE= DateUtil.getYMDDate();

        String startDate=DATE+"00:00:00";
        String endDate=DATE+"23:59:59";

        Map<String,Object> Report=new HashMap<String ,Object>();

        //项目日报的所有记录
        List<Map> proReports=new ArrayList<Map>();
        List<Map> ProReportAll=DayReportDao.selectProReport(1,1000,startDate,endDate);
        //得到该项目的动态记录
        for(int i=0;i<ProReport.size();i++){
            //项目日报的单条记录
            //Map<String,Object> proReort=new HashMap<String,Object>();
            String proName=(String)ProReportAll.get(i).get("proName");
            String creater=(String)ProReportAll.get(i).get("creater");
            String proState=(String)ProReportAll.get(i).get("proState");
            String proProgres=(String)ProReportAll.get(i).get("proProgress");
            Integer proId=(Integer)ProReportAll.get(i).get("proId");
            List<Map> proLogs= DayReportDao.selectProLogByProId(proId,startDate,endDate);

            String proDyn="";
            for(int j=0;j<proLogs.size();j++){
                proDyn=String.valueOf(j+1)+":"+proLogs.get(j).get("Emp")+" "+proLogs.get(j).get("explain");
            }
            Boolean success=DayReportDao.saveProDayReport(proId,proName,creater,proState,proProgres,proDyn,DATE);
            if(success){}else{
                System.out.println("项目日报数据保存失败");
            }
        }*/




        System.out.println("导出"+date+"任务日报");
        //任务日报的定时导出
        Map<String,Object> Report2=DayReportService.exportTaskExcel(date);
        List<Map> TaskReport=(List<Map>)Report2.get("taskReports");
        String pathTaskName ="static//"+date+"TaskReport.xls";
        try {
            File file=new File(pathTaskName);
            if(!file.exists()){
                file.createNewFile();
            }
            OutputStream outputStream2= new FileOutputStream(file);
            DayReportExport2.exportExcel(TaskReport,outputStream2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("导出"+date+"子任务日报");
        //子任务日报的定时导出
        Map<String,Object> Report3=DayReportService.exportSubtaskExcel(date);
        List<Map> SubtaskReport=(List<Map>)Report3.get("subtaskReports");
        String pathSubtaskName ="static//"+date+"SubtaskReport.xls";
        try {
            File file=new File(pathSubtaskName);
            if(!file.exists()){
                file.createNewFile();
            }
            OutputStream outputStream3= new FileOutputStream(file);
            DayReportExport3.exportExcel(SubtaskReport,outputStream3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Scheduled(cron="0 50 18 * * ?")
    public ApiResult<List<Map>> saveDayReportTime() {

        //-------保存项目日报的相关数据
        String date= DateUtil.getYMDDate();
        //项目日报的定时导出
        Map<String,Object> Report1=DayReportService.exportProExcel(date);
        List<Map> ProReport=(List<Map>)Report1.get("proReports");

        //保存项目日报相关数据到数据库
        String DATE= DateUtil.getYMDDate();
        String startDate=DATE+"00:00:00";
        String endDate=DATE+"23:59:59";

        //项目日报的所有记录
        List<Map> ProReportAll=DayReportDao.selectProReport(1,1000,startDate,endDate);
        //得到该项目的动态记录
        for(int i=0;i<ProReport.size();i++){
            //项目日报的单条记录
            String proName=(String)ProReportAll.get(i).get("proName");
            String creater=(String)ProReportAll.get(i).get("creater");
            String proState=(String)ProReportAll.get(i).get("proState");
            String proProgres=(String)ProReportAll.get(i).get("proProgress");
            Integer proId=(Integer)ProReportAll.get(i).get("proId");
            List<Map> proLogs= DayReportDao.selectProLogByProId(proId,startDate,endDate);

            String proDyn="";
            for(int j=0;j<proLogs.size();j++){
                proDyn=String.valueOf(j+1)+":"+proLogs.get(j).get("Emp")+" "+proLogs.get(j).get("explain");
            }
            Boolean success=DayReportDao.saveProDayReport(proId,proName,creater,proState,proProgres,proDyn,DATE);
            if(success){
                System.out.println("项目日报数据保存成功");
            }else{
                System.out.println("项目日报数据保存失败");
            }
        }

        //------保存任务日报的相关数据

        //任务日报的所有记录
        List<Map> taskReports=new ArrayList<Map>();

        //得到所有需要展示的项目的id
        List<Integer> ProIds=new ArrayList<Integer>();
        List<Map> Pros=null;

        Pros=DayReportDao.selectProReport(1,200,startDate,endDate);

        for(Map<String,Object> Pro:Pros){
            Integer ProId=(Integer)Pro.get("proId");
            ProIds.add(ProId);
        }

        //得到展示项目中的所有任务
        List<Map> TaskReport=null;
        List<Map> TaskReports=new ArrayList<Map>();
        for(Integer ProId:ProIds){
            TaskReport=DayReportDao.selectTaskReport(1,200,ProId);
            TaskReports.addAll(TaskReport);
        }

        //得到该项目的动态记录
        for(int i=0;i<TaskReports.size();i++){
            //项目日报的单条记录
            Map<String,Object> taskReport=new HashMap<String,Object>();
            String taskName=(String)TaskReports.get(i).get("taskName");
            String handler=(String)TaskReports.get(i).get("handler");
            String taskState=(String)TaskReports.get(i).get("taskState");
            String taskProgress=(String)TaskReports.get(i).get("taskProgress");
            Integer taskId=(Integer)TaskReports.get(i).get("taskId");
            Integer proId=(Integer)TaskReports.get(i).get("proId");
            List<Map> taskLogs= DayReportDao.selectTaskLogById(taskId,startDate,endDate);
            String taskDyn="";
            for(int j=0;j<taskLogs.size();j++){
                taskDyn=String.valueOf(j+1)+":"+taskLogs.get(j).get("Emp")+" "+taskLogs.get(j).get("explain");
            }

            Boolean success=DayReportDao.saveTaskDayReport(taskId,taskName,handler,taskState,taskProgress,taskDyn,DATE,proId);
            if(success){
                System.out.println("任务日报数据保存成功");
            }else{
                System.out.println("任务日报数据保存失败");
            }
        }

        //-----保存子任务日报的相关数据

        //子任务日报的所有记录
        List<Map> subtaskReports=new ArrayList<Map>();

        //得到所有展示项目id
        //得到所有需要展示的项目的id
        List<Integer> proIds=new ArrayList<Integer>();
        List<Map> pros=DayReportDao.selectProReport(1,200,startDate,endDate);


        for(Map<String,Object> Pro:pros){
            Integer ProId=(Integer)Pro.get("proId");
            proIds.add(ProId);
        }
        //得到展示项目中的所有任务
        List<Map> TaskReport2=null;
        List<Map> TaskReports2=new ArrayList<Map>();

        for(Integer ProId:proIds){
            TaskReport2=DayReportDao.selectTaskReport(1,200,ProId);
            TaskReports2.addAll(TaskReport2);
        }


        //得到展示任务中的任务id
        List<Map> SubtaskReport=null;
        List<Map> SubtaskReports=new ArrayList<Map>();
        for (Map<String,Object> task:TaskReports2){
            Integer TaskId=(Integer)task.get("taskId");
            SubtaskReport=DayReportDao.selectSubtaskReport(1,200,TaskId);
            SubtaskReports.addAll(SubtaskReport);
        }


        //得到该子任务的动态记录
        for(int i=0;i<SubtaskReports.size();i++){
            //子任务日报的单条记录
            Map<String,Object> subtaskReport=new HashMap<String,Object>();
            String subtaskName=(String)SubtaskReports.get(i).get("subtaskName");
            String subtaskHandler=(String)SubtaskReports.get(i).get("subtaskHandler");
            String subtaskState=(String)SubtaskReports.get(i).get("subtaskState");
            String subtaskProgress=(String)SubtaskReports.get(i).get("subtaskProgress");
            Integer subtaskId=(Integer)SubtaskReports.get(i).get("subtaskId");
            Integer taskId=(Integer)SubtaskReports.get(i).get("taskId");
            Integer proId=DayReportDao.getProIdByTaskId(taskId);
            List<Map> subtaskLogs= DayReportDao.selectSubtaskLogById(subtaskId,startDate,endDate);
            String subtaskDyn="";
            for(int j=0;j<subtaskLogs.size();j++){
                subtaskLogs.get(j).put("idd",j+1);
                subtaskDyn=String.valueOf(j+1)+":"+subtaskLogs.get(j).get("Emp")+" "+subtaskLogs.get(j).get("explain");
            }

            Boolean success=DayReportDao.saveSubtaskDayReport(subtaskId,subtaskName,subtaskHandler,subtaskState,subtaskProgress,subtaskDyn,DATE,proId,taskId);
            if(success){
                System.out.println("子任务日报数据保存成功");
            }else{
                System.out.println("子任务日报数据保存失败");
            }
        }

        return null;
    }

    //任务分配超时
   // @Scheduled(cron="0 0/30 * * * ?")
   /* public ApiResult<List<Map>> taskTimeOutWeiXinPush() {

        System.out.println("-------------------");
        List<ProjectTask> tasks=DayReportDao.getTaskByWXTimeOutPush();
        for(ProjectTask task : tasks){

            //计算任务开始时间跟当前时间的时间差有没有超过12小时
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String fromDate=task.getSdate().substring(0,16);
            String toDate =DateUtil.getYMDHMDate();
            long from = 0;
            long to=0;
            try {
                from = simpleFormat.parse(fromDate).getTime();
                to = simpleFormat.parse(toDate).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int hours = (int) ((to - from)/(1000 * 60 * 60));

            if(hours >= 12){
                Integer taskId=task.getTaskId();
                Integer groupId=Integer.parseInt(task.getSquadId());
                List<ProjectSubtask> subtasks=DayReportDao.getSubtaskByWXTimeOutPush(taskId);
                String postUrl1 = "";
                String postUrl2 = "";
                if(subtasks.size()==0){
                    //该任务下面未分配子任务
                    String PushDate = DateUtil.getYMDHMDate();
                    Integer managerId=DayReportDao.getManagerIdByGroupId(groupId);
                    String proName=DayReportDao.getProNameByTaskId(taskId);
                    //推送给部门经理
                    *//*postUrl1 = "{\"Uid\":" + managerId + ",\"Content\":\"《" +proName+ "》需"+task.getHandler()+"协助实施"+task.getTaskname()+"工作，现已超过12小时未处理，请督促处理。"
                            + "\\n\\n任务分配:" + task.getHandler()
                            + "\\n\\n任务名称:" + task.getTaskname()
                            + "\\n\\n开始时间:" + task.getSdate()
                            + "\\n\\n结束时间:" + task.getEdate()
                            + "\\n\\n推送时间:" + PushDate
                            + "\",\"AgentId\":1000011,\"Title\":\"延迟预警\",\"Url\":\"\"}";*//*

                    //推送给郑洁
                    postUrl2 = "{\"Uid\":" + 1367 + ",\"Content\":\"《" +proName+ "》需"+task.getHandler()+"协助实施"+task.getTaskname()+"工作，现已超过12小时未处理，请督促处理。"
                            + "\\n\\n任务分配:" + task.getHandler()
                            + "\\n\\n任务名称:" + task.getTaskname()
                            + "\\n\\n开始时间:" + task.getSdate()
                            + "\\n\\n结束时间:" + task.getEdate()
                            + "\\n\\n推送时间:" + PushDate
                            + "\",\"AgentId\":1000011,\"Title\":\"延迟预警\",\"Url\":\"\"}";
                }
                try {
                    //消息推送-延迟预警
                    String Str = httpPostWithJSON(postUrl1);
                    System.out.println(Str);
                    httpPostWithJSON(postUrl2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
      return null;
    }*/

    //下午4点定时提醒更新子任务开发日志
    @Scheduled(cron="0 05 18 * * ?")
    public ApiResult<List<Map>> subTaskWXPush() {
    System.out.println("=============");
        //获取当前时间
        String nowDay = DateUtil.getYMDDate();
        //获取所有未完成，驳回，逾期项目里的任务
        List<ProjectTask> tasks=DayReportDao.getTaskByWXTimeOutPush();
        for(ProjectTask task:tasks){
            String proName = OnProDao.getProNameByTaskId(task.getTaskId());
            //获取任务中对应的所有子任务
            List<ProjectSubtask> subtasks= OnProDao.getSubtaskByTaskId(task.getTaskId());
            for(ProjectSubtask subtask:subtasks){
                String postUrl1 = "";
                String postUrl2 = "";
                //判断子任务状态是否是未开始，开发中，驳回状态
                if("1".equals(subtask.getSubtaskstate())||"2".equals(subtask.getSubtaskstate())||"5".equals(subtask.getSubtaskstate())){
                      //判断子任务开始时间和当前时间的时间差
                      if(DateUtil.getDayDiff(subtask.getSdate(),nowDay) <= 0){
                          String subtaskHandler = subtask.getSubtaskhandler();
                          Integer Uid = systemUserMapper.getUidByName(subtaskHandler);
                          Integer noPutCount = subtask.getNoPutCount();
                          if(noPutCount == null){
                              noPutCount = 0 ;
                          }
                          //微信提示推送给组员
                          String PushDate = DateUtil.getYMDHMDate();
                          postUrl1 = "{\"Uid\":" + Uid + ",\"Content\":\"【开发日志】\\n\\n《" +proName+ "》需您协助实施"+subtask.getSubtaskname()+"工作，请及时填写开发日志。"
                                  + "\\n\\n任务分配:" + task.getHandler()
                                  + "\\n\\n任务名称:" + subtask.getSubtaskname()
                                  + "\\n\\n开始时间:" + subtask.getSdate()
                                  + "\\n\\n结束时间:" + subtask.getEdate()
                                  + "\\n\\n未按时填写次数:" + noPutCount
                                  + "\\n\\n推送时间:" + PushDate
                                  + "\",\"AgentId\":1000011,\"Title\":\"开发日志\",\"Url\":\"\"}";
                          /*postUrl2 = "{\"Uid\":" + 1340 + ",\"Content\":\"【开发日志】\\n\\n《" +proName+ "》需您协助实施"+subtask.getSubtaskname()+"工作，请及时填写开发日志。"
                                  + "\\n\\n任务分配:" + task.getHandler()
                                  + "\\n\\n任务名称:" + subtask.getSubtaskname()
                                  + "\\n\\n开始时间:" + subtask.getSdate()
                                  + "\\n\\n结束时间:" + subtask.getEdate()
                                  + "\\n\\n未按时填写次数:" + noPutCount
                                  + "\\n\\n推送时间:" + PushDate
                                  + "\",\"AgentId\":1000011,\"Title\":\"开发日志\",\"Url\":\"\"}";*/
                      }
                }
                try {
                    //消息推送-开发日志
                    String Str1 = httpPostWithJSON(postUrl1);
                    String Str2 = httpPostWithJSON(postUrl2);
                    logger.error("微信推送下午4点定时更新子任务开发日志--提醒成功！");
                } catch (Exception e) {
                    logger.error("微信推送下午4点定时更新子任务开发日志--提醒失败！"+ e);
                    e.printStackTrace();
                }
           }
        }
        return null;
    }

    //下午4点半判断是否更新日志，并推送
    @Scheduled(cron="0 06 18 * * ?")
    public ApiResult<List<Map>> subTaskWXPush1() {
        System.out.println("-=-=-=-=-=-=-");
        //获取当前时间
        String todayDate = DateUtil.getYMDDate();
        //获取所有未完成，驳回，逾期项目里的任务
        List<ProjectTask> tasks=DayReportDao.getTaskByWXTimeOutPush();
        for(ProjectTask task:tasks){
            String proName = OnProDao.getProNameByTaskId(task.getTaskId());
            //获取任务中对应的所有子任务
            List<ProjectSubtask> subtasks= OnProDao.getSubtaskByTaskId(task.getTaskId());
            for(ProjectSubtask subtask:subtasks){
                Boolean flag=false;
                String postUrl = "";
                //判断子任务状态是否是未开始，开发中，驳回状态
                if("1".equals(subtask.getSubtaskstate())||"2".equals(subtask.getSubtaskstate())||"5".equals(subtask.getSubtaskstate())){
                    String postUrl1 = "";
                    String postUrl2 = "";
                    String postUrl3 = "";
                    //判断子任务开始时间和当前时间的时间差
                    if(DateUtil.getDayDiff(subtask.getSdate(),todayDate) <= 0) {
                        //获取到该子任务中所有的开发日志记录
                        List<SubtaskDevelopLog> subtaskDevelopLogs = OnProDao.getAllSubTaskDevLog(subtask.getSubtaskId());
                        for (SubtaskDevelopLog subtaskDevelopLog : subtaskDevelopLogs) {
                            if (todayDate.equals(subtaskDevelopLog.getDate().substring(0, 10))) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag == false) {
                            //修改子任务中未按时更新开发日志记录次数
                            Integer NoPutCount = subtask.getNoPutCount();
                            if (NoPutCount == null) {
                                NoPutCount = 0;
                            }
                            NoPutCount++;
                            boolean success = OnProDao.updateNoPutCount(NoPutCount, subtask.getSubtaskId());
                            if (!success) {
                                logger.info("修改noPutCount失败");
                            }
                            //推送微信延迟预警
                            //获得组长id
                            Integer Uid = systemUserMapper.getUidByName(task.getHandler());
                            //获取当前时间
                            String PushDate = DateUtil.getYMDHMDate();
                            //获取项目
                            String proCreater = systemUserMapper.getCreaterByTaskId(task.getTaskId());
                            Integer createrid = systemUserMapper.getUidByName(proCreater);

                            postUrl1 = "{\"Uid\":" + Uid + ",\"Content\":\"【延迟预警1级】\\n\\n《" + proName + "》需" + subtask.getSubtaskhandler() + "协助实施" + subtask.getSubtaskname() + "工作，现已超过半小时未处理，请督促处理。"
                                    + "\\n\\n任务分配:" + task.getHandler()
                                    + "\\n\\n任务名称:" + subtask.getSubtaskname()
                                    + "\\n\\n开始时间:" + subtask.getSdate()
                                    + "\\n\\n结束时间:" + subtask.getEdate()
                                    + "\\n\\n未按时填写次数:" + NoPutCount
                                    + "\\n\\n推送时间:" + PushDate
                                    + "\",\"AgentId\":1000011,\"Title\":\"开发日志\",\"Url\":\"\"}";

                            //推送给郑洁
                            postUrl2 = "{\"Uid\":" + 1340+ ",\"Content\":\"【延迟预警1级】\\n\\n《" + proName + "》需" + subtask.getSubtaskhandler() + "协助实施" + subtask.getSubtaskname() + "工作，现已超过半小时未处理，请督促处理。"
                                    + "\\n\\n任务分配:" + task.getHandler()
                                    + "\\n\\n任务名称:" + subtask.getSubtaskname()
                                    + "\\n\\n开始时间:" + subtask.getSdate()
                                    + "\\n\\n结束时间:" + subtask.getEdate()
                                    + "\\n\\n未按时填写次数:" + NoPutCount
                                    + "\\n\\n推送时间:" + PushDate
                                    + "\",\"AgentId\":1000011,\"Title\":\"开发日志\",\"Url\":\"\"}";

                            //推送给项目创建人
                            postUrl3 = "{\"Uid\":" + createrid + ",\"Content\":\"【延迟预警1级】\\n\\n《" + proName + "》需" + subtask.getSubtaskhandler() + "协助实施" + subtask.getSubtaskname() + "工作，现已超过半小时未处理，请督促处理。"
                                    + "\\n\\n任务分配:" + task.getHandler()
                                    + "\\n\\n任务名称:" + subtask.getSubtaskname()
                                    + "\\n\\n开始时间:" + subtask.getSdate()
                                    + "\\n\\n结束时间:" + subtask.getEdate()
                                    + "\\n\\n未按时填写次数:" + NoPutCount
                                    + "\\n\\n推送时间:" + PushDate
                                    + "\",\"AgentId\":1000011,\"Title\":\"开发日志\",\"Url\":\"\"}";

                        }
                    }

                    try {
                        //消息推送-开发日志
                        httpPostWithJSON(postUrl1);
                        httpPostWithJSON(postUrl2);
                        httpPostWithJSON(postUrl3);
                        logger.error("微信推送下午4点半判断是否更新日志--提醒成功！");
                    } catch (Exception e) {
                        logger.error("微信推送下午4点半判断是否更新日志--提醒失败！"+e);
                        e.printStackTrace();
                    }
                }

            }
        }
        return null;
    }

    //下午5点判断是否更新日志，并推送
    @Scheduled(cron="0 08 18 * * ?")
    public ApiResult<List<Map>> subTaskWXPush2() {

        //获取当前时间
        String todayDate = DateUtil.getYMDDate();
        //获取所有未完成，驳回，逾期项目里的任务
        List<ProjectTask> tasks=DayReportDao.getTaskByWXTimeOutPush();
        for(ProjectTask task:tasks){
            String proName = OnProDao.getProNameByTaskId(task.getTaskId());
            //获取任务中对应的所有子任务
            List<ProjectSubtask> subtasks= OnProDao.getSubtaskByTaskId(task.getTaskId());
            for(ProjectSubtask subtask:subtasks){
                Boolean flag=false;
                //判断子任务状态是否是未开始，开发中，驳回状态
                if("1".equals(subtask.getSubtaskstate())||"2".equals(subtask.getSubtaskstate())||"5".equals(subtask.getSubtaskstate())){
                    String postUrl1 = "";
                    String postUrl2 = "";
                    //判断子任务开始时间和当前时间的时间差
                    if(DateUtil.getDayDiff(subtask.getSdate(),todayDate) <= 0) {
                        //获取到该子任务中所有的开发日志记录
                        List<SubtaskDevelopLog> subtaskDevelopLogs = OnProDao.getAllSubTaskDevLog(subtask.getSubtaskId());
                        for(SubtaskDevelopLog subtaskDevelopLog : subtaskDevelopLogs){
                            if(todayDate.equals(subtaskDevelopLog.getDate().substring(0,10))){
                                flag = true;
                                break;
                            }
                        }

                        if(flag == false){
                            //推送微信延迟预警
                            /*//获得部门经理id
                            Integer managerId = DayReportDao.getManagerIdByGroupId(Integer.parseInt(task.getSquadId()));*/
                            //获取组长id
                            String Uname = task.getHandler();
                            Integer Uid = systemUserMapper.getUidByName(Uname);
                            //获取当前时间
                            String PushDate = DateUtil.getYMDHMDate();
                            Integer NoPutCount = subtask.getNoPutCount();
                            if(NoPutCount == null){
                                NoPutCount = 0;
                            }
                            postUrl1 = "{\"Uid\":" + Uid + ",\"Content\":\"【延迟预警2级】\\n\\n《" +proName+ "》需"+subtask.getSubtaskhandler()+"协助实施"+subtask.getSubtaskname()+"工作，现已超过一小时未处理，请督促处理。"
                                    + "\\n\\n任务分配:" + task.getHandler()
                                    + "\\n\\n任务名称:" + subtask.getSubtaskname()
                                    + "\\n\\n开始时间:" + subtask.getSdate()
                                    + "\\n\\n结束时间:" + subtask.getEdate()
                                    + "\\n\\n未按时填写次数:" + NoPutCount
                                    + "\\n\\n推送时间:" + PushDate
                                    + "\",\"AgentId\":1000011,\"Title\":\"开发日志\",\"Url\":\"\"}";

                            postUrl2 = "{\"Uid\":" +  1340+ ",\"Content\":\"【延迟预警2级】\\n\\n《" +proName+ "》需"+subtask.getSubtaskhandler()+"协助实施"+subtask.getSubtaskname()+"工作，现已超过一小时未处理，请督促处理。"
                                    + "\\n\\n任务分配:" + task.getHandler()
                                    + "\\n\\n任务名称:" + subtask.getSubtaskname()
                                    + "\\n\\n开始时间:" + subtask.getSdate()
                                    + "\\n\\n结束时间:" + subtask.getEdate()
                                    + "\\n\\n未按时填写次数:" + NoPutCount
                                    + "\\n\\n推送时间:" + PushDate
                                    + "\",\"AgentId\":1000011,\"Title\":\"开发日志\",\"Url\":\"\"}";
                        }
                    }

                    try {
                        //消息推送-开发日志
                        httpPostWithJSON(postUrl1);
                        httpPostWithJSON(postUrl2);
                        logger.error("下午5点判断是否更新日志--提醒成功！");
                    } catch (Exception e) {
                        logger.error("下午5点判断是否更新日志--提醒失败！"+ e);
                        e.printStackTrace();
                    }

                }

            }
        }
        return null;
    }

    //项目实施进度通报
    @Scheduled(cron="0 09 18 * * ?")
    public ApiResult<List<Map>> progresseport() {

        System.out.println("===---===---==");
        //获取所有未完成，驳回，逾期项目
        List<ProjectInfo> pros = OnProDao.getAllNoOnlinePro();
        for(ProjectInfo pro : pros){
            String proName = pro.getProname();
            String creater = pro.getCreater();
            String proProgress = pro.getProprogress();
            String proSDate = pro.getCreatedate().substring(0,10);
            String proEDate = pro.getPlansdate().substring(0,10);
            String todayProDate = DateUtil.getYMDDate();
            String proSpeed ="";
            //获取天数差
            Integer allProDay = DateUtil.getDayDiff(proSDate,proEDate);
            if(allProDay == 0){
                allProDay = 1;
            }
            Integer proDay = DateUtil.getDayDiff(proSDate,todayProDate);
            if(proDay == 0){
                proDay = 1;
            }
            if(Integer.parseInt(proProgress) == 0){
                proSpeed = "该项目未开始";
            }else if(Integer.parseInt(proProgress) == 100){
                proSpeed = "该项目已全部完成";
            }else if((proDay/allProDay * 100) >= Integer.parseInt(proProgress) && (proDay/allProDay * 100) <= 100){
                proSpeed = "此进度比时间进度快";
            }else if((proDay/allProDay * 100) >= Integer.parseInt(proProgress) && (proDay/allProDay * 100) > 100){
                proSpeed = "此任务已逾期";
            }else if((proDay/allProDay * 100) < Integer.parseInt(proProgress) && Integer.parseInt(proProgress) != 0 && Integer.parseInt(proProgress) != 100){
                proSpeed = "此进度比时间进度慢";
            }

            //根据项目Id获得项目任务
            List<ProjectTask> tasks = OnProDao.getTaskByProId(pro.getProid());
            StringBuffer str = new StringBuffer();
            str.append(""+proName+"，项目责任人"+creater+"，累计完成"+proProgress+"%，"+proSpeed+"，各项子任务未更新情况如下：<br>");
            Integer idd = 0;
            for(ProjectTask task : tasks){
                idd++;
                String taskName = task.getTaskname();
                String handker = task.getHandler();
                String taskProgress = task.getTaskprogress();
                String taskSDate = task.getSdate();
                String taskEDate = task.getEdate();
                String todayTaskDate = DateUtil.getYMDDate();

                String taskSpeed ="";
                //获取天数差
                Integer allTaskDay = DateUtil.getDayDiff(taskSDate,taskEDate);
                if(allTaskDay == 0){
                    allTaskDay =1;
                }
                Integer taskDay = DateUtil.getDayDiff(taskSDate,todayTaskDate);
                if(taskDay == 0){
                    taskDay =1;
                }
                if(Integer.parseInt(taskProgress) == 0) {
                    taskSpeed = "该任务未开始";
                }else if(Integer.parseInt(taskProgress) == 100){
                    taskSpeed = "该任务已全部完成";
                }else if((taskDay/allTaskDay * 100) >= Integer.parseInt(task.getTaskprogress()) && (taskDay/allTaskDay * 100) <100){
                    taskSpeed = "此进度比时间进度快";
                }else if((taskDay/allTaskDay * 100) >= Integer.parseInt(task.getTaskprogress()) && (taskDay/allTaskDay * 100) >100){
                    taskSpeed = "此任务已逾期";
                }else if((taskDay/allTaskDay * 100) < Integer.parseInt(task.getTaskprogress())){
                    taskSpeed = "此进度比时间进度慢";
                }

                str.append("（"+idd+"）"+taskName+"，任务责任人"+handker+"，累计完成"+taskProgress+"%，"+taskSpeed+"；<br>");


            }
            //发送邮件
            String nowDate = DateUtil.getMDDate();
            String report = str.toString();
            //根据用户名查出用户邮箱
           String email = systemUserMapper.getEmailByName(creater);
              //获取
            try {
                //发送邮件给项目监管人
                String success1 = ToolUtil.sendEmial(prosupervisoremail,"关于《"+proName+"》今日进展情况的日报"+todayProDate+"","您好:<br>   截至"+nowDate+"，"+proName+"实施情况如下，请及时督促项目实施人员按时、按量完成具体工作。<br>"
                        +"通报表格见下方<br>"
                        +report);
                //发送邮件给项目负责人
                String success2 = ToolUtil.sendEmial(email,"关于《"+proName+"》今日进展情况的日报"+todayProDate+"","您好:<br>   截至"+nowDate+"，"+proName+"实施情况如下，请及时督促项目实施人员按时、按量完成具体工作。<br>"
                        +"通报表格见下方<br>"
                        +report);
                //推送邮件到运营总监
                ToolUtil.sendEmial(ceoemail,"关于《"+proName+"》今日进展情况的日报"+todayProDate+"","您好:<br>   截至"+nowDate+"，"+proName+"实施情况如下，请及时督促项目实施人员按时、按量完成具体工作。<br>"
                        +"通报表格见下方<br>"
                        +report);
                logger.error("项目实施进度邮件通报--通报成功！");

                System.out.println(success1);
                System.out.println(success2);
            } catch (IOException e) {
                logger.error("项目实施进度邮件通报--通报失败！"+e);
                e.printStackTrace();
            }
        }


        return null;
    }

    //未实施更新通报
    @Scheduled(cron="0 09 18 * * ?")
    public ApiResult<List<Map>> noPutCountReport() {

        String todayDate = DateUtil.getYMDDate();
        //获取所有未完成，驳回，逾期项目
        List<ProjectInfo> pros = OnProDao.getAllNoOnlinePro();
        for(ProjectInfo pro : pros){
            StringBuffer str = new StringBuffer();

             List<ProjectTask> tasks = OnProDao.getTaskByProId(pro.getProid());
             Integer idd = 0;

             for(ProjectTask task : tasks){
                 List<ProjectSubtask> subtasks = OnProDao.getSubtaskByTaskId(task.getTaskId());
                 Boolean flag=false;

                 for(ProjectSubtask subtask : subtasks){
                     if("1".equals(subtask.getSubtaskstate())||"2".equals(subtask.getSubtaskstate())||"5".equals(subtask.getSubtaskstate())) {
                         //获取到该子任务中所有的开发日志记录
                         //判断子任务开始时间和当前时间的时间差
                         if(DateUtil.getDayDiff(subtask.getSdate(),todayDate) <= 0) {
                             List<SubtaskDevelopLog> subtaskDevelopLogs = OnProDao.getAllSubTaskDevLog(subtask.getSubtaskId());
                             for (SubtaskDevelopLog subtaskDevelopLog : subtaskDevelopLogs) {
                                 if (todayDate.equals(subtaskDevelopLog.getDate().substring(0, 10))) {
                                     flag = true;
                                     break;
                                 }
                             }
                         }

                     }
                     if(flag==false){
                         idd++;
                         if(idd == 1){
                             str.append(""+pro.getProname()+"，项目责任人"+pro.getCreater()+"，各项子任务未更新情况如下：<br>");
                         }
                         Integer NoPutCount = subtask.getNoPutCount();
                         if(NoPutCount == null){
                             NoPutCount = 0;
                         }
                         str.append("（"+idd+"）"+subtask.getSubtaskname()+"，任务责任人"+subtask.getSubtaskhandler()+"，累计未更新次数为"+NoPutCount+"次；<br>");
                     }
                 }
             }
            //发送邮件
            String nowDate = DateUtil.getMDDate();
            String report = str.toString();
            //根据用户名查出用户邮箱
            String email = systemUserMapper.getEmailByName(pro.getCreater());
            //获取
            if(idd > 0){
                try {
                    //推送邮件到项目监管人
                    ToolUtil.sendEmial(prosupervisoremail,"关于《"+pro.getProname()+"》中未按时填写开发日志的通报"+nowDate+"","您好:<br>    截至"+nowDate+"，"+pro.getProname()+"中未按时填写开发日志的情况如下，请及时督促项目实施人员按时、按量完成具体工作。<br>"
                            +"通报表格见下方<br>"
                            +report);
                    //推送邮件到运营总监
                    ToolUtil.sendEmial(ceoemail,"关于《"+pro.getProname()+"》中未按时填写开发日志的通报"+nowDate+"","您好:<br>    截至"+nowDate+"，"+pro.getProname()+"中未按时填写开发日志的情况如下，请及时督促项目实施人员按时、按量完成具体工作。<br>"
                            +"通报表格见下方<br>"
                            +report);
                    //推送邮件给项目责任人
                    ToolUtil.sendEmial(email,"关于《"+pro.getProname()+"》中未按时填写开发日志的通报"+nowDate+"","您好:<br>    截至"+nowDate+"，"+pro.getProname()+"中未按时填写开发日志的情况如下，请及时督促项目实施人员按时、按量完成具体工作。<br>"
                            +"通报表格见下方<br>"
                            +report);
                    logger.error("项目未实施更新邮件通报--通报成功！");

                } catch (IOException e) {
                    logger.error("项目未实施更新邮件通报--通报失败！"+e);
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

}
