package com.marketing.system.service;


import com.marketing.system.entity.*;

import java.util.List;
import java.util.Map;

public interface OnlineProService {

    //模糊查询所有待审批的项目
    Map<String, Object> selectOnPro(Integer current, Integer pageSize, String creatersquadid, String creater, String createdate1, String createdate2, String finishdate1, String finishdate2, String protype, String param);

    //查看上线待审批项目的基本信息和项目信息
    ProjectInfo selectOnProInfo(Integer id,Integer proId);

    //查看上线待审批项目的日志记录
    List<ProLogRecord> selectOnProLogRecord(Integer proId);

    //查看上线待审批项目的开发日志
    List<ProDevelopLog> selectOnProDevRecord(Integer proId);

    //查看上线待审批项目的参与组
    List<Map> selectOnTask(Integer proId);

    //查看单条任务详细信息
    ProjectTask selectOnTaskInfo(Integer taskId);

    //查看任务中的子任务
    List<ProjectSubtask> selectOnSubtask(Integer taskId);

    //查看任务中的任务日志记录
    List<TaskLogRecord> selectOnTaskLogRecord(Integer taskId);

    //查看上线待审批的任务开发日志
    List<TaskDevelopLog> selectOnTaskDevRecord(Integer taskId);

    //查看上线待审批任务的子任务开发日志
    List<SubtaskDevelopLog> selectOnSubTaskDevRecord(Integer subtaskId);

    //查看子任务日志记录
    List<SubtaskLogRecord> selectOnSubtaskLogRecord(Integer subtaskId);

    //查看子任务的详细信息
    ProjectSubtask selectOnSubtaskInfo(Integer subtaskId);

    //通过操作，项目日志添加记录
    boolean insertProPassLog(Integer proid,String explain,String onlineDate);

    //驳回操作，项目日志添加记录
    boolean insertProReturnLog(Integer proid,String explain);

    //根据proid查找项目信息
    ProjectInfo selectProByProId(Integer proId);
}