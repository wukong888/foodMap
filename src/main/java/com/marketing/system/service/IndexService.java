package com.marketing.system.service;

import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectSubtask;
import com.marketing.system.entity.ProjectTask;
import com.marketing.system.entity.SystemUser;
import javafx.beans.binding.ObjectExpression;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IndexService {

    Integer getMyApplyProject(String name);

    Integer getMyJoinProject(SystemUser user);

    Integer getUpApplyProject(String proState);

    List<ProjectInfo> getProjectInfoList(Map<String,Object> map) throws ParseException;

    List<Map<String, Object>> getProjectTaskList(String creater) throws ParseException;

    List<ProjectSubtask> getProjectSubTaskList(String creater) throws ParseException;

    //项目推送-产品:开发中的项目数量
    Integer getDevelopProjects(String creater);

    //项目推送-产品:立项待审批
    Integer getLxProjects(String creater);

    //项目推送-产品:上线待审批
    Integer getSxProjects(String creater);

    //项目推送-活动:开发中的项目数量
    Integer getHdDevelopProjects(String creater);

    //项目推送-活动:立项待审批
    Integer getHdLxProjects(String creater);

    //项目推送-活动:上线待审批
    Integer getHdSxProjects(String creater);

    //组长/经理 查看自己和其组员涉及项目
    List<ProjectInfo> getZuZhangProjectInfos(Map<String,Object> map) throws ParseException;

    //CEO-待审批的产品项目
    List<ProjectInfo> getApprovedProducts(Map<String,Object> map);

    //CEO-待审批的活动项目
    List<ProjectInfo> getActivityProducts(Map<String,Object> map);

    //开发中的总项目数包含逾期
    Integer getAllDevelopProjects();

    //开发中的总项目数List
    List<ProjectInfo> getAllDevelopProjectsList(Map<String,Object> map);
}
