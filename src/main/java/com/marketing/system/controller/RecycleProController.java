package com.marketing.system.controller;

import com.marketing.system.entity.ProDevelopLog;
import com.marketing.system.entity.ProLogRecord;
import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectTask;
import com.marketing.system.service.OnlineProService;
import com.marketing.system.service.RecycleProService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "上线待审批接口", value = "上线待审批接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class RecycleProController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private RecycleProService RecProService;



    /**
     * 查询回收列表
     *
     * @return
     */
    @ApiOperation(value = "查询回收列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "creatersquadid", value = "项目发起部门", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "creater", value = "项目发起人", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createdate1", value = "项目发起时间-开始", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createdate2", value = "项目发起时间-结束", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "plansdate1", value = "项目预计完成时间-开始", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "plansdate2", value = "项目预计完成时间-结束", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "protype", value = "项目类型", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "param", value = "关键字", required = false, dataType = "String"),
    })
    @RequestMapping(value = "/selectRecPro", method = RequestMethod.POST)
    public ApiResult<List<ProjectInfo>> selectRecPro(
            @RequestParam(value = "current") int current,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value="creatersquadid", required = false) String creatersquadid,
            @RequestParam(value="creater", required = false) String creater,
            @RequestParam(value="createdate1", required = false) String createdate1,
            @RequestParam(value="createdate2", required = false) String createdate2,
            @RequestParam(value="plansdate1", required = false) String plansdate1,
            @RequestParam(value="plansdate2", required = false) String plansdate2,
            @RequestParam(value="protype", required = false) String protype,
            @RequestParam(value="param", required = false) String param) {

        ApiResult<List<ProjectInfo>> result =null;
        Map<String,Object> RecProMap=RecProService.selectRecPro(current,pageSize,creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param);

        List<ProjectInfo> RecPro=(List<ProjectInfo>)RecProMap.get("RecPro");
        Integer sum=(Integer)RecProMap.get("RecProNum");

        //分页信息
        RdPage rdPage = new RdPage();
        rdPage.setTotal(sum);
        rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
        rdPage.setCurrent(current);
        rdPage.setPageSize(pageSize);

        String msg = "";
        if (current > rdPage.getPages()) {
            msg = "已经超过当前所有页数！";
            result = new ApiResult<List<ProjectInfo>>(Constant.FAIL_CODE_VALUE, msg, null, rdPage);
        } else {
            msg = "查询成功！";
            result = new ApiResult<List<ProjectInfo>>(Constant.SUCCEED_CODE_VALUE,msg,RecPro,rdPage);
        }

        return  result;

    }

    /**
     * 项目详细信息
     *
     * @return
     */
    @ApiOperation(value = "查看项目的基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目的记录id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "项目编号", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/selectRecProInfo", method = RequestMethod.POST)
    public ApiResult<List<Map>> selectRecProInfo(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "proId") int proId){
        ApiResult<List<Map>> result =null;

        List<Map> RecProInfos=new ArrayList<Map>();
        Map<String,Object> RecProInfo=new HashMap<String,Object>();
        ProjectInfo ProInfo=RecProService.selectRecProInfo(id,proId);
        List<ProLogRecord> ProLogRecord=RecProService.selectRecProLogRecord(proId);
        List<ProjectTask> ProTask=RecProService.selectRecTask(proId);

        RecProInfo.put("ProInfo",ProInfo);
        RecProInfo.put("ProLogRecord",ProLogRecord);
        RecProInfo.put("ProTask",ProTask);
        RecProInfos.add(RecProInfo);
        String msg = "查询成功！";

        result = new ApiResult<List<Map>>(Constant.SUCCEED_CODE_VALUE,msg,RecProInfos,null);
        return result;
    }

}
