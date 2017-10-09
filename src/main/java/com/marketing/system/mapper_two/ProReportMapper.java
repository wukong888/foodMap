package com.marketing.system.mapper_two;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ProReportMapper {

    //查询产品类型的项目统计 createsquad,creater,createdate1,createdate2,finishdate1,finishdate2,plansdate1,plansdate2,protype,param
    @Select("select squadId as squadd,count(*) as sum1 from project_task join project_info on project_task.proId=project_info.proId where project_info.proType=1 and project_info.createrSquadId " +
            " like '%${creatersquadid}%' and project_info.creater like '%${creater}%' and project_info.createDate >= #{createdate1} and project_info.createDate <= #{createdate2} and project_info.finishDate >=#{finishdate1} and project_info.finishDate <= #{finishdate2} and " +
            " project_info.onlineDate >= #{onlinedate1} and project_info.onlineDate <=#{onlinedate2}  and project_info.proName like '%${param}%' GROUP BY project_task.squadId ")
    List<Map> selectType1Sum(@Param("creatersquadid")String creatersquadid,@Param("creater")String creater,@Param("createdate1")String createdate1,@Param("createdate2")String createdate2
            ,@Param("finishdate1")String finishdate1,@Param("finishdate2")String finishdate2,@Param("onlinedate1")String onlinedate1,@Param("onlinedate2")String onlinedate2,@Param("param")String param);


    //查询活动类型的项目统计 createsquad,creater,createdate1,createdate2,finishdate1,finishdate2,plansdate1,plansdate2,protype,param
    @Select("select squadId as squadd,count(*) as sum2 from project_task join project_info on project_task.proId=project_info.proId where project_info.proType=2 and project_info.createrSquadId " +
            " like '%${creatersquadid}%' and project_info.creater like '%${creater}%' and project_info.createDate >= #{createdate1} and project_info.createDate <= #{createdate2} and project_info.finishDate >=#{finishdate1} and project_info.finishDate <= #{finishdate2} and " +
            " project_info.onlineDate >= #{onlinedate1} and project_info.onlineDate <=#{onlinedate1}  and project_info.proName like '%${param}%' GROUP BY project_task.squadId ")
    List<Map> selectType2Sum(@Param("creatersquadid")String creatersquadid,@Param("creater")String creater,@Param("createdate1")String createdate1,@Param("createdate2")String createdate2
            ,@Param("finishdate1")String finishdate1,@Param("finishdate2")String finishdate2,@Param("onlinedate1")String onlinedate1,@Param("onlinedate2")String onlinedate2,@Param("param")String param);

    //根据部门部门id查询部门
    @Select("select squad from projectManage.dbo.[group] where squadId= #{squadId}")
    String selectSquadBySquadId(@Param("squadId")String squadId);

}
