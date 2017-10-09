package com.marketing.system.service;

import java.util.List;
import java.util.Map;

public interface ProReportService {

    //模糊查询统计项目
    List<Map> selectProReport( String creatersquadid,String creater, String createdate1, String createdate2,String finishdate1,String finishdate2,String onlinedate1,String onlinedate2
                              ,String param);

}
