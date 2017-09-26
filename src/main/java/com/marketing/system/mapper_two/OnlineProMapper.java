package com.marketing.system.mapper_two;

import com.marketing.system.entity.ProjectInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OnlineProMapper {

    //模糊查询上线待审批的项目
    @Select("SELECT * FROM project_info where proState=3 and createrSquadId like '%${creatersquadid}%' and creater like '%${creater}%' and createDate >= #{createdate1} " +
            " and createDate <= #{createdate2} and planSDate >= #{plansdate1} and planSDate <= #{plansdate2} and proType like '%${protype}%' and proName like '%${param}%'" +
            " ORDER BY createDate  OFFSET (#{pageSize}*(#{current}-1))  ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<ProjectInfo> selectOnPro(@Param("creatersquadid")String creatersquadid,@Param("creater")String creater,@Param("createdate1")String createdate1,
    @Param("createdate2")String createdate2,@Param("plansdate1")String plansdate1,@Param("plansdate2")String plansdate2,@Param("protype")String protype,
    @Param("param")String param,@Param("current")Integer current,@Param("pageSize")Integer pageSize);

    //模糊查询上线待审批的项目的数量
    @Select("SELECT count(1) FROM project_info where proState=3 and createrSquadId like '%${creatersquadid}%' and creater like '%${creater}%' and createDate >= #{createdate1} and createDate <= #{createdate2} and planSDate >= #{plansdate1} and planSDate <= #{plansdate2} and proType like '%${protype}%' and proName like '%${param}%'")
    Integer selectOnProNum(@Param("creatersquadid")String creatersquadid,@Param("creater")String creater,@Param("createdate1")String createdate1,
                           @Param("createdate2")String createdate2,@Param("plansdate1")String plansdate1,@Param("plansdate2")String plansdate2,@Param("protype")String protype,
                           @Param("param")String param);
}
