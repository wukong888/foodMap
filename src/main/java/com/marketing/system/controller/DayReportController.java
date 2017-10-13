package com.marketing.system.controller;


import com.marketing.system.service.DayReportService;
import com.marketing.system.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(description = "日报接口", value = "日报接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class DayReportController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private DayReportService DayReportService;

    @Resource
    private HttpServletResponse response;




    /**
     * 模糊查询项目日报
     *
     * @return
     */
    @ApiOperation(value = "模糊查询项目日报")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面记录数", required = true, dataType = "Integer")

    })
    @RequestMapping(value = "proReports", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectProReport(
            @RequestParam(value = "current") int current,
            @RequestParam(value = "pageSize") int pageSize) {
        String Date=null;
        ApiResult<List<Map>> result =null;
        Map<String,Object> Report=DayReportService.selectProReport(current,pageSize,Date);
        List<Map> ProReport=(List<Map>)Report.get("proReports");
        Integer sum=(Integer)Report.get("proReportsNum");

        //分页信息
        RdPage rdPage = new RdPage();
        rdPage.setTotal(sum);
        rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
        rdPage.setCurrent(current);
        rdPage.setPageSize(pageSize);

        String msg = "";
        if (current > rdPage.getPages()) {
            msg = "已经超过当前所有页数！";
            result = new ApiResult<List<Map>>(Constant.FAIL_CODE_VALUE, msg, null, rdPage);
        } else {
            msg = "查询成功！";
            result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,ProReport,rdPage);
        }

        return  result;

    }

    /**
     * 模糊查询任务日报
     *
     * @return
     */
    @ApiOperation(value = "模糊查询任务日报")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面记录数", required = true, dataType = "Integer")

    })
    @RequestMapping(value = "/selectTaskReport", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectTaskReport(
            @RequestParam(value = "current") int current,
            @RequestParam(value = "pageSize") int pageSize) {

        ApiResult<List<Map>> result =null;
        String Date=null;


        Map<String,Object> Report=DayReportService.selectTaskReport(current,pageSize,Date);
        System.out.println("Report-----"+Report);
        List<Map> TaskReport=(List<Map>)Report.get("taskReports");
        Integer sum=(Integer)Report.get("taskReportsNum");

        //分页信息
        RdPage rdPage = new RdPage();
        rdPage.setTotal(sum);
        rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
        rdPage.setCurrent(current);
        rdPage.setPageSize(pageSize);

        String msg = "";
        if (current > rdPage.getPages()) {
            msg = "已经超过当前所有页数！";
            result = new ApiResult<List<Map>>(Constant.FAIL_CODE_VALUE, msg, null, rdPage);
        } else {
            msg = "查询成功！";
            result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,TaskReport,rdPage);
        }

        return  result;

    }

    /**
     * 模糊查询子任务日报
     *
     * @return
     */
    @ApiOperation(value = "模糊查询子任务日报")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面记录数", required = true, dataType = "Integer")

    })
    @RequestMapping(value = "/selectSubtaskReport", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectSubtaskReport(
            @RequestParam(value = "current") int current,
            @RequestParam(value = "pageSize") int pageSize) {

        ApiResult<List<Map>> result =null;
        String Date=null;
        Map<String,Object> Report=DayReportService.selectSubtaskReport(current,pageSize,Date);
        List<Map> SubtaskReport=(List<Map>)Report.get("subtaskReports");
        Integer sum=(Integer)Report.get("subtaskReportsNum");

        //分页信息
        RdPage rdPage = new RdPage();
        rdPage.setTotal(sum);
        rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
        rdPage.setCurrent(current);
        rdPage.setPageSize(pageSize);

        String msg = "";
        if (current > rdPage.getPages()) {
            msg = "已经超过当前所有页数！";
            result = new ApiResult<List<Map>>(Constant.FAIL_CODE_VALUE, msg, null, rdPage);
        } else {
            msg = "查询成功！";
            result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,SubtaskReport,rdPage);
        }

        return  result;

    }

    /**
     * 日报定时导出
     *
     * @return

    @RequestMapping(value = "/exportProExcelTime", method = RequestMethod.POST)
    @Scheduled(cron="0 0/1 * * * ?")
    public ApiResult<List<Map>> exportExcelTime() {
       String date= DateUtil.getYMDDate();

       System.out.println("---"+new Date());
        //项目日报的定时导出
        Map<String,Object> Report1=DayReportService.exportProExcel(date);
        List<Map> ProReport=(List<Map>)Report1.get("proReports");

        String pathProName ="\\static\\"+date+"ProjectReport.xlsx";
        try {
            File file=new File(pathProName);
            OutputStream outputStream1= new FileOutputStream(file);
            DayReportExport1.exportExcel(ProReport,outputStream1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //任务日报的定时导出
        Map<String,Object> Report2=DayReportService.exportTaskExcel(date);
        List<Map> TaskReport=(List<Map>)Report2.get("taskReports");
        String pathTaskName ="\\static\\"+date+"TaskReport.xlsx";
        try {
            File file=new File(pathTaskName);
            OutputStream outputStream2= new FileOutputStream(file);
            DayReportExport1.exportExcel(TaskReport,outputStream2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //子任务日报的定时导出
        Map<String,Object> Report3=DayReportService.exportSubtaskExcel(date);
        List<Map> SubtaskReport=(List<Map>)Report2.get("subtaskReports");
        String pathSubtaskName ="\\static\\"+date+"SubtaskReport.xlsx";
        try {
            File file=new File(pathSubtaskName);
            OutputStream outputStream3= new FileOutputStream(file);
            DayReportExport1.exportExcel(SubtaskReport,outputStream3);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }*/

    /**
     * 日报导出
     *
     * @return
     */
    @ApiOperation(value = "日报导出")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "Date", value = "日期（如：2010-01-01）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "日报类型（1：项目，2：任务，3：子任务）", required = true, dataType = "Integer")

    })
    @RequestMapping(value = "/exportDayReport", method = RequestMethod.POST)
    public ApiResult<List<Map>> exportDayReport(
            @RequestParam(value = "Date") String Date,
            @RequestParam(value = "type") int type,
            org.apache.catalina.servlet4preview.http.HttpServletRequest request, HttpServletResponse response){
        ApiResult<List<Map>> result =null;



       if(type==1){
           String proFile="static//"+Date+" ProjectReport.xlsx";
           File file=new File(proFile);
           response.setContentType("application/force-download");// 设置强制下载不打开
           response.addHeader("Content-Disposition",
                   "attachment;fileName="+Date+" ProjectReport.xlsx");// 设置文件名

           try {
                       OutputStream out = response.getOutputStream();
                       ToolUtil.downloadFile(file, out);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }

       }else if(type==2){
           String taskFile="static//"+Date+" TaskReport.xlsx";
           File file=new File(taskFile);
           response.setContentType("application/force-download");// 设置强制下载不打开
           response.addHeader("Content-Disposition",
                   "attachment;fileName="+Date+" TaskReport.xlsx");// 设置文件名

           try {
                   OutputStream out = response.getOutputStream();
                   ToolUtil.downloadFile(file, out);
               } catch (Exception e) {
                   e.printStackTrace();
               }

      }else if(type==3){
           String subtaskFile="static//"+Date+" SubtaskReport.xlsx";
           File file=new File(subtaskFile);
           response.setContentType("application/force-download");// 设置强制下载不打开
           response.addHeader("Content-Disposition","attachment;fileName="+Date+" SubtaskReport.xlsx");// 设置文件名
           try {
               OutputStream out = response.getOutputStream();
               ToolUtil.downloadFile(file,out);
           } catch (Exception e) {
               e.printStackTrace();
           }

       }
        return null;
    }

}
