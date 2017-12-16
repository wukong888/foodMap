package com.marketing.system.mapper;


import com.marketing.system.entity.SystemUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface SystemUserMapper {

    List<SystemUser> selectByMap(Map<String,Object> map);

    boolean updateById(SystemUser userInfo);

    SystemUser selectByPrimaryKey(int id);

    List<Map<String, Object>> selectManagerBydepartment(Map<String,Object> map);

    List<Map<String, Object>> selectUserGroupBydepartment(Map<String,Object> map);

    SystemUser selectIdByName(String UserName);

    List<Map<String,Object>> getGroupMembers(Map<String,Object> map);

    List<Map<String,Object>> getGroupMembersByManeger(Map<String,Object> map);

    //根据id查询组员
    @Select("select id,UserName,duty,UserGroupId from SystemUser where UserGroupId =#{squadId} and job = 1")
    List<Map<String,Object>> getMembersByIdNew(@Param("squadId")String squadId);

    //根据用户名查找SystemUser的Uid
    @Select("SELECT id from SystemUser where UserName=#{name} and job = 1")
    Integer getUidByName(@Param("name")String name);

    //根据任务id查找项目创建人
    @Select("SELECT creater FROM [projectManage].[dbo].[project_info] a join [projectManage].[dbo].[project_task] b on a.proId = b.proId where b.taskId =#{taskId}")
    String getCreaterByTaskId(@Param("taskId")Integer taskId);


    //获取对应组成员
    List<Map<String,Object>> getMembersByUserGroupId(Map<String,Object> map);
}
