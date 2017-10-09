package com.marketing.system.service.impl;


import com.marketing.system.entity.*;
import com.marketing.system.mapper_two.OnlineProMapper;
import com.marketing.system.service.OnlineProService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OnilneProServiceImpl implements OnlineProService{

    @Resource
    private OnlineProMapper OnProDao;

    //模糊查询所有待审批的项目
    public Map<String, Object> selectOnPro(Integer current,Integer pageSize,String creatersquadid,String creater,String createdate1,String createdate2,String finishdate1,String finishdate2,String protype,String param){


        Map<String,Object> onProMap=new HashMap<String,Object>();
        List<ProjectInfo> OnPro=OnProDao.selectOnPro(creatersquadid,creater,createdate1,createdate2,finishdate1,finishdate2,protype,param,current,pageSize);
        Integer OnProNum=OnProDao.selectOnProNum(creatersquadid,creater,createdate1,createdate2,finishdate1,finishdate2,protype,param);
        onProMap.put("OnPro",OnPro);
        onProMap.put("OnProNum",OnProNum);


        return onProMap;
    }

    //查看上线待审批项目的基本信息和项目信息
    public ProjectInfo selectOnProInfo(Integer id,Integer proId){

        ProjectInfo OnProInfo=OnProDao.selectOnProInfo(id);
        List<ProjectTask> OnTasks=OnProDao.selectOnTask(proId);
        int sum = 0;
        for (ProjectTask OnTask:OnTasks) {
            OnTask.getWorkDate();
            if (OnTask.getWorkDate() !="" && OnTask.getWorkDate() != null)
                sum +=Integer.valueOf(OnTask.getWorkDate());

        }
        OnProInfo.setWorkTatalDay(String.valueOf(sum));//项目预计工期（任务工期之和）
        return OnProInfo;
    }

    //查看上线待审批项目的日志记录
    public List<ProLogRecord> selectOnProLogRecord(Integer proId){
        List<ProLogRecord> OnProLogRecords=OnProDao.selectOnProLogRecord(proId);
        for(ProLogRecord OnProLogRecord:OnProLogRecords){
            String squadId=OnProLogRecord.getSquadid()+"";
            String squad=OnProDao.selectSquadById(squadId);
            OnProLogRecord.setSquadid(squad);
        }
        return OnProLogRecords;
    }

    //查看上线待审批项目的参与组
    public List<ProjectTask> selectOnTask(Integer proId){
        List<ProjectTask> OnTasks=OnProDao.selectOnTask(proId);
        for(int i=0;i<OnTasks.size();i++){
            String squadId=OnTasks.get(i).getSquadId();
            String squad=OnProDao.selectSquadById(squadId);
            OnTasks.get(i).setSquadId(squad);
        }
        return OnTasks;
    }

    //查看上线待审批项目的开发日志
    public List<ProDevelopLog> selectOnProDevRecord(Integer proId){
        List<ProDevelopLog> OnProDevRecords=OnProDao.selectOnProDevRecord(proId);
        for(ProDevelopLog OnProDevRecord:OnProDevRecords){
            String squadId=OnProDevRecord.getSquadid()+"";
            String squad=OnProDao.selectSquadById(squadId);
            OnProDevRecord.setSquadid(squad);
        }
        return OnProDevRecords;
    }

    //查看单条任务详细信息
    public ProjectTask selectOnTaskInfo(Integer taskId){
        ProjectTask taskInfo=OnProDao.selectOnTaskInfo(taskId);
        String squadid=taskInfo.getSquadId();
        String squad=OnProDao.selectSquadById(squadid);
        taskInfo.setSquadId(squad);
        return taskInfo;
    }

    //查看任务中的子任务
    public List<ProjectSubtask> selectOnSubtask(Integer taskId){
        List<ProjectSubtask> OnSubtasks=OnProDao.selectOnSubtask(taskId);
        return OnSubtasks;
    }

    //查看任务中的任务日志记录
    public List<TaskLogRecord> selectOnTaskLogRecord(Integer taskId){
        List<TaskLogRecord> OnTaskLogRecords=OnProDao.selectOnTaskLogRecord(taskId);
        for(TaskLogRecord OnTaskLogRecord:OnTaskLogRecords){
            String squadid=OnTaskLogRecord.getSquadid();
            String squad=OnProDao.selectSquadById(squadid);
            OnTaskLogRecord.setSquadid(squad);
        }
        return OnTaskLogRecords;
    }

    //查看上线待审批的任务开发日志
    public List<TaskDevelopLog> selectOnTaskDevRecord(Integer taskId){
        List<TaskDevelopLog> OnTaskDevRecords=OnProDao.selectOnTaskDevRecord(taskId);
        for(TaskDevelopLog OnTaskDevRecord:OnTaskDevRecords){
            String squadId=OnTaskDevRecord.getSquadid();
            String squad=OnProDao.selectSquadById(squadId);
            OnTaskDevRecord.setSquadid(squad);
        }
        return OnTaskDevRecords;
    }

    //查看上线待审批任务的子任务开发日志
    public List<SubtaskDevelopLog> selectOnSubTaskDevRecord(Integer subtaskId){
          List<SubtaskDevelopLog> OnSubtaskDevRecords=OnProDao.selectOnSubtaskDevRecord(subtaskId);
        for(SubtaskDevelopLog OnSubtaskDevRecord:OnSubtaskDevRecords){
            String squadId=OnSubtaskDevRecord.getSquadid();
            String squad=OnProDao.selectSquadById(squadId);
            OnSubtaskDevRecord.setSquadid(squad);
        }
        return OnSubtaskDevRecords;
    }

    //查看子任务日志记录
    public List<SubtaskLogRecord> selectOnSubtaskLogRecord(Integer subtaskId){
        List<SubtaskLogRecord> OnSubtaskLogRecords=OnProDao.selectOnSubtaskLogRecord(subtaskId);
        for(SubtaskLogRecord OnSubtaskLogRecord:OnSubtaskLogRecords){
            String squadid=OnSubtaskLogRecord.getSquadid();
            String squad=OnProDao.selectSquadById(squadid);
            OnSubtaskLogRecord.setSquadid(squad);
        }
        return OnSubtaskLogRecords;
    }

    //查看子任务的详细信息
    public ProjectSubtask selectOnSubtaskInfo(Integer subtaskId){
      ProjectSubtask OnSubtaskInfo= OnProDao.selectOnSubtaskInfo(subtaskId);
        return OnSubtaskInfo;
    }

    //通过操作，项目日志添加记录
    @Transactional
    public boolean insertProPassLog(Integer proid,String explain){
        String type="10";
        Date Date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=sdf.format(Date);
        String squadid="11";
        String emp="陈东和";
        String filepath=null;
        boolean success1=OnProDao.insertProPassLog(proid,type,date,squadid,emp,explain,filepath);
        boolean success2=OnProDao.updateProPassState(proid);
        return success1||success2;
    }

    //审批驳回操作，项目日志添加记录
    @Transactional
    public boolean insertProReturnLog(Integer proid,String explain){
        String type="8";
        Date Date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=sdf.format(Date);
        System.out.println("date--"+date);
        String squadid="11";
        String emp="陈东和";
        String filepath=null;
        boolean success1=OnProDao.insertProReturnLog(proid,type,date,squadid,emp,explain,filepath);
        boolean success2=OnProDao.updateProReturnState(proid);
        return success1||success2;
    }



}
