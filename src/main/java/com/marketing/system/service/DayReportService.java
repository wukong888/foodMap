package com.marketing.system.service;

import java.util.List;
import java.util.Map;

public interface DayReportService {

    //模糊查询项目日报
    Map<String,Object> selectProReport(Integer current, Integer pageSize, String Date);

    //模糊查询任务日报
    Map<String,Object> selectTaskReport(Integer current, Integer pageSize, String Date);

    //模糊查询任务日报
    Map<String,Object> selectSubtaskReport(Integer current, Integer pageSize, String Date);
}
