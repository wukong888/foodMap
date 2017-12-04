package com.marketing.system.controller;

import com.marketing.system.entity.*;
import com.marketing.system.mapper.DepartmentNewMapper;
import com.marketing.system.service.IndexService;
import com.marketing.system.service.MyProjectService;
import com.marketing.system.service.SystemUserService;
import com.marketing.system.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(description = "用户首页接口", value = "用户首页接口")
@Scope("prototype")
@RestController
@EnableAutoConfiguration
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IndexService indexService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private MyProjectService myProjectService;

    @Autowired
    private DepartmentNewMapper departmentNewMapper;

    /**
     * 用户首页
     *
     * @return
     */
    @ApiOperation(value = "用户首页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "登录用户id", required = true, dataType = "int")
    })
    @RequestMapping(value = "/getIndexInfo", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getIndexInfo(
            @RequestParam(value = "id", required = true) int id) throws ParseException {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {
            SystemUser user = systemUserService.selectByPrimaryKey(id);

            //职位，立项待审批、上线待审批暂时只有CEO有该权限
            String dutyName = user.getDuty();

            map.put("dutyName", dutyName);

            //项目推送
            //我申请的项目
            Integer i = indexService.getMyApplyProject(user.getUserName());

            //参与的项目 j 项目任务+子任务+项目
            Integer j = indexService.getMyJoinProject(user);

            //立项待审批
            String proState = "1";
            Integer k = indexService.getUpApplyProject(proState);

            //上线待审批
            String proTypeOnline = "3";
            Integer m = indexService.getUpApplyProject(proTypeOnline);

            map.put("applyProject", i);//我申请的项目
            map.put("joinProject", j);//参与的项目
            map.put("upProject", k);//立项待审批
            map.put("onlineProject", m);//上线待审批

            String creater = "";

            if ("CEO".equals(dutyName)) {
                creater = "";
            } else {
                creater = user.getUserName();
            }
            //逾期提示 1：项目
            Map<String,Object> map1 = new HashMap<>();
            map1.put("creater",creater);
            map1.put("current",1);
            map1.put("pageSize",1000);
            List<ProjectInfo> infoList = indexService.getProjectInfoList(map1);

            //逾期提示 2：任务

            List<Map<String, Object>> taskList = indexService.getProjectTaskList(creater);

            //逾期提示 3：子任务

            List<ProjectSubtask> subtaskList = indexService.getProjectSubTaskList(creater);

            map.put("infoList", infoList);//项目
            map.put("taskList", taskList);//任务
            map.put("subtaskList", subtaskList);//子任务

            list.add(map);
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, null);
            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户首页错误信息：" + e);
        }
        return result;
    }

    /**
     * 用户首页新
     * @param id
     * @return
     */
    @ApiOperation(value = "用户首页新")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "登录用户id", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getIndexInfoNew", method = RequestMethod.POST)
    public ApiResult<List<Map<String, Object>>> getIndexInfoNew(@RequestParam(value = "id", required = true) int id,
                                                                @RequestParam(value = "current") int current,
                                                                @RequestParam(value = "pageSize") int pageSize) {

        Map<String, Object> map = new HashMap<>();
        ApiResult<List<Map<String, Object>>> result = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try{
            SystemUser user = systemUserService.selectByPrimaryKey(id);

            //职位，立项待审批、上线待审批暂时只有CEO有该权限
            String dutyName = user.getDuty();

            map.put("dutyName", dutyName);

            String creater = "";

            if ("CEO".equals(dutyName)) {
                creater = "";
            } else {
                creater = user.getUserName();
            }


            /**
             * 项目概况
             */
            //逾期项目置顶、每页显示五条，超过五条翻页显示、CEO显示所有的项目概况
            Map<String,Object> map1 = new HashMap<>();
            map1.put("creater",creater);
            map1.put("current",current);
            map1.put("pageSize",1000);

            List<ProjectInfo> infoList = new ArrayList<>();
            Integer kf_cp_zz = 0;
            Integer kf_hd_zz = 0;
            if (creater != "" && !dutyName.contains("组长") && !dutyName.contains("经理")) {
                //组员 看自己的项目概况
                infoList = indexService.getProjectInfoList(map1);

            } else if (dutyName.contains("组长") || dutyName.contains("经理")){
                Map<String, Object> objectMap = new HashMap<>();

                objectMap.put("handler", user.getUserName());
                //objectMap.put("creater",creater);
                objectMap.put("current",current);
                objectMap.put("pageSize",1000);

/*******************************************对应组成员 开始***************************************************************/

                /**
                 * 判断是总监、经理、组员
                 * listDuty = 1 经理
                 * listDuty > 1 总监
                 * listDuty 为空 组员
                 */
                List<Map<String, Object>> listDuty = departmentNewMapper.getCheckDuty(user.getUserGroupId());

                List<Map<String, Object>> listMem = new ArrayList<>();
                if (listDuty.size() > 1) {
                    listMem = departmentNewMapper.getZjMember(user.getUserGroupId());
                } else if (listDuty.size() == 1){
                    listMem = departmentNewMapper.getJlMember(user.getUserGroupId());
                } else if (listDuty.size() == 0 ){
                    listMem = departmentNewMapper.getMemMember(user.getUserGroupId());
                }

                Map<String, Object> mapMem = new HashMap<>();

                mapMem = ToolUtil.getmapList(listMem, "id");

                List<Map<String, Object>> listMembers = systemUserService.getMembersByUserGroupId(mapMem);

                String menuLeafIdsmember2 = StringUtil.toString(MapUtil.collectProperty(listMembers, "UserName"));

                String[] Idsmember = menuLeafIdsmember2.split(",");


/*******************************************对应组成员 结束******************************************************************/

                objectMap.put("Idsmember", Idsmember);//小组成员
                //组长/经理 查看自己和其组员涉及项目
                infoList = indexService.getZuZhangProjectInfos(objectMap);

                List<ProjectInfo> infoListNumCp = new ArrayList<>();
                List<ProjectInfo> infoListNumHd = new ArrayList<>();

                //项目类型(1:产品，2：活动)
                infoListNumCp = infoList.stream().filter(t -> Integer.valueOf(t.getProtype()) == 1).collect(Collectors.toList());
                kf_cp_zz= infoListNumCp.size();

                infoListNumHd = infoList.stream().filter(t -> Integer.valueOf(t.getProtype()) == 2).collect(Collectors.toList());
                kf_hd_zz= infoListNumHd.size();
            } else {
                //ceo项目概况
                infoList = indexService.getProjectInfoList(map1);
            }

            /**
             * 项目推送-产品
             */
            //1、开发中的项目
            Integer kf_cp = indexService.getDevelopProjects(creater);
            if (dutyName.contains("组长") || dutyName.contains("经理")) {
                map.put("kf_cp", kf_cp_zz);
            } else {
                map.put("kf_cp", kf_cp);
            }

            //2、立项待审批
            Integer lx_cp = indexService.getLxProjects(creater);
            map.put("lx_cp", lx_cp);

            //3、上线待审批
            Integer sx_cp = indexService.getSxProjects(creater);
            map.put("sx_cp", sx_cp);

            /**
             * 项目推送-活动
             */
            //1、开发中的项目
            Integer kf_hd = indexService.getHdDevelopProjects(creater);
            if (dutyName.contains("组长") || dutyName.contains("经理")) {
                map.put("kf_hd",kf_hd_zz);
            } else {
                map.put("kf_hd",kf_hd);
            }


            //2、立项待审批L
            Integer lx_hd = indexService.getHdLxProjects(creater);
            map.put("lx_hd",lx_hd);

            //3、上线待审批
            Integer sx_hd = indexService.getHdSxProjects(creater);
            map.put("sx_hd",sx_hd);

            ProjectInfo projectInfo = new ProjectInfo();
            //循环计算距逾期时间天数
            for (int i = 0; i < infoList.size(); i++) {
                projectInfo = infoList.get(i);

                long betweenDays = ToolUtil.getBetweenTimes(projectInfo.getPlansdate());

                projectInfo.setBetweenDays(betweenDays);

            }

            infoList = infoList.stream().filter(t -> t.getBetweenDays() <= 6).collect(Collectors.toList());

            int sum = 0;
            sum = infoList.size();
            infoList = ToolUtil.listSplit2(current, pageSize, infoList);

            map.put("infoList",infoList);
            RdPage rdPage = new RdPage();
            //分页信息
            rdPage.setTotal(sum);
            rdPage.setPages(sum % pageSize == 0 ? sum / pageSize : sum / pageSize + 1);
            rdPage.setCurrent(current);
            rdPage.setPageSize(pageSize);

            list.add(map);
            result = new ApiResult<>(Constant.SUCCEED_CODE_VALUE, Constant.OPERATION_SUCCESS, list, rdPage);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户首页错误信息：" + e);
        }

        return result;
    }

}
