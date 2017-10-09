package com.marketing.system.mapper_two;

import com.marketing.system.entity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface OnlineProMapper {

    //模糊查询上线待审批的项目
    @Select("SELECT * FROM project_info where proState=3 and createrSquadId like '%${creatersquadid}%' and creater like '%${creater}%' and createDate >= #{createdate1} " +
            " and createDate <= #{createdate2} and finishDate >= #{finishdate1} and finishDate <= #{finishdate2} and proType like '%${protype}%' and proName like '%${param}%'" +
            " ORDER BY planSDate asc  OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<ProjectInfo> selectOnPro(@Param("creatersquadid")String creatersquadid,@Param("creater")String creater,@Param("createdate1")String createdate1,
    @Param("createdate2")String createdate2,@Param("finishdate1")String finishdate1,@Param("finishdate2")String finishdate2,@Param("protype")String protype,
    @Param("param")String param,@Param("current")Integer current,@Param("pageSize")Integer pageSize);

    //模糊查询上线待审批的项目的数量
    @Select("SELECT count(1) FROM project_info where proState=3 and createrSquadId like '%${creatersquadid}%' and creater like '%${creater}%' and " +
            "createDate >= #{createdate1} and createDate <= #{createdate2} and finishDate >= #{finishdate1} and finishDate <= #{finishdate2} and " +
            "proType like '%${protype}%' and proName like '%${param}%'")
    Integer selectOnProNum(@Param("creatersquadid")String creatersquadid,@Param("creater")String creater,@Param("createdate1")String createdate1,
                           @Param("createdate2")String createdate2,@Param("finishdate1")String finishdate1,@Param("finishdate2")String finishdate2,@Param("protype")String protype,
                           @Param("param")String param);

    //根据部门Id查部门
    @Select("SELECT squad FROM projectManage.dbo.[group] where squadId =#{squadId}")
    String selectSquadById(@Param("squadId")String squadId);

    //查看上线待审批项目的详细信息
    @Select("SELECT * FROM project_info where id=#{id}")
    ProjectInfo selectOnProInfo(@Param("id")Integer id);

    //查询项目的日志记录
    @Select("SELECT * FROM pro_LogRecord where proId=#{proId} ORDER BY Date asc")
    List<ProLogRecord> selectOnProLogRecord(@Param("proId")Integer proId);

    //查询项目的开发日志
    @Select("SELECT * FROM pro_DevelopLog where proId=#{proId} ORDER BY Date asc")
    List<ProDevelopLog> selectOnProDevRecord(@Param("proId")Integer proId);

    //查询项目的参与组
    @Select("SELECT * FROM project_task where proId=#{proId} ORDER BY sDate asc")
    List<ProjectTask> selectOnTask(@Param("proId")Integer proId);

    //查询项目中的任务详细信息
    @Select("SELECT * FROM project_task WHERE taskId=#{taskId}")
    ProjectTask selectOnTaskInfo(@Param("taskId")Integer taskId);

    //查询任务中的子任务
    @Select("SELECT * FROM project_subtask WHERE taskId=#{taskId}")
    List<ProjectSubtask> selectOnSubtask(@Param("taskId")Integer taskId);

    //查看任务的日志记录
    @Select("SELECT * FROM task_LogRecord where taskId=#{taskId} ORDER BY Date asc")
    List<TaskLogRecord> selectOnTaskLogRecord(@Param("taskId")Integer taskId);

    //查看任务的开发日志
    @Select("SELECT * FROM task_DevelopLog where taskId=#{taskId} ORDER BY Date asc")
    List<TaskDevelopLog> selectOnTaskDevRecord(@Param("taskId")Integer taskId);

    //查看子任务的开发日志
    @Select("SELECT * FROM subtask_DevelopLog WHERE subtaskId=#{subtaskId} ORDER BY Date asc")
    List<SubtaskDevelopLog> selectOnSubtaskDevRecord(@Param("subtaskId")Integer subtaskId);

    //查看子任务的详细信息
    @Select("SELECT * FROM project_subtask WHERE subtaskId=#{subtaskId}")
    ProjectSubtask selectOnSubtaskInfo(@Param("subtaskId")Integer subtaskId);

    //查看任务的日志记录
    @Select("SELECT * FROM subtask_LogRecord where subtaskId=#{subtaskId} ORDER BY Date asc")
    List<SubtaskLogRecord> selectOnSubtaskLogRecord(@Param("subtaskId")Integer subtaskId);

    //项目通过功能，项目日志记录增加一条日志记录
    @Insert("INSERT INTO pro_LogRecord (proId,type,Date,squadId,Emp,explain,filePath) values (#{proid},#{type},#{date},#{squadid},#{emp},#{explain},#{filepath})")
    boolean insertProPassLog(@Param("proid")Integer proid,@Param("type")String type,@Param("date")String date,@Param("squadid")String squadid,@Param("emp")String emp,
                             @Param("explain")String explain,@Param("filepath")String filepath);

    //项目上线审批通过，更改项目状态
    @Update("UPDATE project_info SET proState=4 WHERE proId=#{proId}")
    boolean updateProPassState(@Param("proId")Integer proId);

    //项目驳回功能，项目日志记录增加一条日志记录
    @Insert("INSERT INTO pro_LogRecord (proId,type,Date,squadId,Emp,explain,filePath) values (#{proid},#{type},#{date},#{squadid},#{emp},#{explain},#{filepath})")
    boolean insertProReturnLog(@Param("proid")Integer proid,@Param("type")String type,@Param("date")String date,@Param("squadid")String squadid,@Param("emp")String emp,
                             @Param("explain")String explain,@Param("filepath")String filepath);

    //项目上线审批通过，更改项目状态
    @Update("UPDATE project_info SET proState=5 WHERE proId=#{proId}")
    boolean updateProReturnState(@Param("proId")Integer proId);


}
