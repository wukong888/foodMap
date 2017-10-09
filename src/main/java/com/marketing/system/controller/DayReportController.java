package com.marketing.system.controller;


import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.service.DayReportService;
import com.marketing.system.util.ApiResult;
import com.marketing.system.util.Constant;
import com.marketing.system.util.RdPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
}
