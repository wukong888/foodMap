package com.marketing.system.job;

import com.marketing.system.entity.UserInfo;
import com.marketing.system.service.UserInfoService;
import com.marketing.system.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


@Component
@EnableScheduling
@PropertySource("classpath:application.properties")
public class TestJob extends BatchProperties.Job {
    private static final Logger logger = Logger.getLogger(TestJob.class);

    @Autowired
    private UserInfoService userInfo;

    @Resource
    private com.marketing.system.service.DayReportService DayReportService;

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
        String pathProName ="static//"+date+"ProjectReport.xlsx";
        try {
            File file=new File(pathProName);
            if(!file.exists()){
                file.createNewFile();
            }

             OutputStream outputStream1= new FileOutputStream(file);
             DayReportExport1.exportExcel(ProReport,outputStream1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("导出"+date+"任务日报");
        //任务日报的定时导出
        Map<String,Object> Report2=DayReportService.exportTaskExcel(date);
        System.out.println("Report2====="+Report2);
        List<Map> TaskReport=(List<Map>)Report2.get("taskReports");
        String pathTaskName ="static//"+date+"TaskReport.xlsx";
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
        String pathSubtaskName ="static//"+date+"SubtaskReport.xlsx";
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

}
