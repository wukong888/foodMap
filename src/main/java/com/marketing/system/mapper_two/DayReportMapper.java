package com.marketing.system.mapper_two;

import com.marketing.system.entity.ProDevelopLog;
import com.marketing.system.entity.ProjectSubtask;
import com.marketing.system.entity.ProjectTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface DayReportMapper {

    //查询项目日报记录
    @Select("select proName,creater,proState,proProgress,proDevelopLogId,proId,rejectDate,cancelDate from project_info where proState=1 or proState=2 or proState=3 " +
            " or (proState=4 and finishDate >=#{startDate} and finishDate <= #{endDate}) or (proState=5 and rejectDate  >=#{startDate} and rejectDate <= #{endDate}) " +
            " or (proState=6 and cancelDate  >=#{startDate} and cancelDate <= #{endDate}) order by createDate asc OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> selectProReport(@Param("current") Integer current, @Param("pageSize") Integer pageSize, @Param("startDate") String startDate, @Param("endDate") String endDate);

    //查询项目日报记录
    @Select("select proName,creater,proState,proProgress,proDevelopLogId,proId,rejectDate,cancelDate from project_info where proId = #{proId} and (proState=1 or proState=2 or proState=3 " +
            " or (proState=4 and finishDate >=#{startDate} and finishDate <= #{endDate}) or (proState=5 and rejectDate  >=#{startDate} and rejectDate <= #{endDate}) " +
            " or (proState=6 and cancelDate  >=#{startDate} and cancelDate <= #{endDate}))  order by createDate asc OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> selectProReport1(@Param("current") Integer current, @Param("pageSize") Integer pageSize, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("proId") Integer proId);

    //查询全部项目日报记录-不分页
    @Select("select proName,creater,proState,proProgress,proDevelopLogId,proId,rejectDate,cancelDate from project_info where proState=1 or proState=2 or proState=3 " +
            " or (proState=4 and finishDate >=#{startDate} and finishDate <= #{endDate}) or (proState=5 and cancelDate  >=#{startDate} and cancelDate <= #{endDate}) " +
            " order by createDate asc ")
    List<Map> exportProExcel(@Param("startDate") String startDate, @Param("endDate") String endDate);

    //查询项目日报记录数
    @Select("select count(1) from project_info where proState=1 or proState=2 or proState=3 or (proState=4 and finishDate >=#{startDate} and " +
            "finishDate <= #{endDate}) or (proState=5 and cancelDate  >=#{startDate} and cancelDate <= #{endDate})")
    Integer selectProReportNum(@Param("startDate") String startDate, @Param("endDate") String endDate);

    //查询项目日报的动态记录
    @Select("select * from pro_DevelopLog where proId=#{proId} and Date>=#{startDate} and Date<=#{endDate}")
    List<Map> selectProLogByProId(@Param("proId") Integer proId, @Param("startDate") String startDate, @Param("endDate") String endDate);


    //查询任务日报记录
    @Select("select taskName,handler,taskState,taskProgress,taskId,proId from project_task  where proId=#{proId} order by eDate asc OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> selectTaskReport(@Param("current") Integer current, @Param("pageSize") Integer pageSize, @Param("proId") Integer proId);

    //查询任务日报记录
    @Select("select taskName,handler,taskState,taskProgress,taskId from project_task  where taskId=#{taskId} order by eDate asc OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> selectTaskReportByTaskId(@Param("current") Integer current, @Param("pageSize") Integer pageSize, @Param("taskId") Integer taskId);

    //查询全部任务日报记录-不分页
    @Select("select taskName,handler,taskState,taskProgress,taskId from project_task  where proId=#{proId} order by sDate asc ")
    List<Map> exportTaskExcel(@Param("proId") Integer proId);

    //查询模糊查询任务日报记录日报记录数
    @Select("select count(1) from project_task  where proId=#{proId} ")
    Integer selectTaskReportNum(@Param("proId") Integer proId);

    //查询任务日报的动态记录
    @Select("select * from task_DevelopLog where taskId=#{taskId} and task_DevelopLog.[Date] between #{startDate} and #{endDate}")
    List<Map> selectTaskLogById(@Param("taskId") Integer taskId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    //查询子任务日报记录
    @Select("select subtaskName,subtaskHandler,subtaskState,subtaskProgress,subtaskId,taskId from project_subtask where  taskId=#{taskId} order by sDate asc OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> selectSubtaskReport(@Param("current") Integer current, @Param("pageSize") Integer pageSize, @Param("taskId") Integer taskId);

    //查询全部子任务日报记录-不分页
    @Select("select subtaskName,subtaskHandler,subtaskState,subtaskProgress,subtaskId from project_subtask where taskId=#{taskId} order by sDate asc ")
    List<Map> exportSubtaskExcel(@Param("taskId") Integer taskId);

    //查询模糊查询子任务日报记录日报记录数
    @Select("select count(1) from project_subtask  where sDate >=#{startDate} and sDate <=#{endDate}")
    Integer selectSubtaskReportNum(@Param("current") Integer current, @Param("pageSize") Integer pageSize, @Param("startDate") String startDate, @Param("endDate") String endDate);

    //查询子任务日报的动态记录
    @Select("select * from subtask_DevelopLog where subtaskId=#{subtaskId} and Date >=#{startDate} and Date <=#{endDate}")
    List<Map> selectSubtaskLogById(@Param("subtaskId") Integer subtaskId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    //根据任务id查找项目id
    @Select("SELECT proId FROM project_task WHERE taskId=#{taskId}")
    Integer getProIdByTaskId(@Param("taskId")Integer taskId);

    //任务初始化
    @Select("select proName,proId from project_info where proState=1 or proState=2 or proState=3" +
            " or (proState=4 and finishDate >=#{startDate} and finishDate <= #{endDate}) or (proState=5 and rejectDate  >=#{startDate} and rejectDate <= #{endDate})" +
            " or (proState=6 and cancelDate  >=#{startDate} and cancelDate <= #{endDate})")
    List<Map> initTask(@Param("startDate") String startDate, @Param("endDate") String endDate);

    //子任务初始化
    @Select("select taskName,taskId from project_task where proId=#{proId}")
    List<Map> initSubtask(@Param("proId") Integer proId);

    //保存项目日报的相关信息
    @Insert("INSERT INTO day_proReport (proId,proName,creater,proType,proProgress,proDyn,dynDate ) VALUES " +
            "(#{proId},#{proName},#{creater},#{proState},#{proProgres},#{proDyn},#{DATE})" )
    Boolean saveProDayReport(@Param("proId")Integer proId,@Param("proName")String proName,@Param("creater")String creater,@Param("proState")String proState,
                             @Param("proProgres")String proProgres,@Param("proDyn")String proDyn,@Param("DATE")String DATE);

    //保存任务日报的相关信息
    @Insert("INSERT INTO day_taskReport (taskId,taskName,handler,taskType,taskProgress,taskDyn,dynDate,proId ) VALUES " +
            "(#{taskId},#{taskName},#{handler},#{taskState},#{taskProgress},#{taskDyn},#{DATE},#{proId})" )
    Boolean saveTaskDayReport(@Param("taskId")Integer taskId,@Param("taskName")String taskName,@Param("handler")String handler,@Param("taskState")String taskState,
                              @Param("taskProgress")String taskProgress,@Param("taskDyn")String taskDyn,@Param("DATE")String DATE,@Param("proId")Integer proId);

    //保存子任务日报的相关信息
    @Insert("INSERT INTO day_subtaskReport (subtaskId,subtaskName,handler,subtaskType,subtaskProgress,subtaskDyn,dynDate,proId,taskId ) VALUES " +
            "(#{subtaskId},#{subtaskName},#{subtaskHandler},#{subtaskState},#{subtaskProgress},#{subtaskDyn},#{DATE},#{proId},#{taskId})" )
    Boolean saveSubtaskDayReport(@Param("subtaskId")Integer subtaskId,@Param("subtaskName")String subtaskName,@Param("subtaskHandler")String subtaskHandler,@Param("subtaskState")String subtaskState,
                                 @Param("subtaskProgress")String subtaskProgress,@Param("subtaskDyn")String subtaskDyn,@Param("DATE")String DATE,@Param("proId")Integer proId,@Param("taskId")Integer taskId);

    //获取项目日报信息
    @Select("SELECT * FROM day_proReport WHERE dynDate = #{reportDate} ORDER BY proId ASC OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> getProDayReportInfos(@Param("reportDate")String reportDate,@Param("current")Integer current,@Param("pageSize")Integer pageSize);

    //获取项目日报信息总记录数
    @Select("SELECT count(1) FROM day_proReport WHERE dynDate = #{reportDate} ")
    Integer getProDayReportInfosCount(@Param("reportDate")String reportDate);

    //获取任务日报信息
    @Select("SELECT * FROM day_taskReport WHERE dynDate = #{reportDate} ORDER BY taskId ASC OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> getTaskDayReportInfos(@Param("reportDate")String reportDate,@Param("current")Integer current,@Param("pageSize")Integer pageSize);

    //获取项目日报信息总记录数
    @Select("SELECT count(1) FROM day_taskReport WHERE dynDate = #{reportDate} ")
    Integer getTaskDayReportInfosCount(@Param("reportDate")String reportDate);

    //获取任务日报信息-proId
    @Select("SELECT * FROM day_taskReport WHERE dynDate = #{reportDate} AND proId=#{proId} ORDER BY taskId ASC OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> getTaskDayReportInfos1(@Param("reportDate")String reportDate,@Param("current")Integer current,@Param("pageSize")Integer pageSize,@Param("proId")Integer proId);

    //获取项目日报信息总记录数-proId
    @Select("SELECT count(1) FROM day_taskReport WHERE dynDate = #{reportDate} AND proId=#{proId}")
    Integer getTaskDayReportInfosCount1(@Param("reportDate")String reportDate,@Param("proId")Integer proId);

    //获取子任务日报信息
    @Select("SELECT * FROM day_subtaskReport WHERE dynDate = #{reportDate} ORDER BY subtaskId ASC OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> getSubaskDayReportInfos(@Param("reportDate")String reportDate,@Param("current")Integer current,@Param("pageSize")Integer pageSize);

    //获取子项目日报信息总记录数
    @Select("SELECT count(1) FROM day_subtaskReport WHERE dynDate = #{reportDate} ")
    Integer getSubtaskDayReportInfosCount(@Param("reportDate")String reportDate);

    //获取子任务日报信息-proId
    @Select("SELECT * FROM day_subtaskReport WHERE dynDate = #{reportDate} AND proId=#{proId} ORDER BY subtaskId ASC OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> getSubtaskDayReportInfos1(@Param("reportDate")String reportDate,@Param("current")Integer current,@Param("pageSize")Integer pageSize,@Param("proId")Integer proId);

    //获取子项目日报信息总记录数-proId
    @Select("SELECT count(1) FROM day_subtaskReport WHERE dynDate = #{reportDate} AND proId=#{proId}")
    Integer getSubtaskDayReportInfosCount1(@Param("reportDate")String reportDate,@Param("proId")Integer proId);

    //获取子任务日报信息-taskId
    @Select("SELECT * FROM day_subtaskReport WHERE dynDate = #{reportDate} AND taskId=#{taskId} ORDER BY subtaskId ASC OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map> getSubtaskDayReportInfos2(@Param("reportDate")String reportDate,@Param("current")Integer current,@Param("pageSize")Integer pageSize,@Param("taskId")Integer taskId);

    //获取子项目日报信息总记录数-taskId
    @Select("SELECT count(1) FROM day_subtaskReport WHERE dynDate = #{reportDate} AND taskId=#{taskId}")
    Integer getSubtaskDayReportInfosCount2(@Param("reportDate")String reportDate,@Param("taskId")Integer taskId);

    //查找所有未完成，驳回，逾期项目里的任务
    @Select("SELECT a.* FROM [dbo].[project_task] a JOIN project_info b ON a.proId=b.proId WHERE proState=2 OR proState=5 OR proState=7")
    List<ProjectTask> getTaskByWXTimeOutPush();

    //根据任务id查找任务里面是否有子任务
    @Select("SELECT * FROM project_subtask  WHERE taskId=#{taskId}")
    List<ProjectSubtask> getSubtaskByWXTimeOutPush(@Param("taskId")Integer taskId);

    //根据小组id查找部门经理id
    @Select("SELECT TOP (1) [id] FROM [WS_Admin].[dbo].[SystemUser] where  UserGroupId=(select pid from [WS_Admin].[dbo].[Department_new] where id=#{groupId} )")
    Integer getManagerIdByGroupId(@Param("groupId")Integer groupId);

    //根据任务id查找项目名称
    @Select("select a.proName from projectManage.dbo.project_info a join projectManage.dbo.project_task b on a.proId=b.proId where b.taskId=#{taskId}")
    String getProNameByTaskId(@Param("taskId")Integer taskId);
}
