package com.marketing.system.mapper_two;

import com.marketing.system.entity.ProDevelopLog;
import com.marketing.system.entity.ProLogRecord;
import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.ProjectTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RecycleProMapper {

    //模糊查询回收站的项目
    @Select("SELECT * FROM project_info where (proState=5 or proState=6) and createrSquadId like '%${creatersquadid}%' and creater like '%${creater}%' and createDate >= #{createdate1} " +
            " and createDate <= #{createdate2} and planSDate >= #{plansdate1} and planSDate <= #{plansdate2} and proType like '%${protype}%' and proName like '%${param}%'" +
            " ORDER BY createDate desc  OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<ProjectInfo> selectRecPro(@Param("creatersquadid")String creatersquadid, @Param("creater")String creater, @Param("createdate1")String createdate1,
                                  @Param("createdate2")String createdate2, @Param("plansdate1")String plansdate1, @Param("plansdate2")String plansdate2, @Param("protype")String protype,
                                  @Param("param")String param, @Param("current")Integer current, @Param("pageSize")Integer pageSize);

    //模糊查询回收站的项目的数量
    @Select("SELECT count(1) FROM project_info where (proState=5 or proState=6) and createrSquadId like '%${creatersquadid}%' and creater like '%${creater}%' and createDate >= #{createdate1} " +
            " and createDate <= #{createdate2} and planSDate >= #{plansdate1} and planSDate <= #{plansdate2} and proType like '%${protype}%' and proName like '%${param}%'")
    Integer selectRecProNum(@Param("creatersquadid")String creatersquadid,@Param("creater")String creater,@Param("createdate1")String createdate1,
                           @Param("createdate2")String createdate2,@Param("plansdate1")String plansdate1,@Param("plansdate2")String plansdate2,@Param("protype")String protype,
                           @Param("param")String param);

    //根据部门Id查部门
    @Select("SELECT squad FROM projectManage.dbo.[group] where squadId =#{squadId}")
    String selectSquadById(@Param("squadId")String squadId);

    //查看上线待审批项目的详细信息
    @Select("SELECT * FROM project_info where id=#{id}")
    ProjectInfo selectRecProInfo(@Param("id")Integer id);

    //查询项目的日志记录
    @Select("SELECT * FROM pro_LogRecord where proId=#{proId} ORDER BY Date asc")
    List<ProLogRecord> selectRecProLogRecord(@Param("proId")Integer proId);


    //查询项目的参与组
    @Select("SELECT * FROM project_task where proId=#{proId} ORDER BY sDate asc")
    List<ProjectTask> selectRecTask(@Param("proId")Integer proId);
}
