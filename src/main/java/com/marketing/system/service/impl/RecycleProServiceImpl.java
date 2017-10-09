package com.marketing.system.service.impl;

import com.marketing.system.entity.ProDevelopLog;
import com.marketing.system.entity.ProLogRecord;
import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectTask;
import com.marketing.system.mapper_two.RecycleProMapper;
import com.marketing.system.service.RecycleProService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecycleProServiceImpl implements RecycleProService{

    @Resource
    private RecycleProMapper RecProDao;

    //模糊查询所有待审批的项目
    public Map<String, Object> selectRecPro(Integer current, Integer pageSize, String creatersquadid, String creater, String createdate1, String createdate2, String plansdate1, String plansdate2, String protype, String param){
        if(creatersquadid==null){
            creatersquadid="";
        }
        if(creater==null){
            creater="";
        }
        if(createdate1==null||createdate1==""){
            createdate1="2010-01-01";
        }
        if(createdate2==null||createdate2==""){
            createdate2="2040-01-01";
        }
        if(plansdate1==null||plansdate1==""){
            plansdate1="2010-01-01";
        }
        if(plansdate2==null||plansdate2==""){
            plansdate2="2040-01-01";
        }
        if(protype==null){
            protype="";
        }
        if(param==null){
            param="";
        }

        System.out.println("1-"+creatersquadid+"2-"+creater+"3-"+createdate1+"4-"+createdate2+"5-"+plansdate1+"6-"+plansdate2+"7-"+protype+"8-"+param);
        Map<String,Object> recProMap=new HashMap<String,Object>();
        List<ProjectInfo> RecPro=RecProDao.selectRecPro(creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param,current,pageSize);
        Integer RecProNum=RecProDao.selectRecProNum(creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param);

        recProMap.put("RecPro",RecPro);
        recProMap.put("RecProNum",RecProNum);


        return recProMap;
    }

    //查看驳回项目的基本信息和项目信息
    public ProjectInfo selectRecProInfo(Integer id,Integer proId){
        ProjectInfo RecProInfo=RecProDao.selectRecProInfo(id);
        List<ProjectTask> RecTasks=RecProDao.selectRecTask(proId);
        int sum = 0;
        for (ProjectTask RecTask:RecTasks) {
            RecTask.getWorkDate();
            if (RecTask.getWorkDate() !="" && RecTask.getWorkDate() != null)
                sum +=Integer.valueOf(RecTask.getWorkDate());

        }
        RecProInfo.setWorkTatalDay(String.valueOf(sum));//项目预计工期（任务工期之和）
        return RecProInfo;
    }

    //查看驳回项目的日志记录
    public List<ProLogRecord> selectRecProLogRecord(Integer proId){
        List<ProLogRecord> RecProLogRecords=RecProDao.selectRecProLogRecord(proId);
        for(ProLogRecord RecProLogRecord:RecProLogRecords){
            String squadId=RecProLogRecord.getSquadid()+"";
            String squad=RecProDao.selectSquadById(squadId);
            RecProLogRecord.setSquadid(squad);
        }
        return RecProLogRecords;
    }

    //查看驳回项目的参与组
    public List<ProjectTask> selectRecTask(Integer proId){
        List<ProjectTask> RecTasks=RecProDao.selectRecTask(proId);
        for(int i=0;i<RecTasks.size();i++){
            String squadId=RecTasks.get(i).getSquadid();
            String squad=RecProDao.selectSquadById(squadId);
            RecTasks.get(i).setSquadid(squad);
        }
        return RecTasks;
    }


}
