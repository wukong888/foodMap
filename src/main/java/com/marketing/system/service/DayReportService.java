package com.marketing.system.service;

import java.util.List;
import java.util.Map;

public interface DayReportService {

    //模糊查询项目日报
    Map<String,Object> selectProReport(Integer current, Integer pageSize, String Date);

    //模糊查询任务日报
    Map<String,Object> selectTaskReport(Integer current, Integer pageSize, String Date, Integer proId);

    //模糊查询子任务日报
    Map<String,Object> selectSubtaskReport(Integer current, Integer pageSize, String Date, Integer proId, Integer taskId);

    //项目日报的导出
    Map<String,Object> exportProExcel(String date);

    //任务日报的定时导出
    Map<String,Object> exportTaskExcel(String date);

    //子任务日报的定时导出
    Map<String,Object> exportSubtaskExcel(String date);
}
