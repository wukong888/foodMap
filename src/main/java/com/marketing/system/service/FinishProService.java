package com.marketing.system.service;

import com.marketing.system.entity.*;

import java.util.List;
import java.util.Map;

public interface FinishProService {

    //模糊查询所有待审批的项目
    Map<String, Object> selectFinishPro(Integer current, Integer pageSize, String creatersquadid, String creater, String createdate1, String createdate2, String finishdate1, String finishdate2,String onlinedate1,String onlinedate2,String protype, String param);

    //查看归档项目的基本信息和项目信息
    ProjectInfo selectFinProInfo(Integer id,Integer proId);

    //查看归档项目的日志记录
    List<ProLogRecord> selectFinProLogRecord(Integer proId);

    //查看归档项目的开发日志
    List<ProDevelopLog> selectFinProDevRecord(Integer proId);

    //查看归档项目的参与组
    List<Map> selectFinTask(Integer proId);

    //查看单条任务详细信息
    ProjectTask selectFinTaskInfo(Integer taskId);

    //查看任务中的子任务
    List<ProjectSubtask> selectFinSubtask(Integer taskId);

    //查看任务中的任务日志记录
    List<TaskLogRecord> selectFinTaskLogRecord(Integer taskId);

    //查看上线待审批的任务开发日志
    List<TaskDevelopLog> selectFinTaskDevRecord(Integer taskId);

    //查看上线待审批任务的子任务开发日志
    List<SubtaskDevelopLog> selectFinSubTaskDevRecord(Integer subtaskId);

    //查看子任务日志记录
    List<SubtaskLogRecord> selectFinSubtaskLogRecord(Integer subtaskId);

    //查看子任务的详细信息
    ProjectSubtask selectFinSubtaskInfo(Integer subtaskId);
}
