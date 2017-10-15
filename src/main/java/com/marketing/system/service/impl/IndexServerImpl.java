package com.marketing.system.service.impl;

import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectSubtask;
import com.marketing.system.mapper_two.ProjectInfoMapper;
import com.marketing.system.mapper_two.ProjectSubtaskMapper;
import com.marketing.system.mapper_two.ProjectTaskMapper;
import com.marketing.system.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IndexServerImpl implements IndexService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private ProjectTaskMapper projectTaskMapper;

    @Autowired
    private ProjectSubtaskMapper projectSubtaskMapper;

    @Override
    public Integer getMyApplyProject(String name) {

        Integer i = projectInfoMapper.getMyApplyProject(name);

        if (i == null) {
            i = 0;
        }

        return i;
    }

    @Override
    public Integer getMyJoinProject(String name) {

        //项目任务
        Integer i = projectTaskMapper.getMyJoinProject(name);
        if (i == null) {
            i = 0;
        }

        //子任务
        Integer j = projectSubtaskMapper.getMyJoinProject(name);
        if (j == null) {
            j = 0;
        }

        return i + j;
    }

    @Override
    public Integer getUpApplyProject(String prostate) {

        Integer i = projectInfoMapper.getUpApplyProject(prostate);

        if (i == null) {
            i = 0;
        }

        return i;
    }

    @Override
    public List<ProjectInfo> getProjectInfoList(String creater) throws ParseException {
        List<ProjectInfo> infoList = new ArrayList<>();
        if (creater != "") {
           infoList = projectInfoMapper.getProjectInfoList(creater);
        } else {
            infoList = projectInfoMapper.getProjectInfoListNull();
        }

        //循环计算距逾期时间天数
        for (int i = 0; i < infoList.size(); i++) {
            for (ProjectInfo projectInfo : infoList) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date plansdate = sdf.parse(projectInfo.getPlansdate());//预计完成时间

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(plansdate);
                //当前时间
                Date smdate = new Date();

                smdate = sdf.parse(sdf.format(smdate));
                Calendar cal = Calendar.getInstance();
                cal.setTime(smdate);
                long time1 = cal.getTimeInMillis();
                cal.setTime(calendar.getTime());
                long time2 = cal.getTimeInMillis();
                String betweenDays = ((time2 - time1) / (1000 * 3600 * 24))+"";

                projectInfo.setBetweenDays(Integer.valueOf(betweenDays));

                //项目距离完成只有6天时间时即在首页提示
                if (projectInfo.getBetweenDays() < 6 || projectInfo.getBetweenDays() == 6) {
                    infoList.set(i,projectInfo);
                }
            }

        }

        return infoList;
    }

    @Override
    public List<Map<String, Object>> getProjectTaskList(String creater) throws ParseException {

        List<Map<String, Object>> infoList = new ArrayList<>();

        if (creater != "") {
            infoList = projectInfoMapper.getProjectTaskList(creater);
        } else {
            infoList = projectInfoMapper.getProjectTaskListNull();
        }
        //循环计算距逾期时间天数
        for (int i = 0; i < infoList.size(); i++) {
            for (Map map : infoList) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date edate = sdf.parse(String.valueOf(map.get("edate")));//预计完成时间

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(edate);
                //当前时间
                Date smdate = new Date();

                smdate = sdf.parse(sdf.format(smdate));
                Calendar cal = Calendar.getInstance();
                cal.setTime(smdate);
                long time1 = cal.getTimeInMillis();
                cal.setTime(calendar.getTime());
                long time2 = cal.getTimeInMillis();
                long betweenDays = (time2 - time1) / (1000 * 3600 * 24);

                map.put("betweenDays",betweenDays);

                //任务距离完成时间只有3天时间师即在首页提示
                if (Integer.valueOf(String.valueOf(map.get("betweenDays"))) < 3 || Integer.valueOf(String.valueOf(map.get("betweenDays"))) == 3) {
                    infoList.set(i,map);
                }
            }

        }

        return infoList;
    }

    @Override
    public List<ProjectSubtask> getProjectSubTaskList(String creater) throws ParseException {

        List<ProjectSubtask> infoList = new ArrayList<>();

        if (creater != "") {
            infoList = projectInfoMapper.getProjectSubTaskList(creater);
        } else {
            infoList = projectInfoMapper.getProjectSubTaskListNull();
        }

        //循环计算距逾期时间天数
        for (int i = 0; i < infoList.size(); i++) {
            for (ProjectSubtask projectSubtask : infoList) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date edate = sdf.parse(projectSubtask.getEdate());//预计完成时间

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(edate);
                //当前时间
                Date smdate = new Date();

                smdate = sdf.parse(sdf.format(smdate));
                Calendar cal = Calendar.getInstance();
                cal.setTime(smdate);
                long time1 = cal.getTimeInMillis();
                cal.setTime(calendar.getTime());
                long time2 = cal.getTimeInMillis();
                long betweenHours = (time2 - time1) / (1000 * 3600);

                projectSubtask.setBetweenHours(betweenHours);

                //子任务距离逾期时间只有1天时间时即在首页提示
                if (projectSubtask.getBetweenHours() < 24 || projectSubtask.getBetweenHours() == 24) {
                    infoList.set(i,projectSubtask);
                }
            }

        }

        return infoList;
    }
}
