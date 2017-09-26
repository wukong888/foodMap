package com.marketing.system.controller;

import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.Role;
import com.marketing.system.service.OnlineProService;
import com.marketing.system.service.RoleService;
import com.marketing.system.util.ApiResult;
import com.marketing.system.util.Constant;
import com.marketing.system.util.RdPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(description = "上线待审批接口", value = "上线待审批接口")
@Scope("prototype")
@RestController
@RequestMapping("/OnPro")
@EnableAutoConfiguration
public class OnlineProController {

        private Logger logger = LoggerFactory.getLogger(LoginController.class);

        @Resource
        private OnlineProService OnProService;



        /**
         * 查询立项待审批列表
         *
         * @return
         */
        @ApiOperation(value = "查询上线待审批列表")
        @ApiImplicitParams({
                @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
                @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer"),
                @ApiImplicitParam(paramType = "query", name = "creatersquadid", value = "项目发起部门Id", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "creater", value = "项目发起人", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "createdate1", value = "项目发起时间-开始", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "createdate2", value = "项目发起时间-结束", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "plansdate1", value = "项目完成时间-开始", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "plansdate2", value = "项目完成时间-结束", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "protype", value = "项目类型", required = false, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "param", value = "关键字", required = false, dataType = "String"),
        })
        @RequestMapping(value = "/selectOnPro", method = RequestMethod.POST)
        public ApiResult<List<ProjectInfo>> selectOnPro(
                @RequestParam(value = "current") int current,
                @RequestParam(value = "pageSize") int pageSize,
                @RequestParam(value="createsquadid") String createsquadid,
                @RequestParam(value="creater") String creater,
                @RequestParam(value="createdate1") String createdate1,
                @RequestParam(value="createdate2") String createdate2,
                @RequestParam(value="plansdate1") String plansdate1,
                @RequestParam(value="plansdate2") String plansdate2,
                @RequestParam(value="protype") String protype,
                @RequestParam(value="param") String param) {

            ApiResult<List<ProjectInfo>> result =null;
            Map<String,Object> OnProMap=OnProService.selectOnPro(current,pageSize,createsquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param);

            List<ProjectInfo> OnPro=(List<ProjectInfo>)OnProMap.get("OnPro");
            Integer sum=(Integer)OnProMap.get("OnProNum");

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
                result = new ApiResult<List<ProjectInfo>>(Constant.SUCCEED_CODE_VALUE,msg,OnPro,rdPage);
            }

            return  result;

        }

    }

