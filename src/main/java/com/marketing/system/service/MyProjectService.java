package com.marketing.system.service;

import com.marketing.system.entity.*;

import java.util.List;
import java.util.Map;

public interface MyProjectService {

    List<ProjectInfo> getMyProjectInfoList(Map<String,Object> map);

    List<Map<String,Object>> getSubTaskIdByHander(String name);

    List<Map<String,Object>> getproIdByTaskId(Map<String,Object> map);

    Department getDepartmentIdByMent(String department);

    List<Map<String,Object>> getSquadId(String departmentid);

    List<Map<String,Object>> getMembers(Map<String,Object> map);

    List<Map<String,Object>> getSubTaskListMap(Map<String,Object> map);

    List<ProDevelopLog> getProDevelopLogList(int proId);

    int insertProDeveLog(ProDevelopLog proDevelopLog);

    List<TaskDevelopLog> getTaskDeveLogList(int secondLevelId);

    List<SubtaskDevelopLog> getSubTaskDevLogList(int secondLevelId);

    int insertProTask(ProjectTask projectTask);

    int updateTaskById(ProjectTask projectTask);

    int deleteSubTaskById(int taskId);

    int deleteTaskById(int taskId);

    ProjectTask getProjectTaskByTaskId(int taskId);

    List<TaskLogRecord> getTaskLogList(int taskId);

    //添加任务开发日志
    int insertTaskDevlog(TaskDevelopLog taskDevelopLog);

    //添加子任务开发日志
    int insertSubTaskDevlog(SubtaskDevelopLog subtaskDevelopLog);

    //获取子任务列表
    List<ProjectSubtask> getProjectSubtaskList(int taskId);

    //我的项目--任务分配详细页-子任务列表-添加
    int insertProSubTask(ProjectSubtask projectSubtask);

    //我的项目--任务分配详细页-子任务列表-修改
    int updateProSubTask(ProjectSubtask projectSubtask);

    //插入日志-子任务日志记录
    int insertSubTaskLogRecord(SubtaskLogRecord subtaskLogRecord);

    //插入日志-任务日志记录
    int insertTaskLogRecode(TaskLogRecord taskLogRecord);

    //我的项目--任务分配详细页-子任务列表-删除
    int deleteProSubTaskById(int subtaskId);

    //我的项目--子任务详情页 - 基本信息+任务信息
    ProjectSubtask getProjectSubtaskById(int subtaskId);

    //我的项目--子任务详情页 - 日志记录
    List<SubtaskLogRecord> getSubtaskLogList(int subtaskId);

    //同步更新项目进度
    int updateProjectInfo(ProjectInfo projectInfo);
}