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

    //模糊查询所有回收站的项目
    public Map<String, Object> selectRecPro(Integer current, Integer pageSize, String creatersquadid, String creater, String createdate1, String createdate2, String plansdate1, String plansdate2, String protype, String param){


        System.out.println("1-"+creatersquadid+"2-"+creater+"3-"+createdate1+"4-"+createdate2+"5-"+plansdate1+"6-"+plansdate2+"7-"+protype+"8-"+param);
        Map<String,Object> recProMap=new HashMap<String,Object>();
        List<ProjectInfo> RecPro=RecProDao.selectRecPro(creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param,current,pageSize);
        Integer RecProNum=RecProDao.selectRecProNum(creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param);

        recProMap.put("RecPro",RecPro);
        recProMap.put("RecProNum",RecProNum);


        return recProMap;
    }

    //模糊查询回收站驳回的项目
    public Map<String, Object> selectRecProState5(Integer current, Integer pageSize, String creatersquadid, String creater, String createdate1, String createdate2, String plansdate1, String plansdate2, String protype, String param){

        Map<String,Object> recProMap=new HashMap<String,Object>();
        List<ProjectInfo> RecPro=RecProDao.selectRecProState5(creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param,current,pageSize);
        Integer RecProNum=RecProDao.selectRecProNumState5(creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param);

        recProMap.put("RecPro",RecPro);
        recProMap.put("RecProNum",RecProNum);


        return recProMap;
    }

    //模糊查询回收站作废的项目
    public Map<String, Object> selectRecProState6(Integer current, Integer pageSize, String creatersquadid, String creater, String createdate1, String createdate2, String plansdate1, String plansdate2, String protype, String param){

        Map<String,Object> recProMap=new HashMap<String,Object>();
        List<ProjectInfo> RecPro=RecProDao.selectRecProState6(creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param,current,pageSize);
        Integer RecProNum=RecProDao.selectRecProNumState6(creatersquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param);

        recProMap.put("RecPro",RecPro);
        recProMap.put("RecProNum",RecProNum);

        return recProMap;
    }

    //查看驳回项目的基本信息和项目信息
    public ProjectInfo selectRecProInfo(Integer id,Integer proId){
        ProjectInfo RecProInfo=RecProDao.selectRecProInfo(id);
        List<ProjectTask> RecTasks=RecProDao.selectRecTask(proId);
        Double sum = 0.0;
        for (ProjectTask RecTask:RecTasks) {
            RecTask.getWorkDate();
            if (RecTask.getWorkDate() !="" && RecTask.getWorkDate() != null)
                //sum +=Integer.valueOf(RecTask.getWorkDate());
                sum += Double.parseDouble(RecTask.getWorkDate());

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
            String squadId=RecTasks.get(i).getSquadId();
            String squad=RecProDao.selectSquadById(squadId);
            RecTasks.get(i).setSquadId(squad);
        }
        return RecTasks;
    }


}
