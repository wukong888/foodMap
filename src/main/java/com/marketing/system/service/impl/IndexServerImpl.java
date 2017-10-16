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
import java.util.stream.Collectors;

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

        //项目
        Integer k = projectInfoMapper.getMyJoinProject(name);

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

        return i + j + k;
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
        List<ProjectInfo> infoListNew = new ArrayList<>();
        List<ProjectInfo> infoList = new ArrayList<>();
        if (creater != "") {
            infoList = projectInfoMapper.getProjectInfoList(creater);
        } else {
            infoList = projectInfoMapper.getProjectInfoListNull();
        }

        ProjectInfo projectInfo = new ProjectInfo();
        //循环计算距逾期时间天数
        for (int i = 0; i < infoList.size(); i++) {
            projectInfo = infoList.get(i);
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
            long betweenDays = ((time2 - time1) / (1000 * 3600 * 24));

            projectInfo.setBetweenDays(betweenDays);

        }

        infoList = infoList.stream().filter(t -> t.getBetweenDays() <= 6).collect(Collectors.toList());


        return infoList;
    }

    @Override
    public List<Map<String, Object>> getProjectTaskList(String creater) throws ParseException {

        List<Map<String, Object>> infoList = new ArrayList<>();
        List<Map<String, Object>> infoListNew = new ArrayList<>();

        if (creater != "") {
            infoList = projectInfoMapper.getProjectTaskList(creater);
        } else {
            infoList = projectInfoMapper.getProjectTaskListNull();
        }
        Map<String, Object> map = new HashMap<>();
        //循环计算距逾期时间天数
        for (int i = 0; i < infoList.size(); i++) {
            map = infoList.get(i);
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

            map.put("betweenDays", betweenDays);
            infoListNew.add(map);
            //任务距离完成时间只有3天时间师即在首页提示
            /*if (Integer.valueOf(String.valueOf(map.get("betweenDays"))) > 3) {
                infoListNew.remove(map);
            }*/
        }
        infoListNew = infoListNew.stream().filter(t -> Integer.valueOf(String.valueOf(t.get("betweenDays"))) <= 3).collect(Collectors.toList());
        return infoListNew;
    }

    @Override
    public List<ProjectSubtask> getProjectSubTaskList(String creater) throws ParseException {

        List<ProjectSubtask> infoList = new ArrayList<>();

        if (creater != "") {
            infoList = projectInfoMapper.getProjectSubTaskList(creater);
        } else {
            infoList = projectInfoMapper.getProjectSubTaskListNull();
        }

        ProjectSubtask projectSubtask = new ProjectSubtask();
        //循环计算距逾期时间天数
        for (int i = 0; i < infoList.size(); i++) {
            projectSubtask = infoList.get(i);
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
            /*if (projectSubtask.getBetweenHours() > 24) {
                infoList.remove(projectSubtask);
            }*/
        }

        infoList = infoList.stream().filter(t -> Integer.valueOf(String.valueOf(t.getBetweenHours())) <= 24).collect(Collectors.toList());

        return infoList;
    }
}
