package com.marketing.system.service.impl;

import com.marketing.system.entity.*;
import com.marketing.system.mapper.DepartmentNewMapper;
import com.marketing.system.mapper_two.FinishProMapper;
import com.marketing.system.service.FinishProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinishProServiceImpl implements FinishProService {

    @Resource
    private FinishProMapper FinProDao;

    @Autowired
    private DepartmentNewMapper departmentNewMapper;

    //模糊查询所有待审批的项目
    public Map<String, Object> selectFinishPro(Integer current, Integer pageSize, String creatersquadid, String creater, String createdate1, String createdate2, String finishdate1, String finishdate2, String onlinedate1,String onlinedate2,String protype, String param){

        Map<String,Object> FinProMap=new HashMap<String,Object>();
        List<ProjectInfo> FinPro=FinProDao.selectFinPro(creatersquadid,creater,createdate1,createdate2,finishdate1,finishdate2,onlinedate1,onlinedate2,protype,param,current,pageSize);
        Integer FinProNum=FinProDao.selectFinProNum(creatersquadid,creater,createdate1,createdate2,finishdate1,finishdate2,onlinedate1,onlinedate2,protype,param);
        FinProMap.put("FinPro",FinPro);
        FinProMap.put("FinProNum",FinProNum);


        return FinProMap;
    }

    //查看归档项目的基本信息和项目信息
    public ProjectInfo selectFinProInfo(Integer id,Integer proId){
        ProjectInfo FinProInfo=FinProDao.selectFinProInfo(id);
        List<Map> FinTasks=FinProDao.selectFinTask(proId);
        Double sum = 0.0;
        for (Map<String,Object> FinTask:FinTasks) {
            FinTask.get("workDate");
            if (FinTask.get("workDate") !="" && FinTask.get("workDate") != null)
                //sum +=Integer.valueOf((String) FinTask.get("workDate"));
                sum += Double.parseDouble(String.valueOf(FinTask.get("workDate")));


        }
        FinProInfo.setWorkTatalDay(String.valueOf(sum));//项目预计工期（任务工期之和）
        return FinProInfo;
    }

    //查看归档项目的日志记录
    public List<ProLogRecord> selectFinProLogRecord(Integer proId){
        List<ProLogRecord> FinProLogRecords=FinProDao.selectFinProLogRecord(proId);
        for(ProLogRecord FinProLogRecord:FinProLogRecords){
            String squadId=FinProLogRecord.getSquadid();
            String squad=departmentNewMapper.selectSquadByIdNew(squadId);
            FinProLogRecord.setSquadid(squad);
        }
        return FinProLogRecords;
    }

    //查看归档项目的参与组
    public List<Map> selectFinTask(Integer proId){
        List<Map> FinTasks=FinProDao.selectFinTask(proId);
        for(int i=0;i<FinTasks.size();i++){
            String squadId=(String) FinTasks.get(i).get("squadId");
            String squad=departmentNewMapper.selectSquadByIdNew(squadId);
            String departmentId=departmentNewMapper.selectDepartmentIdBySquadIdNew(squadId);
            FinTasks.get(i).put("departmentId",departmentId);
            FinTasks.get(i).put("squad",squad);
        }
        return FinTasks;
    }

    //查看归档项目的开发日志
    public List<ProDevelopLog> selectFinProDevRecord(Integer proId){
        List<ProDevelopLog> FinProDevRecords=FinProDao.selectFinProDevRecord(proId);
        for(ProDevelopLog FinProDevRecord:FinProDevRecords){
            String squadId=FinProDevRecord.getSquadid()+"";
            String squad=departmentNewMapper.selectSquadByIdNew(squadId);
            FinProDevRecord.setSquadid(squad);
        }
        return FinProDevRecords;
    }

    //查看单条任务详细信息
    public ProjectTask selectFinTaskInfo(Integer taskId){
        ProjectTask taskInfo=FinProDao.selectFinTaskInfo(taskId);
        String squadid=taskInfo.getSquadId();
        String squad=departmentNewMapper.selectSquadByIdNew(squadid);
        taskInfo.setSquadId(squad);
        return taskInfo;
    }

    //查看任务中的子任务
    public List<ProjectSubtask> selectFinSubtask(Integer taskId){
        List<ProjectSubtask> FinSubtasks=FinProDao.selectFinSubtask(taskId);
        return FinSubtasks;
    }

    //查看任务中的任务日志记录
    public List<TaskLogRecord> selectFinTaskLogRecord(Integer taskId){
        List<TaskLogRecord> FinTaskLogRecords=FinProDao.selectFinTaskLogRecord(taskId);
        for(TaskLogRecord FinTaskLogRecord:FinTaskLogRecords){

            String squadid=FinTaskLogRecord.getSquadid();
            String squad=departmentNewMapper.selectSquadByIdNew(squadid);
            FinTaskLogRecord.setSquadid(squad);
        }
        return FinTaskLogRecords;
    }

    //查看上线待审批的任务开发日志
    public List<TaskDevelopLog> selectFinTaskDevRecord(Integer taskId){
        List<TaskDevelopLog> FinTaskDevRecords=FinProDao.selectFinTaskDevRecord(taskId);
        for(TaskDevelopLog FinTaskDevRecord:FinTaskDevRecords){
            String squadId=FinTaskDevRecord.getSquadid();
            String squad=departmentNewMapper.selectSquadByIdNew(squadId);
            FinTaskDevRecord.setSquadid(squad);
        }
        return FinTaskDevRecords;
    }

    //查看上线待审批任务的子任务开发日志
    public List<SubtaskDevelopLog> selectFinSubTaskDevRecord(Integer subtaskId){
        List<SubtaskDevelopLog> FinSubtaskDevRecords=FinProDao.selectFinSubtaskDevRecord(subtaskId);
        for(SubtaskDevelopLog FinSubtaskDevRecord:FinSubtaskDevRecords){
            String squadId=FinSubtaskDevRecord.getSquadid();
            String squad=departmentNewMapper.selectSquadByIdNew(squadId);
            FinSubtaskDevRecord.setSquadid(squad);
        }
        return FinSubtaskDevRecords;
    }

    //查看子任务日志记录
    public List<SubtaskLogRecord> selectFinSubtaskLogRecord(Integer subtaskId){
        List<SubtaskLogRecord> FinSubtaskLogRecords=FinProDao.selectFinSubtaskLogRecord(subtaskId);
        for(SubtaskLogRecord FinSubtaskLogRecord:FinSubtaskLogRecords){

            String squadid=FinSubtaskLogRecord.getSquadid();
            String squad=departmentNewMapper.selectSquadByIdNew(squadid);
            FinSubtaskLogRecord.setSquadid(squad);
        }
        return FinSubtaskLogRecords;
    }

    //查看子任务的详细信息
    public ProjectSubtask selectFinSubtaskInfo(Integer subtaskId){
        ProjectSubtask FinSubtaskInfo= FinProDao.selectFinSubtaskInfo(subtaskId);
        return FinSubtaskInfo;
    }
}
