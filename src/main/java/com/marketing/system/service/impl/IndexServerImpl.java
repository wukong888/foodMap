package com.marketing.system.service.impl;

import com.marketing.system.entity.Department;
import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectSubtask;
import com.marketing.system.entity.SystemUser;
import com.marketing.system.mapper_two.ProjectInfoMapper;
import com.marketing.system.mapper_two.ProjectSubtaskMapper;
import com.marketing.system.mapper_two.ProjectTaskMapper;
import com.marketing.system.service.IndexService;
import com.marketing.system.service.MyProjectService;
import com.marketing.system.util.MapUtil;
import com.marketing.system.util.StringUtil;
import com.marketing.system.util.ToolUtil;
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

    @Autowired
    private MyProjectService myProjectService;

    @Override
    public Integer getMyApplyProject(String name) {

        Integer i = projectInfoMapper.getMyApplyProject(name);

        if (i == null) {
            i = 0;
        }

        return i;
    }

    @Override
    public Integer getMyJoinProject(SystemUser user) {

        String userName = user.getUserName();//当前登录用户

        String department = user.getDepartment();

        department = department.substring(0, 2);

        //当前用户为组长/经理时，可以查看自己和其小组成员相关的项目
        Department did = myProjectService.getDepartmentIdByMent(department);
        String departmentid = did.getDepartmentid();

        //根据部门id查找小组id
        List<Map<String, Object>> mapList = myProjectService.getSquadId(String.valueOf(departmentid));

        String mentIds = StringUtil.toString(MapUtil.collectProperty(mapList, "squadId"));
        String[] mIds = mentIds.split(",");
        Map<String, Object> mapTid = new HashMap<>();

        mapTid.put("mentIds", mIds);
        //组长/经理其小组成员
        List<Map<String, Object>> mapList1 = myProjectService.getMembers(mapTid);

        String menuLeafIdsmember = StringUtil.toString(MapUtil.collectProperty(mapList1, "subtaskHandler"));

        String[] Idsmember = menuLeafIdsmember.split(",");

        Map<String, Object> mapTmem = new HashMap<>();

        mapTmem.put("menuLeafIds", Idsmember);

        List<Map<String, Object>> subtaskList = new ArrayList<>();

        if ((user.getDuty().contains("组长") || user.getDuty().contains("经理")) && !user.getDuty().equals("CEO")) {
            //当前登录用户并其成员包含所涉及子任务
            subtaskList = myProjectService.getSubTaskIdByHanderMap(mapTmem);
        } else {
            //当前登录用户所涉及子任务
            subtaskList = myProjectService.getSubTaskIdByHander(userName);
        }

        String menuLeafIds = StringUtil.toString(MapUtil.collectProperty(subtaskList, "taskId"));

        String[] Ids = menuLeafIds.split(",");

        Map<String, Object> mapT = new HashMap<>();

        mapT.put("menuLeafIds", Ids);

        //根据taskId查找proId
        List<Map<String, Object>> taskList = myProjectService.getproIdByTaskId(mapT);

        String menuLeafIds2 = StringUtil.toString(MapUtil.collectProperty(taskList, "proId"));

        String[] Ids2 = menuLeafIds2.split(",");

        Map<String, Object> mapTt = new HashMap<>();

        mapTt.put("menuLeafIds", Ids2);

        //根据proid查找项目list
        List<Map<String, Object>> proList = myProjectService.getProjectByProId(mapTt);

        Integer k = 0;
        if (taskList.size() > 0) {
            //4：完成，5：驳回，6：作废不在我的项目里显示
            proList = proList.stream().filter(t -> t.get("proState").equals("1") || t.get("proState").equals("2") || t.get("proState").equals("3") || t.get("proState").equals("7")).collect(Collectors.toList());

            k = proList.size();
            if (user.getDuty().equals("CEO")) {
                k = projectInfoMapper.getAllProjectSize();
            }
        } else {
            if (user.getDuty().equals("CEO")) {
                k = projectInfoMapper.getAllProjectSize();
            } else {
                k = projectInfoMapper.getMyApplyProject(userName);

            }
        }

        return k;
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
    public List<ProjectInfo> getProjectInfoList(Map<String,Object> map) throws ParseException {
        List<ProjectInfo> infoList = new ArrayList<>();
        if (String.valueOf(map.get("creater")) != "") {
            //查看自己的项目
            infoList = projectInfoMapper.getProjectInfoListNew(map);
        } else {
            //ceo
            infoList = projectInfoMapper.getProjectInfoListNullNew(map);
        }

        ProjectInfo projectInfo = new ProjectInfo();
        //循环计算距逾期时间天数
        for (int i = 0; i < infoList.size(); i++) {
            projectInfo = infoList.get(i);

            long betweenDays = ToolUtil.getBetweenTimes(projectInfo.getPlansdate());

            projectInfo.setBetweenDays(betweenDays);

        }
        //infoList = infoList.stream().filter(t -> Integer.valueOf(t.getProstate()) == 2).collect(Collectors.toList());

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

            long betweenDays = ToolUtil.getBetweenTimes(String.valueOf(map.get("edate")));

            map.put("betweenDays", betweenDays);
            infoListNew.add(map);

        }
        infoListNew = infoListNew.stream().filter(t -> Integer.valueOf(String.valueOf(t.get("taskstate"))) == 2).collect(Collectors.toList());

        //任务距离完成时间只有3天时间师即在首页提示
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

        }
        infoList = infoList.stream().filter(t -> Integer.valueOf(String.valueOf(t.getSubtaskstate())) == 2).collect(Collectors.toList());

        //子任务距离逾期时间只有1天时间时即在首页提示
        infoList = infoList.stream().filter(t -> Integer.valueOf(String.valueOf(t.getBetweenHours())) <= 24).collect(Collectors.toList());

        return infoList;
    }

    @Override
    public Integer getDevelopProjects(String creater) {

        Integer kf = 0;
        /*if (creater != "") {
            kf = projectInfoMapper.getDevelopProjects(creater);
        }else {
            kf = projectInfoMapper.getDevelopProjectsAll();
        }*/
        kf = projectInfoMapper.getDevelopProjectsAll();

        return kf;
    }

    @Override
    public Integer getLxProjects(String creater) {

        Integer lx = 0;
        /*if (creater != "") {
            lx = projectInfoMapper.getLxProjects(creater);
        }else {
            lx = projectInfoMapper.getLxProjectsAll();
        }*/
        lx = projectInfoMapper.getLxProjectsAll();

        return lx;
    }

    @Override
    public Integer getSxProjects(String creater) {

        Integer sx = 0;
        /*if (creater != "") {
            sx = projectInfoMapper.getSxProjects(creater);
        }else {
            sx = projectInfoMapper.getSxProjectsAll();
        }*/
        sx = projectInfoMapper.getSxProjectsAll();

        return sx;
    }


    @Override
    public Integer getHdDevelopProjects(String creater) {

        Integer kf = 0;
        /*if (creater != "") {
            kf = projectInfoMapper.getHdDevelopProjects(creater);
        }else {
            kf = projectInfoMapper.getHdDevelopProjectsAll();
        }*/
        kf = projectInfoMapper.getHdDevelopProjectsAll();

        return kf;
    }

    @Override
    public Integer getHdLxProjects(String creater) {

        Integer lx = 0;
        /*if (creater != "") {
            lx = projectInfoMapper.getHdLxProjects(creater);
        }else {
            lx = projectInfoMapper.getHdLxProjectsAll();
        }*/
        lx = projectInfoMapper.getHdLxProjectsAll();
        return lx;
    }

    @Override
    public Integer getHdSxProjects(String creater) {

        Integer sx = 0;
        /*if (creater != "") {
            sx = projectInfoMapper.getHdSxProjects(creater);
        }else {
            sx = projectInfoMapper.getHdSxProjectsAll();
        }*/
        sx = projectInfoMapper.getHdSxProjectsAll();

        return sx;
    }

    @Override
    public List<ProjectInfo> getZuZhangProjectInfos(Map<String, Object> map) throws ParseException {

        List<ProjectInfo> list = projectInfoMapper.getZuZhangProjectInfos(map);

        return list;
    }
}
