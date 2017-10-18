package com.marketing.system.service.impl;

import com.marketing.system.entity.ProDevelopLog;
import com.marketing.system.mapper_two.DayReportMapper;
import com.marketing.system.service.DayReportService;
import com.marketing.system.util.DateUtil;
import com.marketing.system.util.ToolUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DayReportServiceImpl implements DayReportService{

    @Resource
    private DayReportMapper DayReportDao;

    //模糊查询项目日报
    public Map<String,Object> selectProReport(Integer current, Integer pageSize, String Date){

        String date=null;
        if(Date==null||Date==""){
           date= DateUtil.getYMDDate();
        }
        String startDate=date+"00:00:00";
        String endDate=date+"23:59:59";

        Map<String,Object> Report=new HashMap<String ,Object>();

        //项目日报的所有记录
        List<Map> proReports=new ArrayList<Map>();
        List<Map> ProReport=DayReportDao.selectProReport(current,pageSize,startDate,endDate);
        //得到该项目的动态记录
        for(int i=0;i<ProReport.size();i++){
            //项目日报的单条记录
            Map<String,Object> proReort=new HashMap<String,Object>();
            String proName=(String)ProReport.get(i).get("proName");
            String creater=(String)ProReport.get(i).get("creater");
            String proState=(String)ProReport.get(i).get("proState");
            String proProgres=(String)ProReport.get(i).get("proProgress");
            Integer proId=(Integer)ProReport.get(i).get("proId");
            List<Map> proLogs= DayReportDao.selectProLogByProId(proId,startDate,endDate);
            for(int j=0;j<proLogs.size();j++){
                proLogs.get(j).put("idd",j+1);
            }
            proReort.put("proName",proName);
            proReort.put("creater",creater);
            proReort.put("proState",proState);
            proReort.put("proProgres",proProgres);
            proReort.put("proLogs",proLogs);
            proReports.add(proReort);
        }

        Integer proReportsNum=DayReportDao.selectProReportNum(startDate,endDate);
        Report.put("proReports",proReports);
        Report.put("proReportsNum",proReportsNum);
        return  Report;
    }

    //模糊查询任务日报
    public Map<String,Object> selectTaskReport(Integer current, Integer pageSize, String Date){



        String date=null;
        if(Date==null||Date==""){
           date= DateUtil.getYMDDate();
        }
        String startDate=date+"00:00:00";
        String endDate=date+"23:59:59";

        Map<String,Object> Report=new HashMap<String ,Object>();

        //任务日报的所有记录
        List<Map> taskReports=new ArrayList<Map>();

        //得到所有需要展示的项目的id
        List<Integer> proIds=new ArrayList<Integer>();
        List<Map> Pros=DayReportDao.selectProReport(1,200,startDate,endDate);
        for(Map<String,Object> Pro:Pros){
            Integer proId=(Integer)Pro.get("proId");
            proIds.add(proId);
        }

        //得到展示项目中的所有任务
        List<Map> TaskReport=null;
        List<Map> TaskReports=new ArrayList<Map>();
        for(Integer proId:proIds){
            TaskReport=DayReportDao.selectTaskReport(1,200,proId);
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
            List<Map> taskLogs= DayReportDao.selectTaskLogById(taskId,startDate,endDate);
            for(int j=0;j<taskLogs.size();j++){
                taskLogs.get(j).put("idd",j+1);
            }
            taskReport.put("taskName",taskName);
            taskReport.put("handler",handler);
            taskReport.put("taskState",taskState);
            taskReport.put("taskProgress",taskProgress);
            taskReport.put("taskLogs",taskLogs);
            System.out.println("taskLogs----------------"+taskLogs);
            taskReports.add(taskReport);
        }
        List<Map> taskReport=ToolUtil.listSplit(current,pageSize,taskReports);

       /* Integer taskReportsNum=null;
        for(Integer proId:proIds){
            taskReportsNum=DayReportDao.selectTaskReportNum(proId);
        }*/
        Report.put("taskReports",taskReport);
        Report.put("taskReportsNum",taskReports.size());
        return  Report;
    }

    //模糊查询子任务日报
    public Map<String,Object> selectSubtaskReport(Integer current, Integer pageSize, String Date){

        String date=null;
        if(Date==null||Date==""){
         date= DateUtil.getYMDDate();
        }
        String startDate=date+"00:00:00";
        String endDate=date+"23:59:59";

        Map<String,Object> Report=new HashMap<String ,Object>();

        //子任务日报的所有记录
        List<Map> subtaskReports=new ArrayList<Map>();

        //得到所有展示项目id
        //得到所有需要展示的项目的id
        List<Integer> proIds=new ArrayList<Integer>();
        List<Map> Pros=DayReportDao.selectProReport(1,200,startDate,endDate);
        for(Map<String,Object> Pro:Pros){
            Integer proId=(Integer)Pro.get("proId");
            proIds.add(proId);
        }
        //得到展示项目中的所有任务
        List<Map> TaskReport=null;
        List<Map> TaskReports=new ArrayList<Map>();
        for(Integer proId:proIds){
            TaskReport=DayReportDao.selectTaskReport(1,200,proId);
            TaskReports.addAll(TaskReport);
        }
        //得到展示任务中的任务id
        List<Map> SubtaskReport=null;
        List<Map> SubtaskReports=new ArrayList<Map>();
        for (Map<String,Object> task:TaskReports){
            Integer taskId=(Integer)task.get("taskId");
            SubtaskReport=DayReportDao.selectSubtaskReport(1,200,taskId);
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
            List<Map> subtaskLogs= DayReportDao.selectSubtaskLogById(subtaskId,startDate,endDate);
            for(int j=0;j<subtaskLogs.size();j++){
                subtaskLogs.get(j).put("idd",j+1);
            }
            subtaskReport.put("subtaskName",subtaskName);
            subtaskReport.put("subtaskHandler",subtaskHandler);
            subtaskReport.put("subtaskState",subtaskState);
            subtaskReport.put("subtaskProgress",subtaskProgress);
            subtaskReport.put("subtaskLogs",subtaskLogs);
            subtaskReports.add(subtaskReport);
        }
        List<Map> subtaskReport=ToolUtil.listSplit(current,pageSize,subtaskReports);
        Report.put("subtaskReports",subtaskReport);
        Report.put("subtaskReportsNum",subtaskReport.size());
        return  Report;
    }

    //项目日报的定时导出
    public Map<String,Object> exportProExcel( String date){
        String startDate=date+"00:00:00";
        String endDate=date+"23:59:59";

        Map<String,Object> Report=new HashMap<String ,Object>();
        //项目日报的所有记录
        List<Map> proReports=new ArrayList<Map>();
        List<Map> ProReport=DayReportDao.exportProExcel(startDate,endDate);
        //得到该项目的动态记录
        for(int i=0;i<ProReport.size();i++){
            //项目日报的单条记录
            Map<String,Object> proReport=new HashMap<String,Object>();
            String proName=(String)ProReport.get(i).get("proName");
            String creater=(String)ProReport.get(i).get("creater");
            String proState=(String)ProReport.get(i).get("proState");
            String proProgres=(String)ProReport.get(i).get("proProgress");
            Integer proId=(Integer)ProReport.get(i).get("proId");
            if(proState.equals("1")) {
                proReport.put("proState", "立项待审批");
            }else if(proState.equals("2")){
                proReport.put("proState", "开发中");
            }else if(proState.equals("3")){
                proReport.put("proState", "上线带审批");
            }else if(proState.equals("4")){
                proReport.put("proState", "立项待完成审批");
            }else if(proState.equals("5")){
                proReport.put("proState", "驳回");
            }else if(proState.equals("6")){
                proReport.put("proState", "作废");
            }else if(proState.equals("7")){
                proReport.put("proState", "逾期");
            }

            List<Map> proLogs= DayReportDao.selectProLogByProId(proId,startDate,endDate);
            for(int j=0;j<proLogs.size();j++){
                proLogs.get(j).put("idd",j+1);
            }
            StringBuffer str=new StringBuffer();
            for(Map<String,Object> prolog:proLogs){
                Integer idd=(Integer)prolog.get("idd");
                String Emp=(String)prolog.get("Emp");
                String explain=(String)prolog.get("explain");
                str.append(idd).append('.').append(Emp).append(explain).append(" ");
            }
            String prologs=str+"";
            proReport.put("proName",proName);
            proReport.put("creater",creater);
            proReport.put("proProgres",proProgres);
            proReport.put("proLogs",prologs);
            proReports.add(proReport);
        }
        Report.put("proReports",proReports);
        return  Report;
    }


    ///任务日报的定时导出
    public Map<String,Object> exportTaskExcel( String date){

        String startDate=date+"00:00:00";
        String endDate=date+"23:59:59";
        Map<String,Object> Report=new HashMap<String ,Object>();

        //得到所有需要展示的项目的id
        List<Integer> proIds=new ArrayList<Integer>();
        List<Map> Pros=DayReportDao.exportProExcel(startDate,endDate);
        for(Map<String,Object> Pro:Pros){
            Integer proId=(Integer)Pro.get("proId");
            proIds.add(proId);
        }
        //得到展示项目中的所有任务
        List<Map> TaskReport=null;
        List<Map> TaskReports=new ArrayList<Map>();
        for(Integer proId:proIds){
            TaskReport=DayReportDao.exportTaskExcel(proId);
            TaskReports.addAll(TaskReport);
        }

        //任务日报的所有记录
        List<Map> taskReports=new ArrayList<Map>();

        //得到该任务的动态记录
        for(int i=0;i<TaskReports.size();i++){
            //任务日报的单条记录
            Map<String,Object> taskReport=new HashMap<String,Object>();
            String taskName=(String)TaskReports.get(i).get("taskName");
            String handler=(String)TaskReports.get(i).get("handler");
            String taskState=(String)TaskReports.get(i).get("taskState");
            if(taskState.equals("1")) {
                taskReport.put("taskState", "未开始");
            }else if(taskState.equals("2")){
                taskReport.put("taskState", "开发中");
            }else if(taskState.equals("3")){
                taskReport.put("taskState", "预验收");
            }else if(taskState.equals("4")){
                taskReport.put("taskState", "已完成");
            }else if(taskState.equals("5")){
                taskReport.put("taskState", "逾期");
            }

            String taskProgress=(String)TaskReports.get(i).get("taskProgress");
            Integer taskId=(Integer)TaskReports.get(i).get("taskId");
            List<Map> taskLogs= DayReportDao.selectTaskLogById(taskId,startDate,endDate);
            for(int j=0;j<taskLogs.size();j++){
                taskLogs.get(j).put("idd",j+1);
            }
            StringBuffer str=new StringBuffer();
            for(Map<String,Object> tasklog:taskLogs){
                Integer idd=(Integer)tasklog.get("idd");
                String Emp=(String)tasklog.get("Emp");
                String explain=(String)tasklog.get("explain");
                str.append(idd).append('.').append(Emp).append(explain).append(" ");
            }
            String tasklogs=str+"";
            taskReport.put("taskName",taskName);
            taskReport.put("handler",handler);
            taskReport.put("taskProgress",taskProgress);
            taskReport.put("taskLogs",tasklogs);
            taskReports.add(taskReport);
        }
        Report.put("taskReports",taskReports);

        return Report;
    }

    //子任务日报的定时导出
    public Map<String,Object> exportSubtaskExcel( String date){

        String startDate=date+"00:00:00";
        String endDate=date+"23:59:59";
        Map<String,Object> Report=new HashMap<String ,Object>();

        /*子任务日报的所有记录*/
        List<Map> subtaskReports=new ArrayList<Map>();
        //得到所有展示项目id
        //得到所有需要展示的项目的id
        List<Integer> proIds=new ArrayList<Integer>();
        List<Map> Pros=DayReportDao.exportProExcel(startDate,endDate);
        for(Map<String,Object> Pro:Pros){
            Integer proId=(Integer)Pro.get("proId");
            proIds.add(proId);
        }
        //得到展示项目中的所有任务
        List<Map> TaskReport=null;
        List<Map> TaskReports=new ArrayList<Map>();
        for(Integer proId:proIds){
            TaskReport=DayReportDao.exportTaskExcel(proId);
            TaskReports.addAll(TaskReport);
        }
        //得到展示任务中的任务id
        List<Map> SubtaskReport=null;
        List<Map> SubtaskReports=new ArrayList<Map>();
        for (Map<String,Object> task:TaskReports){
            Integer taskId=(Integer)task.get("taskId");
            SubtaskReport=DayReportDao.exportSubtaskExcel(taskId);
            SubtaskReports.addAll(SubtaskReport);
        }
       // List<Map> SubtaskReport=DayReportDao.exportSubtaskExcel(startDate,endDate);

        //得到该任务的动态记录
        for(int i=0;i<SubtaskReports.size();i++){
            //任务日报的单条记录
            Map<String,Object> subtaskReport=new HashMap<String,Object>();
            String subtaskName=(String)SubtaskReports.get(i).get("subtaskName");
            String subtaskHandler=(String)SubtaskReports.get(i).get("subtaskHandler");
            String subtaskState=(String)SubtaskReports.get(i).get("subtaskState");
            String subtaskProgress=(String)SubtaskReports.get(i).get("subtaskProgress");
            Integer subtaskId=(Integer)SubtaskReports.get(i).get("subtaskId");
            if(subtaskState.equals("1")) {
                subtaskReport.put("taskState", "未开始");
            }else if(subtaskState.equals("2")){
                subtaskReport.put("taskState", "开发中");
            }else if(subtaskState.equals("3")){
                subtaskReport.put("taskState", "预验收");
            }else if(subtaskState.equals("4")){
                subtaskReport.put("taskState", "已完成");
            }else if(subtaskReport.equals("5")){
                subtaskReport.put("taskState", "逾期");
            }

            List<Map> subtaskLogs= DayReportDao.selectSubtaskLogById(subtaskId,startDate,endDate);
            for(int j=0;j<subtaskLogs.size();j++){
                subtaskLogs.get(j).put("idd",j+1);
            }
            StringBuffer str=new StringBuffer();
            for(Map<String,Object> subtasklog:subtaskLogs){
                Integer idd=(Integer)subtasklog.get("idd");
                String Emp=(String)subtasklog.get("Emp");
                String explain=(String)subtasklog.get("explain");
                str.append(idd).append('.').append(Emp).append(explain).append(" ");
            }
            String tasklogs=str+"";
            subtaskReport.put("subtaskName",subtaskName);
            subtaskReport.put("subtaskHandler",subtaskHandler);
            subtaskReport.put("subtaskProgress",subtaskProgress);
            subtaskReport.put("subtaskLogs",tasklogs);
            subtaskReports.add(subtaskReport);
        }
        Report.put("subtaskReports",subtaskReports);

        return Report;
    }
}
