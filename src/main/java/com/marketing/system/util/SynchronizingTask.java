package com.marketing.system.util;

import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectSubtask;
import com.marketing.system.entity.ProjectTask;
import com.marketing.system.mapper_two.DayReportMapper;
import com.marketing.system.mapper_two.ProjectInfoMapper;
import org.apache.tools.ant.Project;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.TimerTask;

import static com.marketing.system.util.WeiXinPushUtil.httpPostWithJSON;

public class SynchronizingTask extends TimerTask{
    private ProjectTask Task;

    @Autowired
    private ProjectInfoMapper proInfoMapper;

    @Autowired
    private DayReportMapper DayReportDao;

    public SynchronizingTask(ProjectTask Task) {
        this.Task = Task;
    }

    @Override
    public void run() {
        System.out.println("进行延迟预警");

        Integer taskId=Task.getTaskId();
        //根据任务Id查找项目信息
        ProjectInfo pro = proInfoMapper.getProjectInfoByTaskId(taskId);
        Integer groupId=Integer.parseInt(Task.getSquadId());
        List<ProjectSubtask> subtasks=DayReportDao.getSubtaskByWXTimeOutPush(taskId);
        String postUrl1 = "";
        String postUrl2 = "";
        if(subtasks.size()==0){
            //该任务下面未分配子任务
            String PushDate = DateUtil.getYMDHMDate();
            Integer managerId=DayReportDao.getManagerIdByGroupId(groupId);
            String proName=DayReportDao.getProNameByTaskId(taskId);
            //推送给部门经理
            /*postUrl1 = "{\"Uid\":" + managerId + ",\"Content\":\"【延迟预警1级】\\n\\n《" +pro.getProname()+ "》需"+Task.getHandler()+"协助实施"+Task.getTaskname()+"工作，现已超过12小时未处理，请督促处理。"
                    + "\\n\\n任务分配:" + Task.getHandler()
                    + "\\n\\n任务名称:" + Task.getTaskname()
                    + "\\n\\n开始时间:" + Task.getSdate()
                    + "\\n\\n结束时间:" + Task.getEdate()
                    + "\\n\\n推送时间:" + PushDate
                    + "\",\"AgentId\":1000011,\"Title\":\"延迟预警\",\"Url\":\"\"}";*/

            //推送给郑洁
            postUrl2 = "{\"Uid\":" + 1340 + ",\"Content\":\"【延迟预警1级】\\n\\n《" +pro.getProname()+ "》需"+Task.getHandler()+"协助实施"+Task.getTaskname()+"工作，现已超过12小时未处理，请督促处理。"
                    + "\\n\\n任务分配:" + Task.getHandler()
                    + "\\n\\n任务名称:" + Task.getTaskname()
                    + "\\n\\n开始时间:" + Task.getSdate()
                    + "\\n\\n结束时间:" + Task.getEdate()
                    + "\\n\\n推送时间:" + PushDate
                    + "\",\"AgentId\":1000011,\"Title\":\"延迟预警\",\"Url\":\"\"}";
        }
        try {
            //消息推送-延迟预警
            String Str = httpPostWithJSON(postUrl1);
            System.out.println(Str);
            httpPostWithJSON(postUrl2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
