package com.marketing.system.mapper_two;

import com.marketing.system.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FinishProMapper {

    //模糊查询已完成归档的项目
    @Select("SELECT * FROM project_info where proState=4 and createrSquadId like '%${creatersquadid}%' and creater like '%${creater}%' and createDate >= #{createdate1} " +
            " and createDate <= #{createdate2} and finishDate >= #{finishdate1} and finishDate <= #{finishdate2} and onlineDate >= #{onlinedate1} and onlineDate <= #{onlinedate2} " +
            " and proType like '%${protype}%' and proName like '%${param}%' ORDER BY createDate desc  OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<ProjectInfo> selectFinPro(@Param("creatersquadid")String creatersquadid, @Param("creater")String creater, @Param("createdate1")String createdate1,
                                  @Param("createdate2")String createdate2, @Param("finishdate1")String finishdate1, @Param("finishdate2")String finishdate2,
                                   @Param("onlinedate1")String onlinedate1,@Param("onlinedate2")String onlinedate2,@Param("protype")String protype,
                                  @Param("param")String param, @Param("current")Integer current, @Param("pageSize")Integer pageSize);

    //模糊查询上线待审批的项目的数量
    @Select("SELECT count(1) FROM project_info where proState=4 and createrSquadId like '%${creatersquadid}%' and creater like '%${creater}%' and createDate >= #{createdate1} and createDate <= #{createdate2} " +
            "and finishDate >= #{finishdate1} and finishDate <= #{finishdate2} and onlineDate >= #{onlinedate1} and onlineDate <= #{onlinedate2} and proType like '%${protype}%' and proName like '%${param}%'")
    Integer selectFinProNum(@Param("creatersquadid")String creatersquadid,@Param("creater")String creater,@Param("createdate1")String createdate1,
                           @Param("createdate2")String createdate2,@Param("finishdate1")String finishdate1,@Param("finishdate2")String finishdate2,@Param("onlinedate1")String onlinedate1,
                            @Param("onlinedate2")String onlinedate2,@Param("protype")String protype,
                           @Param("param")String param);

    //根据部门Id查部门
    @Select("SELECT squad FROM projectManage.dbo.[group] where squadId =#{squadId}")
    String selectSquadById(@Param("squadId")String squadId);

    //查看归档项目的详细信息
    @Select("SELECT * FROM project_info where id=#{id}")
    ProjectInfo selectFinProInfo(@Param("id")Integer id);

    //查询项目的日志记录
    @Select("SELECT * FROM pro_LogRecord where proId=#{proId} ORDER BY Date asc")
    List<ProLogRecord> selectFinProLogRecord(@Param("proId")Integer proId);

    //查询项目的开发日志
    @Select("SELECT * FROM pro_DevelopLog where proId=#{proId} ORDER BY Date asc")
    List<ProDevelopLog> selectFinProDevRecord(@Param("proId")Integer proId);

    //查询项目的参与组
    @Select("SELECT * FROM project_task where proId=#{proId} ORDER BY sDate asc")
    List<ProjectTask> selectFinTask(@Param("proId")Integer proId);

    //查询项目中的任务详细信息
    @Select("SELECT * FROM project_task WHERE taskId=#{taskId}")
    ProjectTask selectFinTaskInfo(@Param("taskId")Integer taskId);

    //查询任务中的子任务
    @Select("SELECT * FROM project_subtask WHERE taskId=#{taskId}")
    List<ProjectSubtask> selectFinSubtask(@Param("taskId")Integer taskId);

    //查看任务的日志记录
    @Select("SELECT * FROM task_LogRecord where taskId=#{taskId} ORDER BY Date asc")
    List<TaskLogRecord> selectFinTaskLogRecord(@Param("taskId")Integer taskId);

    //查看任务的开发日志
    @Select("SELECT * FROM task_DevelopLog where taskId=#{taskId} ORDER BY Date asc")
    List<TaskDevelopLog> selectFinTaskDevRecord(@Param("taskId")Integer taskId);

    //查看子任务的开发日志
    @Select("SELECT * FROM subtask_DevelopLog WHERE subtaskId=#{subtaskId} ORDER BY Date asc")
    List<SubtaskDevelopLog> selectFinSubtaskDevRecord(@Param("subtaskId")Integer subtaskId);

    //查看子任务的详细信息
    @Select("SELECT * FROM project_subtask WHERE subtaskId=#{subtaskId}")
    ProjectSubtask selectFinSubtaskInfo(@Param("subtaskId")Integer subtaskId);

    //查看任务的日志记录
    @Select("SELECT * FROM subtask_LogRecord where subtaskId=#{subtaskId} ORDER BY Date asc")
    List<SubtaskLogRecord> selectFinSubtaskLogRecord(@Param("subtaskId")Integer subtaskId);
}
