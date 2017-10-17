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
        Group.add(4,"64");
        Group.add(5,"22");
        Group.add(6,"103");
        Group.add(7,"32");
        Group.add(8,"33");
        Group.add(9,"51");
        Group.add(10,"52");
        Group.add(11,"53");
        Group.add(12,"54");
        Group.add(13,"111");
        Group.add(14,"71");
        Group.add(15,"72");
        Group.add(16,"73");
        Group.add(17,"82");
        Group.add(18,"83");
        Group.add(19,"84");
        Group.add(20,"91");
        Group.add(21,"122");
        Group.add(22,"123");
        Group.add(23,"131");
        Group.add(24,"161");
        Group.add(25,"141");

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

        /*for(Map<String,Object> ProReport:ProReports){
            for(Map<String,Object> Report2:ProReport2){
                if(((String)ProReport.get("squadId")).equals((String)Report2.get("squadd"))){
                    ProReport.put("sum2",(Integer)Report2.get("sum2"));
                    break;
                }
            }
            ProReport.put("sum2","");
        }*/
        for(int i=0;i<ProReports.size();i++){

            for (int j=0;j<ProReport2.size();j++){
                String squadId1=(String) ProReports.get(i).get("squadId");
                String squadId2=(String)ProReport2.get(j).get("squadd");
                if(squadId1.equals(squadId2)){
                    System.out.println("---------");
                }
                if(ProReports.get(i).get("squadId").equals(ProReport2.get(j).get("squadd"))){
                    ProReports.get(i).put("sum2",ProReport2.get(j).get("sum2"));
                    break;
                }else{
                    ProReports.get(i).put("sum2","");
                }
            }

        }

        return ProReports;
    }
}
