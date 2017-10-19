package com.marketing.system.controller;

import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.service.ProReportService;
import com.marketing.system.util.ApiResult;
import com.marketing.system.util.Constant;
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

@Api(description = "项目统计接口", value = "项目统计接口")
@Scope("prototype")
@RestController
@RequestMapping("/ProReport")
@EnableAutoConfiguration
public class ProReportController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private ProReportService ProReportService;

    /**
     * 查询项目统计列表
     *
     * @return
     */
    @ApiOperation(value = "项目统计列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "creatersquadid", value = "项目发起部门名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "creater", value = "项目发起人", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createdate1", value = "项目发起时间-开始", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createdate2", value = "项目发起时间-结束", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "finishdate1", value = "项目完成时间-开始", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "finishdate2", value = "项目完成时间-结束", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "onlinedate1", value = "项目上线时间-开始", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "onlinedate2", value = "项目上线时间-结束", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "param", value = "关键字", required = false, dataType = "String"),
    })
    @RequestMapping(value = "/selectOnPro", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectOnPro(
            @RequestParam(value="creatersquadid", required = false) String creatersquadid ,
            @RequestParam(value="creater", required = false) String creater,
            @RequestParam(value="createdate1", required = false) String createdate1,
            @RequestParam(value="createdate2", required = false) String createdate2,
            @RequestParam(value="finishdate1", required = false) String finishdate1,
            @RequestParam(value="finishdate2", required = false) String finishdate2,
            @RequestParam(value="onlinedate1", required = false) String onlinedate1,
            @RequestParam(value="onlinedate2", required = false) String onlinedate2,
            @RequestParam(value="param", required = false) String param){

            ApiResult<List<Map>> result =null;
        try {
            List<Map> ProReports=ProReportService.selectProReport(creatersquadid,creater,createdate1,createdate2,finishdate1,finishdate2,onlinedate1,onlinedate2,param);

            return new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,"查询成功",ProReports,null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("项目统计列表 错误信息：" + e);
        }
        return result;
    }
}
