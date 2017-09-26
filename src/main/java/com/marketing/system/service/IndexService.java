package com.marketing.system.service;

import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectSubtask;
import com.marketing.system.entity.ProjectTask;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IndexService {

    Integer getMyApplyProject(String name);

    Integer getMyJoinProject(String name);

    Integer getUpApplyProject(String proState);

    List<ProjectInfo> getProjectInfoList(String creater) throws ParseException;

    List<Map<String, Object>> getProjectTaskList(String creater) throws ParseException;

    List<ProjectSubtask> getProjectSubTaskList(String creater) throws ParseException;

}
