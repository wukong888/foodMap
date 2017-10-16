package com.marketing.system.service.impl;

import com.marketing.system.entity.*;
import com.marketing.system.mapper_two.ProDevelopLogMapper;
import com.marketing.system.mapper_two.*;
import com.marketing.system.service.MyProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MyProjectServiceImpl implements MyProjectService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private ProjectSubtaskMapper projectSubtaskMapper;

    @Autowired
    private ProjectTaskMapper projectTaskMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private MembersMapper membersMapper;

    @Autowired
    private ProDevelopLogMapper proDevelopLogMapper;

    @Autowired
    private TaskDevelopLogMapper taskDevelopLogMapper;

    @Autowired
    private SubtaskDevelopLogMapper subtaskDevelopLogMapper;

    @Autowired
    private TaskLogRecordMapper taskLogRecordMapper;

    @Autowired
    private SubtaskLogRecordMapper subtaskLogRecordMapper;

    @Override
    public List<ProjectInfo> getMyProjectInfoList(Map<String, Object> map) {

        List<ProjectInfo> list = projectInfoMapper.getMyProjectInfoList(map);

        return list;
    }

    @Override
    public List<Map<String,Object>> getSubTaskIdByHander(String name) {

        List<Map<String,Object>> list = projectSubtaskMapper.getSubTaskIdByHander(name);

        return list;
    }

    @Override
    public List<Map<String, Object>> getproIdByTaskId(Map<String, Object> map) {

        List<Map<String,Object>> list = projectTaskMapper.getproIdByTaskId(map);

        return list;
    }

    @Override
    public Department getDepartmentIdByMent(String department) {

        return departmentMapper.getDepartmentIdByMent(department);
    }

    @Override
    public List<Map<String, Object>> getSquadId(String departmentid) {

        List<Map<String,Object>> list = groupMapper.getSquadId(departmentid);

        return list;
    }

    @Override
    public List<Map<String, Object>> getMembers(Map<String, Object> map) {

        List<Map<String,Object>> list = membersMapper.getMembers(map);

        return list;
    }

    @Override
    public List<Map<String, Object>> getSubTaskListMap(Map<String, Object> map) {

        List<Map<String,Object>> list = projectSubtaskMapper.getSubTaskListMap(map);

        return list;
    }

    @Override
    public List<ProDevelopLog> getProDevelopLogList(int proId) {

        List<ProDevelopLog> logList = proDevelopLogMapper.getProDevelopLogList(proId);

        return logList;
    }

    @Override
    public int insertProDeveLog(ProDevelopLog proDevelopLog) {

        return proDevelopLogMapper.insert(proDevelopLog);
    }

    @Override
    public List<TaskDevelopLog> getTaskDeveLogList(int secondLevelId) {

        List<TaskDevelopLog> logList = taskDevelopLogMapper.getTaskDeveLogList(secondLevelId);

        return logList;
    }

    @Override
    public List<SubtaskDevelopLog> getSubTaskDevLogList(int secondLevelId) {

        List<SubtaskDevelopLog> logList = subtaskDevelopLogMapper.getSubTaskDevLogList(secondLevelId);

        return logList;
    }

    @Override
    public int insertProTask(ProjectTask projectTask) {

        return projectTaskMapper.insert(projectTask);
    }

    @Override
    public int updateTaskById(ProjectTask projectTask) {

        return projectTaskMapper.updateByPrimaryKeySelective(projectTask);
    }

    @Override
    public int deleteSubTaskById(int taskId) {

        return projectSubtaskMapper.deleteSubTaskById(taskId);
    }

    @Override
    public int deleteTaskById(int taskId) {

        return projectTaskMapper.deleteByPrimaryKey(taskId);
    }

    @Override
    public ProjectTask getProjectTaskByTaskId(int taskId) {

        return projectTaskMapper.selectByPrimaryKey(taskId);
    }

    @Override
    public List<TaskLogRecord> getTaskLogList(int taskId) {

        List<TaskLogRecord> logList = taskLogRecordMapper.getTaskLogList(taskId);

        return logList;
    }

    @Override
    public int insertTaskDevlog(TaskDevelopLog taskDevelopLog) {

        return taskDevelopLogMapper.insert(taskDevelopLog);
    }

    //添加子任务开发日志
    @Override
    public int insertSubTaskDevlog(SubtaskDevelopLog subtaskDevelopLog) {

        return subtaskDevelopLogMapper.insert(subtaskDevelopLog);
    }

    //获取子任务列表
    @Override
    public List<ProjectSubtask> getProjectSubtaskList(int taskId) {

        List<ProjectSubtask> list = projectSubtaskMapper.getProjectSubtaskList(taskId);

        return list;
    }

    //我的项目--任务分配详细页-子任务列表-添加
    @Override
    public int insertProSubTask(ProjectSubtask projectSubtask) {

        return projectSubtaskMapper.insert(projectSubtask);
    }

    //我的项目--任务分配详细页-子任务列表-修改
    @Override
    public int updateProSubTask(ProjectSubtask projectSubtask) {

        return projectSubtaskMapper.updateByPrimaryKeySelective(projectSubtask);
    }

    //插入日志-子任务日志记录
    @Override
    public int insertSubTaskLogRecord(SubtaskLogRecord subtaskLogRecord) {

        return subtaskLogRecordMapper.insert(subtaskLogRecord);
    }

    //插入日志-任务日志记录
    @Override
    public int insertTaskLogRecode(TaskLogRecord taskLogRecord) {

        return taskLogRecordMapper.insert(taskLogRecord);
    }

    //我的项目--任务分配详细页-子任务列表-删除
    @Override
    public int deleteProSubTaskById(int subtaskId) {

        return projectSubtaskMapper.deleteByPrimaryKey(subtaskId);
    }

    //我的项目--子任务详情页 - 基本信息+任务信息
    @Override
    public ProjectSubtask getProjectSubtaskById(int subtaskId) {

        return projectSubtaskMapper.selectByPrimaryKey(subtaskId);
    }

    //我的项目--子任务详情页 - 日志记录
    @Override
    public List<SubtaskLogRecord> getSubtaskLogList(int subtaskId) {

        return subtaskLogRecordMapper.getSubtaskLogList(subtaskId);
    }

    //同步更新项目进度
    @Override
    public int updateProjectInfo(ProjectInfo projectInfo) {

        return projectInfoMapper.updateByPrimaryKeySelectiveState(projectInfo);
    }

    //当前登录用户并其成员包含所涉及子任务
    @Override
    public List<Map<String, Object>> getSubTaskIdByHanderMap(Map<String, Object> map) {

        List<Map<String,Object>> list = projectSubtaskMapper.getSubTaskIdByHanderMap(map);

        return list;
    }

    @Override
    public Department getDepartmentById(Map<String, Object> map) {

        return departmentMapper.getDepartmentTwo(map);
    }

    @Override
    public List<ProjectInfo> getProjectByHanderMap(Map<String, Object> map) {

        List<ProjectInfo> list = projectInfoMapper.getProjectByHanderMap(map);

        return list;
    }
}
