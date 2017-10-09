package com.marketing.system.service.impl;

import com.marketing.system.entity.ProDevelopLog;
import com.marketing.system.mapper_two.DayReportMapper;
import com.marketing.system.service.DayReportService;
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
            Date DATE=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date= sdf.format(DATE);
        }
        String startDate=date+" 00:00:00";
        String endDate=date+" 23:59:59";

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
            System.out.println("proLogs======"+proLogs);
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
        System.out.println("proReports----"+proReports);

        Integer proReportsNum=DayReportDao.selectProReportNum(startDate,endDate);
        Report.put("proReports",proReports);
        Report.put("proReportsNum",proReportsNum);
        return  Report;
    }

    //模糊查询任务日报
    public Map<String,Object> selectTaskReport(Integer current, Integer pageSize, String Date){

        String date=null;
        if(Date==null||Date==""){
            Date DATE=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date= sdf.format(DATE);
        }
        String startDate=date+" 00:00:00";
        String endDate=date+" 23:59:59";

        Map<String,Object> Report=new HashMap<String ,Object>();

        //任务日报的所有记录
        List<Map> taskReports=new ArrayList<Map>();

        List<Map> TaskReport=DayReportDao.selectTaskReport(current,pageSize,startDate,endDate);
        //得到该项目的动态记录
        for(int i=0;i<TaskReport.size();i++){
            //项目日报的单条记录
            Map<String,Object> taskReport=new HashMap<String,Object>();
            String taskName=(String)TaskReport.get(i).get("taskName");
            String handler=(String)TaskReport.get(i).get("handler");
            String taskState=(String)TaskReport.get(i).get("taskState");
            String taskProgress=(String)TaskReport.get(i).get("taskProgress");
            Integer taskId=(Integer)TaskReport.get(i).get("taskId");
            List<Map> taskLogs= DayReportDao.selectTaskLogById(taskId,startDate,endDate);
            System.out.println("taskLogs======"+taskLogs);
            for(int j=0;j<taskLogs.size();j++){
                taskLogs.get(j).put("idd",j+1);
            }
            taskReport.put("taskName",taskName);
            taskReport.put("handler",handler);
            taskReport.put("taskState",taskState);
            taskReport.put("taskProgress",taskProgress);
            taskReport.put("taskLogs",taskLogs);
            taskReports.add(taskReport);
        }
        System.out.println("taskReports----"+taskReports);


        Integer taskReportsNum=DayReportDao.selectTaskReportNum(startDate,endDate);
        Report.put("taskReports",taskReports);
        Report.put("taskReportsNum",taskReportsNum);
        return  Report;
    }

    //模糊查询子任务日报
    public Map<String,Object> selectSubtaskReport(Integer current, Integer pageSize, String Date){

        String date=null;
        if(Date==null||Date==""){
            Date DATE=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date= sdf.format(DATE);
        }
        String startDate=date+" 00:00:00";
        String endDate=date+" 23:59:59";

        Map<String,Object> Report=new HashMap<String ,Object>();

        //子任务日报的所有记录
        List<Map> subtaskReports=new ArrayList<Map>();

        List<Map> SubtaskReport=DayReportDao.selectSubtaskReport(current,pageSize,startDate,endDate);
        //得到该子任务的动态记录
        for(int i=0;i<SubtaskReport.size();i++){
            //子任务日报的单条记录
            Map<String,Object> subtaskReport=new HashMap<String,Object>();
            String subtaskName=(String)SubtaskReport.get(i).get("subtaskName");
            String subtaskHandler=(String)SubtaskReport.get(i).get("subtaskHandler");
            String subtaskState=(String)SubtaskReport.get(i).get("subtaskState");
            String subtaskProgress=(String)SubtaskReport.get(i).get("subtaskProgress");
            Integer subtaskId=(Integer)SubtaskReport.get(i).get("subtaskId");
            List<Map> subtaskLogs= DayReportDao.selectSubtaskLogById(subtaskId,startDate,endDate);
            System.out.println("subtaskLogs======"+subtaskLogs);
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
        System.out.println("subtaskReports----"+subtaskReports);
        Integer subtaskReportsNum=DayReportDao.selectSubtaskReportNum(current,pageSize,startDate,endDate);
        Report.put("subtaskReports",subtaskReports);
        Report.put("subtaskReportsNum",subtaskReportsNum);
        return  Report;
    }
}
