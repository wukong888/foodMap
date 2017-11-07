package com.marketing.system.mapper_two;

import com.marketing.system.entity.ProDevelopLog;
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
    @Select("select taskName,handler,taskState,taskProgress,taskId from project_task  where proId=#{proId} order by eDate asc OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
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
    @Select("select subtaskName,subtaskHandler,subtaskState,subtaskProgress,subtaskId from project_subtask where  taskId=#{taskId} order by sDate asc OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
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

    //根据部门Id查询部门
    @Select("SELECT squad FROM projectManage.dbo.[group] where squadId =#{squadId} ")
    String selectSquadById(@Param("squadId") String squadId);

    //任务初始化
    @Select("select proName,proId from project_info where proState=1 or proState=2 or proState=3" +
            " or (proState=4 and finishDate >=#{startDate} and finishDate <= #{endDate}) or (proState=5 and rejectDate  >=#{startDate} and rejectDate <= #{endDate})" +
            " or (proState=6 and cancelDate  >=#{startDate} and cancelDate <= #{endDate})")
    List<Map> initTask(@Param("startDate") String startDate, @Param("endDate") String endDate);

    //子任务初始化
    @Select("select taskName,taskId from project_task where proId=#{proId}")
    List<Map> initSubtask(@Param("proId") Integer proId);
}
