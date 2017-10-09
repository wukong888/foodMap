package com.marketing.system.service.impl;

import com.marketing.system.mapper_two.ProReportMapper;
import com.marketing.system.service.ProReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProReportServiceImpl implements ProReportService{
    
    @Resource
    ProReportMapper ProReportDao;

    //模糊查询项目统计结果
    public List<Map> selectProReport(String creatersquadid,String creater, String createdate1, String createdate2,String finishdate1,String finishdate2,String onlinedate1,String onlinedate2
            ,String param) {

        List<String> Group=new ArrayList<String>();
        Group.add(0,"104");
        Group.add(1,"102");
        Group.add(2,"62");
        Group.add(3,"63");
        Group.add(4,"62");
        Group.add(5,"64");
        Group.add(6,"22");
        Group.add(7,"103");
        Group.add(8,"32");
        Group.add(9,"33");
        Group.add(10,"51");
        Group.add(11,"52");
        Group.add(12,"53");
        Group.add(13,"54");
        Group.add(14,"111");
        Group.add(15,"71");
        Group.add(16,"72");
        Group.add(17,"73");
        Group.add(18,"82");
        Group.add(19,"83");
        Group.add(20,"84");
        Group.add(21,"91");
        Group.add(22,"122");
        Group.add(23,"123");
        Group.add(24,"131");
        Group.add(25,"161");
        Group.add(26,"141");

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
        if(finishdate1==null||finishdate1==""){
            finishdate1="2010-01-01";
        }
        if(finishdate2==null||finishdate2==""){
            finishdate2="2040-01-01";
        }
        if(onlinedate1==null||onlinedate1==""){
            onlinedate1="2010-01-01";
        }
        if(onlinedate2==null||onlinedate2==""){
            onlinedate2="2040-01-01";
        }
        if(param==null){
            param="";
        }
        //产品查询
        List<Map> ProReport1 = ProReportDao.selectType1Sum(creatersquadid, creater, createdate1, createdate2, finishdate1, finishdate2, onlinedate1, onlinedate2, param);
        //活动查询
        List<Map> ProReport2 = ProReportDao.selectType2Sum(creatersquadid, creater, createdate1, createdate2, finishdate1, finishdate2, onlinedate1, onlinedate2, param);

        List<Map> ProReports=new ArrayList<Map>();

        for(String group:Group){
            Map<String,Object> ProReport=new HashMap<String,Object>();
            for(Map<String,Object> Report1:ProReport1){
                if(group.equals((String)Report1.get("squadd"))){
                    ProReport.put("squadId",group);
                    ProReport.put("sum1",(Integer)Report1.get("sum1"));
                    break;
                }
                ProReport.put("squadId",group);
                ProReport.put("sum1","");
            }
            String squad=ProReportDao.selectSquadBySquadId(group);
            ProReport.put("squad",squad);
            ProReports.add(ProReport);
        }

        for(Map<String,Object> ProReport:ProReports){
            for(Map<String,Object> Report2:ProReport2){
                if(((String)ProReport.get("squadId")).equals((String)Report2.get("squadd"))){
                    ProReport.put("sum2",(Integer)Report2.get("sum2"));
                    break;
                }
            }
            ProReport.put("sum2","");
        }

        return ProReports;
    }
}
